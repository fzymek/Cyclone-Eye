package pl.fzymek.android.cycloneeye.game.engine;

import java.util.ArrayList;
import java.util.List;

import android.util.FloatMath;
import android.util.Log;

public class SpatialHashGrid {

	private final static String TAG = SpatialHashGrid.class.getSimpleName();

	final List<GameObject>[] dynamicCells;
	final List<GameObject>[] staticCells;
	final int cellsPerRow;
	final int cellsPerCol;
	final float cellSize;
	final int[] cellIds = new int[4];
	final List<GameObject> foundObjects;

	@SuppressWarnings("unchecked")
	public SpatialHashGrid(final float worldWidth, final float worldHeight,
			final float cellSize) {
		this.cellSize = cellSize;
		this.cellsPerRow = (int) FloatMath.ceil(worldWidth / cellSize);
		this.cellsPerCol = (int) FloatMath.ceil(worldHeight / cellSize);
		final int numCells = cellsPerRow * cellsPerCol;
		dynamicCells = new List[numCells];
		staticCells = new List[numCells];
		for (int i = 0; i < numCells; i++) {
			dynamicCells[i] = new ArrayList<GameObject>(10);
			staticCells[i] = new ArrayList<GameObject>(10);
		}
		foundObjects = new ArrayList<GameObject>(10);
		
		Log.d(TAG, "Created with parameters: "+ 
				"total cells: " + (cellsPerRow * cellsPerCol) +
				" cell size: " + cellSize +
				" cells per row: "+ cellsPerRow +
				" cells per col: "+ cellsPerCol +
				" dynamic cells: "+ dynamicCells.length + 				
				" static cells: " + staticCells.length);
		
		
	}

	public void insertStaticObject(final GameObject obj) {
		final int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			staticCells[cellId].add(obj);
			Log.d(TAG, "Insert static object: " + obj + " to cell: " + cellId);
		}
	}

	public void insertDynamicObject(final GameObject obj) {
		final int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			dynamicCells[cellId].add(obj);
			Log.d(TAG, "Insert dynamic object: " + obj + " to cell: " + cellId);
		}
	}

	public void removeObject(final GameObject obj) {
		final int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			dynamicCells[cellId].remove(obj);
			staticCells[cellId].remove(obj);
			Log.d(TAG, "Removed object: " + obj + " from cell: " + cellId);
		}
	}

	public void clearDynamicCells(GameObject obj) {
		int len = dynamicCells.length;
		for (int i = 0; i < len; i++) {
			dynamicCells[i].clear();
		}
	}

	public List<GameObject> getPotentialColliders(final GameObject obj) {
		foundObjects.clear();
		final int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			int len = dynamicCells[cellId].size();
			for (int j = 0; j < len; j++) {
				final GameObject collider = dynamicCells[cellId].get(j);
				if (!foundObjects.contains(collider)) {
					foundObjects.add(collider);
				}
			}
			len = staticCells[cellId].size();
			for (int j = 0; j < len; j++) {
				GameObject collider = staticCells[cellId].get(j);
				if (!foundObjects.contains(collider)) {
					foundObjects.add(collider);
				}
			}
		}

		// Log.d(TAG, "Potential Colliders:" + foundObjects);

		return foundObjects;
	}

	public int[] getCellIds(final GameObject obj) {

		// calculate cell ids based on object position

		// bottom left corner
		final int x1 = (int) FloatMath.floor(obj.bounds.lowerLeft.x / cellSize);
		final int y1 = (int) FloatMath.floor(obj.bounds.lowerLeft.y / cellSize);

		// top right corner
		final int x2 = (int) FloatMath.floor((obj.bounds.lowerLeft.x + obj.bounds.width) / cellSize);
		final int y2 = (int) FloatMath.floor((obj.bounds.lowerLeft.y + obj.bounds.height) / cellSize);
		
		// Log.d(TAG, "Object is placed between: (" + x1 + ", " + y1 +
		// "), and ("
		// + x2 + ", " + y2 + ")");

		if (x1 == x2 && y1 == y2) {

			// bottom left, top right cell coordinates are the same - > object
			// is
			// contained in single cell

			if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
				cellIds[0] = x1 + y1 * cellsPerRow;
			} else {
				cellIds[0] = -1;
			}
			cellIds[1] = -1;
			cellIds[2] = -1;
			cellIds[3] = -1;
		} else if (x1 == x2) {

			// bottom left in one cell, top right in cell to the right ->
			// object is in two cells horizontally

			int i = 0;
			if (x1 >= 0 && x1 < cellsPerRow) {
				if (y1 >= 0 && y1 < cellsPerCol) {
					cellIds[i++] = x1 + y1 * cellsPerRow;
				}
				if (y2 >= 0 && y2 < cellsPerCol) {
					cellIds[i++] = x1 + y2 * cellsPerRow;
				}
			}
			while (i <= 3) {
				cellIds[i++] = -1;
			}
		} else if (y1 == y2) {
			
			// bottom left in one cell, top right in cell above ->
			// object is in two cells vertically
			
			int i = 0;
			if (y1 >= 0 && y1 < cellsPerCol) {
				if (x1 >= 0 && x1 < cellsPerRow) {
					cellIds[i++] = x1 + y1 * cellsPerRow;
				}
				if (x2 >= 0 && x2 < cellsPerRow) {
					cellIds[i++] = x2 + y1 * cellsPerRow;
				}
			}
			while (i <= 3) {
				cellIds[i++] = -1;
			}
		} else {
			
			// bottom left in one cell, top right in cell to the right and above ->
			// object is in four cells
			
			int i = 0;
			final int y1CellsPerRow = y1 * cellsPerRow;
			final int y2CellsPerRow = y2 * cellsPerRow;
			if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
				cellIds[i++] = x1 + y1CellsPerRow;
			}
			if (x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
				cellIds[i++] = x2 + y1CellsPerRow;
			}
			if (x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
				cellIds[i++] = x2 + y2CellsPerRow;
			}
			if (x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
				cellIds[i++] = x1 + y2CellsPerRow;
			}
			while (i <= 3) {
				cellIds[i++] = -1;
			}
		}

		// Log.d(TAG, "Object is in cells: " + Arrays.toString(cellIds));
		return cellIds;
	}

}

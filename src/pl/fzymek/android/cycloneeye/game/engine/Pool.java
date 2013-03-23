package pl.fzymek.android.cycloneeye.game.engine;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Pool<T> {

	private final static String TAG = Pool.class.getSimpleName();

	public interface PoolObjectFactory<T> {
		public T createObject();
	}

	private final List<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize;

	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);

		Log.d(TAG, "Pool created for: " + maxSize + " objects");
	}

	public T newObject() {
		T object = null;
		if (freeObjects.isEmpty()) {
			object = factory.createObject();
		} else {
			object = freeObjects.remove(freeObjects.size() - 1);
		}
		return object;
	}

	public void free(T object) {
		if (freeObjects.size() < maxSize) {
			freeObjects.add(object);
		}
	}

}

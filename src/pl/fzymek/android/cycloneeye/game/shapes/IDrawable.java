package pl.fzymek.android.cycloneeye.game.shapes;

import javax.microedition.khronos.opengles.GL10;

public interface IDrawable {

	void draw(GL10 gl);

	void update(long time);

}

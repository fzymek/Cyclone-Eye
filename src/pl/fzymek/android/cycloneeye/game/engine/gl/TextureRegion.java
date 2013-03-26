package pl.fzymek.android.cycloneeye.game.engine.gl;


public class TextureRegion {
	
	private final static String TAG = TextureRegion.class.getSimpleName();
	
	public final float u1, v1;
	public final float u2, v2;
	public final Texture texture;

	public TextureRegion(Texture texture, float x, float y, float width,
			float height) {
		this.u1 = x / texture.width;
		this.v1 = y / texture.height;
		this.u2 = this.u1 + width / texture.width;
		this.v2 = this.v1 + height / texture.height;
		this.texture = texture;
		
		// Log.d(TAG, "Created with" +
		// " u1: " + u1 +
		// " v1: " + v1 +
		// " u2: " + u2 +
		// " v2: " + v2 +
		// texture );
				
	}

	@Override
	public String toString() {
		return new StringBuffer().append("u1: " + u1).append(" v1: " + v1)
				.append(" u2: " + u2).append(" v2: " + v2).toString();
	}
}

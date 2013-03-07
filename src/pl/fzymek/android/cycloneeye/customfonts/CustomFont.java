package pl.fzymek.android.cycloneeye.customfonts;

public enum CustomFont {

	PIPE_DREAM(0, "fonts/PIPE_DREAM.ttf");

	private final int id;
	private final String resourcePath;

	private CustomFont(final int id, final String resourcePath) {
		this.id = id;
		this.resourcePath = resourcePath;
	}

	public int getId() {
		return id;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public static CustomFont fromId(int id) {
		for (CustomFont f : CustomFont.values()) {
			if (id == f.getId()) {
				return f;
			}
		}
		return null;
	}

}

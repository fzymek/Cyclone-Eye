package pl.fzymek.android.cycloneeye.database.dao;

public class Highscore {

	private String player;
	private String score;

	public Highscore(String player, String score) {
		super();
		this.player = player;
		this.score = score;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}

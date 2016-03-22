package de.reelos.wordfilter.database;

public class BlackWord {
	private String playername;
	private String word;
	private int flag;
	
	public BlackWord(String playername, String word, int flag){
		this.setPlayername(playername);
		this.setWord(word);
		this.setFlag(flag);
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}

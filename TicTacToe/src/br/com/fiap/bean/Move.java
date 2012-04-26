package br.com.fiap.bean;

public class Move {
	
	private int move;
	private String message;
	
	public Move(){}

	

	public Move(int move, String message) {
		super();
		this.move = move;
		this.message = message;
	}



	public int getMove() {
		return move;
	}



	public void setMove(int move) {
		this.move = move;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}

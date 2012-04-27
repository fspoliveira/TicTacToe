package br.com.fiap.bean;

public class Move {

	private int move;
	private String message;
	private Mark id;

	public Move() {
	}

	public Move(int move, String message) {
		super();
		this.move = move;
		this.message = message;
	}

	public Move(int move, String message, Mark id) {
		super();
		this.move = move;
		this.message = message;
		this.id = id;
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

	public Mark getId() {
		return id;
	}

	public void setId(Mark id) {
		this.id = id;
	}

}

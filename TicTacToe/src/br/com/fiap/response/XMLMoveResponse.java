package br.com.fiap.response;

import br.com.fiap.bean.Mark;
import br.com.fiap.bean.Move;

public class XMLMoveResponse {
	private Mark mark;
	private Move move;
	private String message;
		
	public XMLMoveResponse(){
		
	}

	public XMLMoveResponse(Mark mark) {
		super();
		this.mark = mark;
	}
	

	public XMLMoveResponse(Mark mark, Move move, String message) {
		super();
		this.mark = mark;
		this.move = move;
		this.message = message;
	}

	public XMLMoveResponse(Mark mark, Move move) {
		super();
		this.mark = mark;
		this.move = move;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

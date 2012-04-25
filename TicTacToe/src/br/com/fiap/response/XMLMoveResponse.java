package br.com.fiap.response;

import br.com.fiap.bean.Mark;

public class XMLMoveResponse {
	private Mark mark;
	
	public XMLMoveResponse(){
		
	}

	public XMLMoveResponse(Mark mark) {
		super();
		this.mark = mark;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}
	
	
}

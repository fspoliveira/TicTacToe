package br.com.fiap.response;

import br.com.fiap.bean.Move;
import br.com.fiap.request.XMLMoveRequest;

import com.thoughtworks.xstream.XStream;

public class XMLMoveResponse {

	private XMLMoveRequest xmr;
	private Move move;
	XStream xt;
	private String location;
	private String message;
	
	
	public XMLMoveResponse(){}
	

	public XMLMoveResponse(String location, String message) {
		super();
		this.location = location;
		this.message = message;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String createXMLServer() {

		xmr = new XMLMoveRequest();
		move = new Move();

		move.setMove(location);
		move.setMessage(message);

		xmr.setMovePlayer(move);

		xt = new XStream();
		xt.alias("ticTacToeMove", XMLMoveRequest.class);
		return xt.toXML(xmr);

	}

}

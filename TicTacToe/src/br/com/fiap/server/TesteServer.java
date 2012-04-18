package br.com.fiap.server;

import javax.swing.JFrame;

public class TesteServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TicTacToeServer application = new TicTacToeServer();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.execute();

	}

}

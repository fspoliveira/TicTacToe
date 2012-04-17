package br.com.fiap.server;

import javax.swing.JFrame;

public class TicTacToeServerTest implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		TicTacToeServer application = new TicTacToeServer();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.execute();

	}
} // end class TicTacToeServerTest
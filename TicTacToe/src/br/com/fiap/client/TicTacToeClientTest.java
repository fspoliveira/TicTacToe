package br.com.fiap.client;

import javax.swing.JFrame;

public class TicTacToeClientTest implements Runnable {

	public void run() {
		// TODO Auto-generated method stub

		TicTacToeClient application; // declare client application

		// if no command line args

		application = new TicTacToeClient("127.0.0.1"); // localhost
		// application = new TicTacToeClient( "192.168.0.21" ); // localhost

		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
} // end class TicTacToeClientTest
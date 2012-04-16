package br.com.fiap.playgame;

import br.com.fiap.client.TicTacToeClientTest;
import br.com.fiap.server.TicTacToeServerTest;

public class PlayGame {
	public static void main( String args[]){
		
		TicTacToeServerTest tt = new TicTacToeServerTest();
		TicTacToeServerTest.main(null);
		
		
		TicTacToeClientTest tc = new TicTacToeClientTest();
		TicTacToeClientTest.main(null);
		
		TicTacToeClientTest td = new TicTacToeClientTest();
		TicTacToeClientTest.main(null);
		
	}
}

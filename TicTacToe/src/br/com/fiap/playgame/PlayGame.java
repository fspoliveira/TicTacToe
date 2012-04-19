package br.com.fiap.playgame;

import br.com.fiap.client.TicTacToeClientTest;
import br.com.fiap.server.TicTacToeServerTest;

public class PlayGame {
	public static void main( String args[]){
		
		TicTacToeServerTest tt = new TicTacToeServerTest();		
		TicTacToeClientTest tc = new TicTacToeClientTest();	
		TicTacToeClientTest td = new TicTacToeClientTest();	
		
		Thread t1 = new Thread(tt);
		Thread t2 = new Thread(tc);
		Thread t3 = new Thread(td);
		
		// inicia as threads
		
		t1.start();
		t2.start();	
		t3.start();
		
	}
}
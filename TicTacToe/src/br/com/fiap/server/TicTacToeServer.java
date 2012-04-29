package br.com.fiap.server;

import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.com.fiap.bean.DrawLine;
import br.com.fiap.bean.Mark;
import br.com.fiap.bean.Move;
import br.com.fiap.request.XMLMoveRequest;
import br.com.fiap.response.XMLMoveResponse;
import br.com.fiap.validate.ValidateMove;

import com.thoughtworks.xstream.XStream;

public class TicTacToeServer extends JFrame {
	/**
	 */
	private static final long serialVersionUID = 1L;
	private String[] board = new String[9]; // tic-tac-toe board
	private JTextArea outputArea; // for outputting moves
	private Player[] players; // array of Players
	private ServerSocket server; // server socket to connect with clients
	private int currentPlayer; // keeps track of player with current move
	private final static int PLAYER_X = 0; // constant for first player
	private final static int PLAYER_O = 1; // constant for second player
	private final static String[] MARKS = { "X", "O" }; // array of marks
	private ExecutorService runGame; // will run players
	private Lock gameLock; // to lock game for synchronization
	private Condition otherPlayerConnected; // to wait for other player
	private Condition otherPlayerTurn; // to wait for other player's turn
	private boolean weHaveAWinner = false;
	private DrawLine line;

	// set up tic-tac-toe server and GUI that displays messages
	public TicTacToeServer() {
		super("Tic-Tac-Toe Server"); // set title of window

		// create ExecutorService with a thread for each player
		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock(); // create lock for game

		// condition variable for both players being connected
		otherPlayerConnected = gameLock.newCondition();

		// condition variable for the other player's turn
		otherPlayerTurn = gameLock.newCondition();

		for (int i = 0; i < 9; i++)
			board[i] = new String(""); // create tic-tac-toe board
		players = new Player[2]; // create array of players
		currentPlayer = PLAYER_X; // set current player to first player

		try {
			server = new ServerSocket(12345, 2); // set up ServerSocket
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
			System.exit(1);
		} // end catch

		outputArea = new JTextArea(); // create JTextArea for output
		add(outputArea, BorderLayout.CENTER);
		outputArea.setText("Server awaiting connections\n");

		setSize(300, 300); // set size of window
		setVisible(true); // show window
	} // end TicTacToeServer constructor

	// wait for two connections so game can be played
	public void execute() {
		// wait for each client to connect
		for (int i = 0; i < players.length; i++) {
			try // wait for connection, create Player, start runnable
			{
				// New Player
				players[i] = new Player(server.accept(), i);
				runGame.execute(players[i]); // execute player runnable
			} // end try
			catch (IOException ioException) {
				ioException.printStackTrace();
				System.exit(1);
			} // end catch
		} // end for

		gameLock.lock(); // lock game to signal player X's thread

		try {
			players[PLAYER_X].setSuspended(false); // resume player X
			otherPlayerConnected.signal(); // wake up player X's thread
		} // end try
		finally {
			gameLock.unlock(); // unlock game after signalling player X
		} // end finally
	} // end method execute

	// display message in outputArea
	private void displayMessage(final String messageToDisplay) {
		// display message from event-dispatch thread of execution
		SwingUtilities.invokeLater(new Runnable() {
			public void run() // updates outputArea
			{
				outputArea.append(messageToDisplay); // add message
			} // end method run
		} // end inner class
				); // end call to SwingUtilities.invokeLater
	} // end method displayMessage

	public boolean checkWinning(int player, int location) {

		if (location == 0) {
			if ((board[1].equals(MARKS[player]) && board[2]
					.equals(MARKS[player]))
					|| (board[4].equals(MARKS[player]) && board[8]
							.equals(MARKS[player]))
					|| (board[3].equals(MARKS[player]) && board[6]
							.equals(MARKS[player]))) {

				if ((board[1].equals(MARKS[player]) && board[2]
						.equals(MARKS[player]))) {
					line = new DrawLine(0, 15, 900, 15);

				} else if (board[4].equals(MARKS[player])
						&& board[8].equals(MARKS[player])) {
					line = new DrawLine(0, 0, 90, 90);

				} else if (board[3].equals(MARKS[player])
						&& board[6].equals(MARKS[player])) {
					line = new DrawLine(14, 0, 0, 3000);
				}

				return true;
			}

		} else if (location == 1) {
			if ((board[0].equals(MARKS[player]) && board[2]
					.equals(MARKS[player]))
					|| (board[4].equals(MARKS[player]) && board[7]
							.equals(MARKS[player]))) {

				if ((board[0].equals(MARKS[player]) && board[2]
						.equals(MARKS[player]))) {
					line = new DrawLine(0, 15, 900, 15);
				} else if (board[4].equals(MARKS[player])
						&& board[7].equals(MARKS[player])) {
					line = new DrawLine(48, 0, 0, 3000);
				}

				return true;
			}
		} else if (location == 2) {
			if ((board[0].equals(MARKS[player]) && board[1]
					.equals(MARKS[player]))
					|| (board[4].equals(MARKS[player]) && board[6]
							.equals(MARKS[player]))
					|| (board[5].equals(MARKS[player]) && board[8]
							.equals(MARKS[player]))) {

				if ((board[0].equals(MARKS[player]) && board[1]
						.equals(MARKS[player]))) {
					line = new DrawLine(0, 15, 900, 15);

				} else if (board[4].equals(MARKS[player])
						&& board[6].equals(MARKS[player])) {
					line = new DrawLine(90, 0, 0, 90);

				} else if (board[5].equals(MARKS[player])
						&& board[8].equals(MARKS[player])) {
					line = new DrawLine(78, 0, 0, 3000);
				}

				return true;
			}
		} else if (location == 3) {
			if ((board[0].equals(MARKS[player]) && board[6]
					.equals(MARKS[player]))
					|| (board[4].equals(MARKS[player]) && board[5]
							.equals(MARKS[player]))) {

				if ((board[0].equals(MARKS[player]) && board[6]
						.equals(MARKS[player]))) {
					line = new DrawLine(14, 0, 0, 3000);

				} else if ((board[4].equals(MARKS[player]) && board[5]
						.equals(MARKS[player]))) {
					line = new DrawLine(0, 45, 900, 45);
				}
				return true;
			}
		} else if (location == 4) {
			if ((board[1].equals(MARKS[player]) && board[7]
					.equals(MARKS[player]))
					|| (board[2].equals(MARKS[player]) && board[6]
							.equals(MARKS[player]))
					|| (board[0].equals(MARKS[player]) && board[8]
							.equals(MARKS[player]))
					|| (board[3].equals(MARKS[player]) && board[5]
							.equals(MARKS[player]))) {

				// Draw Line
				if ((board[1].equals(MARKS[player]) && board[7]
						.equals(MARKS[player]))) {
					line = new DrawLine(48, 0, 0, 3000);

				} else if ((board[2].equals(MARKS[player]) && board[6]
						.equals(MARKS[player]))) {
					line = new DrawLine(90, 0, 0, 90);

				} else if ((board[0].equals(MARKS[player]) && board[8]
						.equals(MARKS[player]))) {
					line = new DrawLine(0, 0, 90, 90);

				} else if (board[3].equals(MARKS[player])
						&& board[5].equals(MARKS[player])) {
					line = new DrawLine(0, 45, 900, 45);

				}

				return true;
			}
		} else if (location == 5) {
			if ((board[2].equals(MARKS[player]) && board[8]
					.equals(MARKS[player]))
					|| (board[3].equals(MARKS[player]) && board[4]
							.equals(MARKS[player]))) {

				// Draw Line
				if ((board[2].equals(MARKS[player]) && board[8]
						.equals(MARKS[player]))) {
					line = new DrawLine(78, 0, 0, 3000);

				} else if (board[3].equals(MARKS[player])
						&& board[4].equals(MARKS[player])) {
					line = new DrawLine(0, 45, 900, 45);
				}

				return true;
			}
		} else if (location == 6) {
			if ((board[0].equals(MARKS[player]) && board[3]
					.equals(MARKS[player]))
					|| (board[4].equals(MARKS[player]) && board[2]
							.equals(MARKS[player]))
					|| (board[7].equals(MARKS[player]) && board[8]
							.equals(MARKS[player]))) {

				// Draw Line
				if ((board[0].equals(MARKS[player]) && board[3]
						.equals(MARKS[player]))) {

					line = new DrawLine(15, 0, 0, 3000);

				} else if (board[4].equals(MARKS[player])
						&& board[2].equals(MARKS[player])) {

					line = new DrawLine(90, 0, 0, 90);

				} else if ((board[7].equals(MARKS[player]) && board[8]
						.equals(MARKS[player]))) {

					line = new DrawLine(0, 75, 900, 75);
				}

				return true;
			}
		} else if (location == 7) {
			if ((board[1].equals(MARKS[player]) && board[4]
					.equals(MARKS[player]))
					|| (board[6].equals(MARKS[player]) && board[8]
							.equals(MARKS[player]))) {

				// Draw Line
				if (board[1].equals(MARKS[player])
						&& board[4].equals(MARKS[player])) {
					line = new DrawLine(48, 0, 0, 3000);

				} else if ((board[6].equals(MARKS[player]) && board[8]
						.equals(MARKS[player]))) {
					line = new DrawLine(0, 75, 900, 75);

				}
				return true;

			}
		} else if (location == 8) {
			if ((board[5].equals(MARKS[player]) && board[2]
					.equals(MARKS[player]))
					|| (board[4].equals(MARKS[player]) && board[0]
							.equals(MARKS[player]))
					|| (board[6].equals(MARKS[player]) && board[7]
							.equals(MARKS[player]))) {

				// Draw Line
				if (board[4].equals(MARKS[player])
						&& board[0].equals(MARKS[player])) {
					line = new DrawLine(0, 0, 90, 90);

				} else if (board[5].equals(MARKS[player])
						&& board[2].equals(MARKS[player])) {
					line = new DrawLine(78, 0, 0, 3000);

				} else if (board[6].equals(MARKS[player])
						&& board[7].equals(MARKS[player])) {

					line = new DrawLine(0, 75, 900, 75);

				}

				return true;
			}
		}

		return false;

	}

	// determine if move is valid
	public boolean validateAndMove(int location, int player) {
		// while not current player, must wait for turn
		while (player != currentPlayer) {
			gameLock.lock(); // lock game to wait for other player to go

			try {
				otherPlayerTurn.await(); // wait for player's turn
			} // end try
			catch (InterruptedException exception) {
				exception.printStackTrace();
			} // end catch
			finally {
				gameLock.unlock(); // unlock game after waiting
			} // end finally
		} // end while

		// if location not occupied, make move
		if (!isOccupied(location)) {
			board[location] = MARKS[currentPlayer]; // set move on board

			if (checkWinning(currentPlayer, location)) {
				weHaveAWinner = true;
			}

			currentPlayer = (currentPlayer + 1) % 2; // change player

			// let new current player know that move occurred
			players[currentPlayer].otherPlayerMoved(location);

			gameLock.lock(); // lock game to signal other player to go

			try {
				otherPlayerTurn.signal(); // signal other player to continue
			} // end try
			finally {
				gameLock.unlock(); // unlock game after signaling
			} // end finally

			return true; // notify player that move was valid
		} // end if
		else
			// move was not valid
			return false; // notify player that move was invalid
	} // end method validateAndMove

	// determine whether location is occupied
	public boolean isOccupied(int location) {
		if (board[location].equals(MARKS[PLAYER_X])
				|| board[location].equals(MARKS[PLAYER_O]))
			return true; // location is occupied
		else
			return false; // location is not occupied
	} // end method isOccupied

	// place code in this method to determine whether game over
	public boolean isGameOver() {
		return false; // this is left as an exercise
	} // end method isGameOver

	// private inner class Player manages each Player as a runnable
	private class Player implements Runnable {
		private Socket connection; // connection to client
		private Scanner input; // input from client
		private Formatter output; // output to client
		private int playerNumber; // tracks which player this is
		private String mark; // mark for this player
		private boolean suspended = true; // whether thread is suspended
		private Move move;
		private Mark markPlayer;
		private XMLMoveResponse xmlMoveResponse;
		private XStream xstream;
		private String xml;
		private ValidateMove validate;

		// set up Player thread
		public Player(Socket socket, int number) {
			playerNumber = number; // store this player's number
			mark = MARKS[playerNumber]; // specify player's mark
			connection = socket; // store socket for client

			try // obtain streams from Socket
			{
				input = new Scanner(connection.getInputStream());
				output = new Formatter(connection.getOutputStream());
			} // end try
			catch (IOException ioException) {
				ioException.printStackTrace();
				System.exit(1);
			} // end catch
		} // end Player constructor

		// send message that other player moved
		public void otherPlayerMoved(int location) {
			if (weHaveAWinner) {

				/*
				 * output.format("You Loose\n"); output.format("%d\n",
				 * location);
				 */

				output.format("%s\n", createXMLResponse("You Loose", location,
						line));
				output.flush(); // flush output

			}

			else {
				/*
				 * output.format("Opponent moved\n"); output.format("%d\n",
				 * location); // send location of move
				 */

				output.format("%s\n", createXMLResponse("Opponent moved",
						location));
				output.flush(); // flush output
			}
		} // end method otherPlayerMoved

		public String createXMLResponse(String msg, int location) {
			markPlayer = new Mark(mark);
			move = new Move(location, msg);

			// Create XML Server Response
			xmlMoveResponse = new XMLMoveResponse();
			xmlMoveResponse.setMark(markPlayer);
			xmlMoveResponse.setMove(move);

			xstream = new XStream();
			xstream.alias("ticTacToe", XMLMoveResponse.class);
			return xstream.toXML(xmlMoveResponse);

		}

		public String createXMLResponse(String msg, int location, DrawLine line) {
			markPlayer = new Mark(mark);
			move = new Move(location, msg);

			// Create XML Server Response
			xmlMoveResponse = new XMLMoveResponse();
			xmlMoveResponse.setMark(markPlayer);
			xmlMoveResponse.setMove(move);
			xmlMoveResponse.setLine(line);

			xstream = new XStream();
			xstream.alias("ticTacToe", XMLMoveResponse.class);
			return xstream.toXML(xmlMoveResponse);

		}

		// control thread's execution
		public void run() {
			// send client its mark (X or O), process messages from client
			try {

				markPlayer = new Mark(mark);

				// Create XML Server Response
				xmlMoveResponse = new XMLMoveResponse();
				xmlMoveResponse.setMark(markPlayer);

				xstream = new XStream();
				xstream.alias("ticTacToe", XMLMoveResponse.class);
				xml = xstream.toXML(xmlMoveResponse);

				displayMessage("Player " + mark + " connected\n");

				output.format("%s\n", xml); // send player's mark
				output.flush(); // flush output

				// if player X, wait for another player to arrive
				if (playerNumber == PLAYER_X) {

					/*
					 * output.format("%s\n%s", "Player X connected",
					 * "Waiting for another player\n");
					 */

					// output.format(createXMLResponse("Player X connected" +
					// "Waiting for another player",0));

					// output.flush(); // flush output

					gameLock.lock(); // lock game to wait for second player

					try {
						while (suspended) {
							otherPlayerConnected.await(); // wait for player O
						} // end while
					} // end try
					catch (InterruptedException exception) {
						exception.printStackTrace();
					} // end catch
					finally {
						gameLock.unlock(); // unlock game after second player
					} // end finally

					// send message that other player connected

					// output.format("Other player connected. Your move.\n");

					output.format("%s\n", createXMLResponse(
							"Other player connected. Your move.", 0));

					output.flush(); // flush output
				} // end if
				else {
					// output.format("Player O connected, please wait\n");
					// output.format("%s\n",
					// createXMLResponse("Player O connected, please wait", 0));

				} // end else

				// while game not over
				while (!isGameOver()) {
					int location = 0; // initialize move location
					String xml = "";

					try {
						// Fix String Buffer (1024) class Scanner
						input = new Scanner(connection.getInputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// Get XML Move
					xml = input.useDelimiter("\\z").next().trim();

					System.out
							.println("To no server recebendo o movimento do cliente\n"
									+ xml);

					// Validate Move
					validate = new ValidateMove(xml);
					try {
						validate.validateXMLSchema();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println(xml.toString());

					// Parse String Request XMLfrom Client to Object
					XStream xt = new XStream();
					xt.alias("ticTacToeMove", XMLMoveRequest.class);

					XMLMoveRequest request = (XMLMoveRequest) xt.fromXML(xml);
					Move move = request.getMovePlayer();

					location = move.getMove();

					// check for valid move
					if (validateAndMove(location, playerNumber)) {
						displayMessage("\nlocation: " + location);
						// output.format("Valid move.\n"); // notify client

						if (weHaveAWinner) {
							output.format("%s\n", createXMLResponse("You Win",
									0, line));
							output.flush(); // flush output
						} else {
							output.format("%s\n", createXMLResponse(
									"Valid move.", 0));
							output.flush(); // flush output

						}

						System.out.println(weHaveAWinner);

						// if (weHaveAWinner)
						// output.format("You Win :-) \n");
						// output.format("%s\n",
						// createXMLResponse("You Win :-)", 0));

					} // end if
					else // move was invalid
					{
						// output.format("Invalid move, try again\n");
						output.format("%s\n", createXMLResponse(
								"Invalid move, try again", 0));

						output.flush(); // flush output
					} // end else
				} // end while
			} // end try
			finally {
				try {
					connection.close(); // close connection to client
				} // end try
				catch (IOException ioException) {
					ioException.printStackTrace();
					System.exit(1);
				} // end catch
			} // end finally
		} // end method run

		// set whether or not thread is suspended
		public void setSuspended(boolean status) {
			suspended = status; // set value of suspended
		} // end method setSuspended
	} // end class Player
} // end class TicTacToeServer
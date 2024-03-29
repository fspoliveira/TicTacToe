package br.com.fiap.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.com.fiap.bean.DrawLine;
import br.com.fiap.bean.Mark;
import br.com.fiap.bean.Move;
import br.com.fiap.request.XMLMoveRequest;
import br.com.fiap.response.XMLMoveResponse;
import br.com.fiap.validate.ValidateResponseServer;
import com.thoughtworks.xstream.XStream;

import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class TicTacToeClient extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField idField; // textfield to display player's mark
	private JTextArea displayArea; // JTextArea to display output
	private JPanel boardPanel; // panel for tic-tac-toe board
	private JPanel panel2; // panel to hold board
	private Square board[][]; // tic-tac-toe board
	private Square currentSquare; // current square
	private Socket connection; // connection to server
	private Scanner input; // input from server
	private Formatter output; // output to server
	private String ticTacToeHost; // host name for server
	private String myMark; // this client's mark
	private boolean myTurn; // determines which client's turn it is
	private final String X_MARK = "X"; // mark for first client
	private final String O_MARK = "O"; // mark for second client
	private XMLMoveRequest xmr;
	private Move move;
	private XStream xt;
	private String xml;
	private Mark id;

	// set up user-interface and board
	public TicTacToeClient(String host) {

		super("Tic-Tac-Toe Client"); // set title of window

		ticTacToeHost = host; // set name of server
		displayArea = new JTextArea(4, 30); // set up JTextArea
		displayArea.setEditable(false);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);

		boardPanel = new JPanel(); // set up panel for squares in board
		boardPanel.setLayout(new GridLayout(3, 3, 0, 0));

		board = new Square[3][3]; // create board

		// loop over the rows in the board
		for (int row = 0; row < board.length; row++) {
			// loop over the columns in the board
			for (int column = 0; column < board[row].length; column++) {
				// create square
				board[row][column] = new Square(" ", row * 3 + column);
				boardPanel.add(board[row][column]); // add square
			} // end inner for
		} // end outer for

		idField = new JTextField(); // set up textfield
		idField.setEditable(false);
		add(idField, BorderLayout.NORTH);

		panel2 = new JPanel(); // set up panel to contain boardPanel
		panel2.add(boardPanel, BorderLayout.CENTER); // add board panel
		add(panel2, BorderLayout.CENTER); // add container panel

		setSize(300, 225); // set size of window
		setVisible(true); // show window

		startClient();
	} // end TicTacToeClient constructor

	// start the client thread
	public void startClient() {
		try // connect to server, get streams and start outputThread
		{
			// make connection to server
			connection = new Socket(InetAddress.getByName(ticTacToeHost), 12345);

			// get streams for input and output
			input = new Scanner(connection.getInputStream());
			output = new Formatter(connection.getOutputStream());
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch

		// create and start worker thread for this client
		ExecutorService worker = Executors.newFixedThreadPool(1);
		worker.execute(this); // execute client
	} // end method startClient

	// control thread that allows continuous update of displayArea
	public void run() {

		// myMark = input.nextLine(); // get player's mark (X or O)

		String xmlResponse = input.useDelimiter("\\z").next().trim(); // get
																		// player's
																		// mark
																		// (X or
																		// O)
		System.out.println("*************************************************");
		System.out.println("Method run of client");
		System.out.println("*************************************************");
		System.out.println(xmlResponse + "\n");
		

		// Parse Response String from Server XML to Object
		XStream xt = new XStream();
		xt.alias("ticTacToe", XMLMoveResponse.class);
		XMLMoveResponse response = (XMLMoveResponse) xt.fromXML(xmlResponse);
		myMark = response.getMark().getMark();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// display player's mark
				idField.setText("You are player \"" + myMark + "\"");
			} // end method run
		} // end anonymous inner class
				); // end call to SwingUtilities.invokeLater

		myTurn = (myMark.equals(X_MARK)); // determine if client's turn

		// receive messages sent to client and output them
		while (true) {

			// teste
			try {
				// Fix String Buffer (1024) clas Scanner
				input = new Scanner(connection.getInputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// if (input.hasNextLine())
			// processMessage(input.nextLine());

			processMessage(input.useDelimiter("\\z").next().trim());

		} // end while

	} // end method run

	private void drawLine(final DrawLine line) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				Graphics g = boardPanel.getGraphics();
				g.setColor(Color.RED);
				g.drawLine(line.getX1(),line.getY1(),line.getX2(), line.getY2());

			}
		});

	}

	// process messages received by client
	private void processMessage(String xml) {
		// valid move occurred

		System.out.println("*************************************************");
		System.out.println("XML Server response in client");
		System.out.println("*************************************************");
		System.out.println( xml + "\n");
		
		//Validate Server XML Response
		ValidateResponseServer vrs = new ValidateResponseServer(xml);
		try {
			vrs.validateXMLSchema();
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

		// Parse XML Response from Server
		XStream xt = new XStream();
		xt.alias("ticTacToe", XMLMoveResponse.class);

		XMLMoveResponse response = (XMLMoveResponse) xt.fromXML(xml);
		
		if (response.getMove().getMessage().equals("It's a tie")){
			
			int location = response.getMove().getMove();

			int row = location / 3; // calculate row
			int column = location % 3; // calculate column
			
			setMark(board[row][column], (myMark.equals(X_MARK) ? O_MARK
					: X_MARK)); // mark move

			board[row][column].setBackground(myMark.equals(X_MARK) ? Color.yellow
					: Color.green);
			
			setMark(currentSquare, myMark); // set mark in square

			// Set Color for Player
			currentSquare.setBackground(myMark.equals(X_MARK) ? Color.green
					: Color.yellow);
			
			
			displayMessage("It's a tie\n");
			
			myTurn = false; 
		}
		else if(response.getMove().getMessage().equals("You Win")) {
			displayMessage("You Win :-)\n");

			setMark(currentSquare, myMark); // set mark in square

			// Set Color for Player
			currentSquare.setBackground(myMark.equals(X_MARK) ? Color.green
					: Color.yellow);
			
			DrawLine drawLine = new DrawLine(response.getLine().getX1(),
					response.getLine().getY1(),
					response.getLine().getX2(),
					response.getLine().getY2());
			drawLine(drawLine);
		}
		
		else if (response.getMove().getMessage().equals("Valid move.")) {
			displayMessage("Valid move, please wait.\n");

			setMark(currentSquare, myMark); // set mark in square

			// Set Color for Player
			currentSquare.setBackground(myMark.equals(X_MARK) ? Color.green
					: Color.yellow);

		} // end if
		else if (response.getMove().getMessage()
				.equals("Invalid move, try again")) {
			displayMessage(response.getMove().getMessage() + "\n"); // display
																	// invalid
																	// move
			myTurn = true; // still this client's turn
		} // end else if
		else if (response.getMove().getMessage().equals("Opponent moved")) {
			/*
			 * int location = input.nextInt(); // get move location
			 * input.nextLine(); // skip newline after int location
			 */

			int location = response.getMove().getMove();

			int row = location / 3; // calculate row
			int column = location % 3; // calculate column

			setMark(board[row][column], (myMark.equals(X_MARK) ? O_MARK
					: X_MARK)); // mark move

			board[row][column].setBackground(myMark.equals(X_MARK) ? Color.yellow
					: Color.green);

			displayMessage("Opponent moved. Your turn.\n");
			myTurn = true; // now this client's turn
		} // end else if

		else if (response.getMove().getMessage().equals("You Loose")) {
			/*
			 * int location = input.nextInt(); // get move location
			 * input.nextLine(); // skip newline after int location
			 */
			int location = response.getMove().getMove();
			int row = location / 3; // calculate row
			int column = location % 3; // calculate column
			
			setMark(board[row][column], (myMark.equals(X_MARK) ? O_MARK
					: X_MARK)); // mark move

			// Set Color board
			board[row][column].setBackground(myMark.equals(X_MARK) ? Color.yellow
					: Color.green);

			displayMessage("You Loose :-(");

			DrawLine drawLine = new DrawLine(response.getLine().getX1(),
					response.getLine().getY1(),
					response.getLine().getX2(),
					response.getLine().getY2());
			drawLine(drawLine);
			
			myTurn = false; // now this client's turn			

		} // end else if

		else if (response.getMove().getMessage().equals("You Win")) {

			displayMessage(response.getMove().getMessage() + "\n"); // display
																	// invalid
																	// move

			myTurn = false; // now this client's turn
		} // end else if

		else
			displayMessage(response.getMove().getMessage() + "\n"); // display
																	// the
																	// message
	} // end method processMessage

	// manipulate outputArea in event-dispatch thread
	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				displayArea.append(messageToDisplay); // updates output
			} // end method run
		} // end inner class
				); // end call to SwingUtilities.invokeLater
	} // end method displayMessage

	// utility method to set mark on board in event-dispatch thread
	private void setMark(final Square squareToMark, final String mark) {
		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		squareToMark.setMark(mark); // set mark in square

		// } // end method run
		// } // end anonymous inner class
		// ); // end call to SwingUtilities.invokeLater
	} // end method setMark

	// send message to server indicating clicked square
	public void sendClickedSquare(int location) {
		// if it is my turn
		if (myTurn) {

			// Send move by XML

			try {
				output = new Formatter(connection.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			xmr = new XMLMoveRequest();

			id = new Mark(myMark);

			move = new Move();
			move.setMove(location);
			move.setMessage("");
			move.setId(id);

			xmr.setMovePlayer(move);

			xt = new XStream();
			xt.alias("ticTacToeMove", XMLMoveRequest.class);
			xml = xt.toXML(xmr);

			// output.format("%d\n", teste); // send location to server
			output.format("%s\n", xml); // send location to server

			output.flush();

			myTurn = false; // not my turn anymore
		} // end if
	} // end method sendClickedSquare

	// set current Square
	public void setCurrentSquare(Square square) {
		currentSquare = square; // set current square to argument
	} // end method setCurrentSquare

	// private inner class for the squares on the board
	private class Square extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String mark; // mark to be drawn in this square
		private int location; // location of square

		public Square(String squareMark, int squareLocation) {
			mark = squareMark; // set mark for this square
			location = squareLocation; // set location of this square

			addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					setCurrentSquare(Square.this); // set current square

					// send location of this square
					sendClickedSquare(getSquareLocation());
				} // end method mouseReleased
			} // end anonymous inner class
			); // end call to addMouseListener
		} // end Square constructor

		// return preferred size of Square
		public Dimension getPreferredSize() {
			return new Dimension(30, 30); // return preferred size
		} // end method getPreferredSize

		// return minimum size of Square
		public Dimension getMinimumSize() {
			return getPreferredSize(); // return preferred size
		} // end method getMinimumSize

		// set mark for Square
		public void setMark(String newMark) {
			mark = newMark; // set mark of square
			repaint(); // repaint square
		} // end method setMark

		// return Square location
		public int getSquareLocation() {
			return location; // return location of square
		} // end method getSquareLocation

		// draw Square
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawRect(0, 0, 29, 29); // draw square
			g.drawString(mark, 11, 20); // draw mark
		} // end method paintComponent
	} // end inner-class Square
} // end class TicTacToeClient


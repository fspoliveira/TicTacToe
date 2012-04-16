package br.com.fia.madrugada;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketServer {
	 public static void main(String args[]) throws Exception {
		    ServerSocket serverSocket;
		    int portNumber = 1777;
		    Socket socket;
		    String str;
		    
		    int re = 1;

		    str = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		    str += "<ticketRequest><customer custID=\"1\">";
		    str += "</ticketRequest>";
		    
		    

		    serverSocket = new ServerSocket(portNumber);

		    System.out.println("Waiting for a connection on " + portNumber);

		    while (true) {	
		    socket = serverSocket.accept();

		    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

		    oos.writeObject(str + re++);

		    oos.close();

		    socket.close();
		    }

		  }

}

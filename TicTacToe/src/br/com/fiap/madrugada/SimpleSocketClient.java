package br.com.fiap.madrugada;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SimpleSocketClient {
	
	public static void main(String args[]) throws Exception {
	    Socket socket;
	    int portNumber = 1777;
	    String str = "";

	    socket = new Socket(InetAddress.getLocalHost(), portNumber);

	    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	    str = (String) ois.readObject();
	    System.out.println(str);
	  }


}

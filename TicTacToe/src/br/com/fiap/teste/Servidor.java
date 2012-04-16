package br.com.fiap.teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;



public class Servidor {

	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket servidor = null;
		String xml = new String();   

		try {
			servidor = new ServerSocket(7000);
			System.out.println("Servidor online !!!");
			while (true) {				
				
				
				socket = servidor.accept();				
				
				
			/*	BufferedReader entrada = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				
				String linha = entrada.readLine();
				
				System.out.println(linha);
				*/
			
				Scanner entradaXml = new Scanner(socket.getInputStream());     
				
				 while (entradaXml.hasNextLine()) {   
		                xml = xml + entradaXml.nextLine();   
		            }   

				
				//Teste XML
				XStream xt = new XStream();
				xt.alias("ida", XMLRequest.class);
				
				XMLRequest request =   (XMLRequest) xt.fromXML(xml);
				Mensagem msg  = request.getMensagem();
				
				
				
				//Fim testes XML
				
				
//				if (linha.equals(":q")) {
//					break;		
//				}
				
				//System.out.println("[" + socket.getInetAddress().getHostAddress() + "] - " + linha);
				System.out.println("[" + socket.getInetAddress().getHostAddress() + "] - " + msg.getMensagem());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (servidor != null) {
					servidor.close();	
				}
				
				if (socket != null) {
					socket.close();	
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

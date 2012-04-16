package br.com.fiap.teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.thoughtworks.xstream.XStream;



public class Client {
	public static void main(String[] args) {
		try {
//			 Instancio o socket no IP e na porta específicos
			Socket socket = null;
			
			while(true) {
				//socket = new Socket("10.1.15.10",7000);
				//socket = new Socket("192.168.1.33",7000);
				socket = new Socket("127.0.0.1",7000);

				// System.in retorna InputStream
				InputStream is = System.in;

				// O InputStream pode ser decorado com InputStreamReader
				InputStreamReader isr = new InputStreamReader(is);

				// que por sua vez é decorado com BufferedReader
				BufferedReader stdin = new BufferedReader(isr);
				
				// pede ao usuário que digite a entrada
				System.out.print("Digite e pressione <Enter>:");			
				String linha = stdin.readLine();
				
				//Teste XML
				XMLRequest ida = new XMLRequest();
				Mensagem mensagem = new Mensagem();
				mensagem.setMensagem(linha);
				
				ida.setMensagem(mensagem);
				
				XStream xt = new XStream();
				xt.alias("ida", XMLRequest.class);
				String xml = xt.toXML(ida);
				
				System.out.println("XML enviado:");
				System.out.println(xml);
				
				//Fim testes
				
//			 	Instancio a saída dos dados
				PrintStream ps1 = new PrintStream(socket.getOutputStream());
	
//			 	Imprime uma linha para a stream de saída de dados
				ps1.println(xml);
				
				
//			 	fecho o socket
				socket.close();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

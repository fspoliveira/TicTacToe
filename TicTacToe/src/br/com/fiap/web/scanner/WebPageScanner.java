package br.com.fiap.web.scanner;
import java.net.URL;
   import java.net.URLConnection;
   import java.io.IOException;
   import java.util.Scanner;

   public class WebPageScanner {
     public static void main(String[] args) {
       try {
         URLConnection connection =
           new URL("http://www.terra.com.br").openConnection();
         String text = new Scanner(
           connection.getInputStream()).
           useDelimiter("\\Z").next();
         
         //z ou Z muda o parametro
         // useDelimiter("\\z").next();
         
         System.out.println(text);
       } catch (IOException e) {
         e.printStackTrace();
       }
     }
   } 
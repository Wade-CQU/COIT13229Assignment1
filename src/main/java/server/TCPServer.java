/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Wade
 */
import java.net.*;
import java.io.*;

public class TCPServer {
	public static void main (String args[]) {
		
		try{
			int serverPort = 8888; 
			ServerSocket listenSocket = new ServerSocket(serverPort);
			
			while(true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}

		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
}

class Connection extends Thread {
	
	ObjectInputStream in;
	ObjectOutputStream out;
	Socket clientSocket;
	
	public Connection (Socket aClientSocket) {
		
		try {
			clientSocket = aClientSocket;
			in = new ObjectInputStream( clientSocket.getInputStream());
			out =new ObjectOutputStream( clientSocket.getOutputStream());
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}

        

	public void run(){
		
		try {			                 
                    Drone drone = (Drone)in.readObject();	
                    //add logic to figure out what message is being recieved from drones
			


		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		} catch(IOException e) {System.out.println("readline:"+e.getMessage());
		} catch(ClassNotFoundException ex){
					 ex.printStackTrace();
		}finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
		

	}
}

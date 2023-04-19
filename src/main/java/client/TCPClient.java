/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import server.Drone;

/**
 *
 * @author Wade
 */
public class TCPClient {
	public static void main (String args[]) {
		
                //initialize Socket object as 's'
		Socket s = null;
		
		try{
			//set port as serverPort variable and create Socket object using that port
			int serverPort = 8888;
			s = new Socket("localhost", serverPort);   
                        
                        //initialize ObjectInputStream and ObjectOutputStream objects
			ObjectInputStream in = null;
			ObjectOutputStream out =null;
			
                        //create Output Stream using the Socket 's' specific output stream - allows data to be sent through the specified port
			out = new ObjectOutputStream(s.getOutputStream());
                        //create Input Stream using the Socket 's' specific input stream - allows data to be recieved through the specified port
			in = new ObjectInputStream(s.getInputStream());
                        
                        //initialize drone object and ask user for id, name, x and y
                        Drone drone = new Drone();
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Enter Drone ID: ");
                        drone.setDroneID(sc.nextInt());
                        System.out.println("Enter Drone Name: ");
                        drone.setDroneName(sc.nextLine());
                        System.out.println("Enter Drone X starting position: ");
                        drone.position.setX(sc.nextInt());
                        System.out.println("Enter Drone y starting position: ");
                        drone.position.setY(sc.nextInt());
                        
                        //send server the drone object created/updated by the user
                        out.writeObject(drone);
                        

                //catch exceptions
		}catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}
                finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
     }
}

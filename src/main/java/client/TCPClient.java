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
import java.util.Random;
import java.util.Scanner;
import server.Drone;
import server.FireDetection;
import server.Position;

/**
 *
 * @author Wade
 */
public class TCPClient {
	public static void main (String args[]) {
		
                //initialize Socket object as 's'
		Socket s = null;
                
		
		try{
                        //Create random number generator object
                        Random numberGenerator = new Random();
                        
			//set port as serverPort variable and create Socket object using that port
			int serverPort = 8887;
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
                        FireDetection fireDetection = new FireDetection();
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Enter Drone ID: ");
                        drone.setDroneID(sc.nextInt());
                        sc.nextLine();
                        System.out.println("Enter Drone Name: ");
                        drone.setDroneName(sc.nextLine());
                        System.out.println("Enter Drone X starting position: ");
                        drone.position.setX(sc.nextInt());
                        System.out.println("Enter Drone y starting position: ");
                        drone.position.setY(sc.nextInt());
                        
                        //send server the drone object created/updated by the user
                        out.writeObject(drone);
                        
                        while (true){
                            try{
                                //Delay process by 10 seconds
                                Thread.sleep(10000);
                                
                                //set the max distance a drone can move in 10 seconds
                                int numGeneratorBound = 20;
                                /* used to subtract the random number generation to allow the possibility of the drone x/y movment to be less than current value
                                this prevents the drone from only moving up and to the right of the map */
                                int numOffset = 10;
                                //generate new X position that is +/- 0-10 of last x position
                                int randomX = drone.position.getX() - numOffset + numberGenerator.nextInt(numGeneratorBound);
                                //generate new Y position that is +/- 0-10 of last y position
                                int randomY = drone.position.getY() - numOffset + numberGenerator.nextInt(numGeneratorBound);
                                
                                //set drone's new randomly generated x and y positions
                                drone.position.setX(randomX);
                                drone.position.setY(randomY);
                                
                                System.out.println("Drones x&y: " + drone.position.getX() + " " + drone.position.getY());
                                //Send current drone's position to server
                                Position newPosition = drone.getPosition();
                                out.writeObject(newPosition);
                                
                                //generate random number between 0-20
                                int fireProbabilty = numberGenerator.nextInt(numGeneratorBound);
                                int fireSeverityBound = 10;
                                
                                //if number is less than 5, create fire detection object and send to server.
                                if(fireProbabilty < 5){
                                    System.out.println("Fireobject detected and sent to server");
                                    fireDetection.setFirePositionx(drone.position.getX());
                                    fireDetection.setFirePositiony(drone.position.getY());
                                    //randomly generate a fire serverity level between 0-10
                                    fireDetection.setFireSeverity(numberGenerator.nextInt(fireSeverityBound));
                                    //send fire detection object to server
                                    out.writeObject(fireDetection);
                                }
                            }
                            catch (InterruptedException ie){
                                ie.printStackTrace();
                            }
                        }
                        
                        
                        

                //catch exceptions
		}catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}
                finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
     }
}

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
import java.util.LinkedList;

public class TCPServer {

    public static void main(String args[]) {

        try {
            //set port as serverPort variable and create ServerSocket object using that port
            int serverPort = 8887;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            //Listen for input data on the port defined above
            while (true) {
                Socket clientSocket = listenSocket.accept();
                //create new connection object which extends thread - create a new thread for each connection
                Connection c = new Connection(clientSocket);
            }

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}

//connection class which extends thread to create a new thread for each connection
class Connection extends Thread {

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket clientSocket;
    private LinkedList<Drone> droneList;
    private LinkedList<FireDetection> fireList;

    //connection class constructor
    public Connection(Socket aClientSocket) {

        try {
            clientSocket = aClientSocket;
            //create input/output stream with the client socket passed into the constructor
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            droneList = new LinkedList<Drone>();
            fireList = new LinkedList<FireDetection>();
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        
        loadSavedDrones();
        Drone thisDrone = new Drone();

        try {
            while (true) {
                Object droneMessage = (Object) in.readObject();

                if (droneMessage instanceof Drone) {
                    //collect variables from droneMessage drone object sent
                    int droneID = ((Drone) droneMessage).getDroneID();
                    String droneName = ((Drone) droneMessage).getDroneName();
                    int dronePositionX = ((Drone) droneMessage).getPosition().getX();
                    int dronePositionY = ((Drone) droneMessage).getPosition().getY();
                    //set the variables to a drone object
                    thisDrone.setDroneID(droneID);
                    thisDrone.setDroneName(droneName);
                    thisDrone.position.setX(dronePositionX);
                    thisDrone.position.setY(dronePositionY);

                    //check if drone with the same id exists, if it does update it with new drone object passed from client
                    for (Drone drone : droneList) {
                        if (drone.getDroneID() == thisDrone.getDroneID()) {
                            droneList.set(droneList.indexOf(drone), thisDrone);
                        }
                    }
                    //if the droneList does not already contain the drone passed by the client, add it into the list.
                    if (!droneList.contains(thisDrone)) {
                        droneList.add(thisDrone);
                    }
                    //test - print drone details
                    System.out.println("Drone id: " + thisDrone.droneID);
                    System.out.println("Drone name: " + thisDrone.droneName);
                    System.out.println("Drone position x: " + thisDrone.position.getX());
                    System.out.println("Drone position y: " + thisDrone.position.getY());
                    //Test - see if it is updating drones and not just adding new objects everytime
                    System.out.println("Number of drones in List: " + droneList.size());
                    System.out.println("\n");
                }

                if (droneMessage instanceof Position) {

                    //collect variable from the droneMessage Position object
                    int dronePositionX = ((Position) droneMessage).getX();
                    int dronePositionY = ((Position) droneMessage).getY();
                    //set thisDrone's position using variables collected
                    thisDrone.position.setX(dronePositionX);
                    thisDrone.position.setY(dronePositionY);
                    //test functionality
                    System.out.println("Using object get method:");
                    System.out.println("New position X: " + thisDrone.position.getX());
                    System.out.println("New position Y: " + thisDrone.position.getY());
                    System.out.println("\n");
                    System.out.println("Using int variables:");
                    System.out.println("New position X: " + dronePositionX);
                    System.out.println("New position Y: " + dronePositionY);
                }

                if (droneMessage instanceof FireDetection) {
                    //collect fireDetection variables through droneMessage
                    int fireX = ((FireDetection) droneMessage).getFirePositionx();
                    int fireY = ((FireDetection) droneMessage).getFirePositiony();
                    int fireSeverity = ((FireDetection) droneMessage).getFireSeverity();
                    //create new ForeDetection object and assign the variables collected
                    FireDetection fire = new FireDetection();
                    fire.setFirePositionx(fireX);
                    fire.setFirePositiony(fireY);
                    fire.setFireSeverity(fireSeverity);
                    fireList.add((FireDetection) droneMessage);
                    //test functionbality
                    System.out.println("Fire Added To Active Fire List");
                    System.out.println("Fire coordinates: " + fire.getFirePositionx() + ", " + fire.getFirePositiony());
                    System.out.println("Fire Severity: " + fire.getFireSeverity());
                }
            }

            //add logic to figure out what message is being recieved from drones
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {/*close failed*/
            }
        }

    }

    public void saveDrones() {

        String filename = "drones";
        FileOutputStream fos = null;
        ObjectOutputStream out = null;

        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            for (Drone drone : droneList) {
                out.writeObject(drone);
            }
            out.close();
            System.out.println("Drones successfully saved to hard drive.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void loadSavedDrones(){
           String filename = "drones";
           Boolean hasNextObject = true;

	   FileInputStream fis = null;
	   ObjectInputStream in = null;
	   try
	   {
	     fis = new FileInputStream(filename);
	     in = new ObjectInputStream(fis);
                while(hasNextObject = true){
                    try{
                        droneList.add((Drone)in.readObject());
                    } catch (EOFException e){
                        hasNextObject = false;
                    }  
                }
            in.close();
	   }
	   
	   catch(IOException | ClassNotFoundException ex) {
	     ex.printStackTrace();
	   }
    }
}

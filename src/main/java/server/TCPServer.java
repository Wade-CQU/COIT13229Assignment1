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
import static server.TCPServer.gui;

public class TCPServer {

    //declaring global variables so they can be referenced in main and in connection class
    public static GUI gui;
    public static LinkedList<Drone> droneList = new LinkedList<Drone>();
    public static LinkedList<FireDetection> fireList = new LinkedList<FireDetection>();

    public static void main(String args[]) {

        gui = new GUI();

        try {
            //set port as serverPort variable and create ServerSocket object using that port
            int serverPort = 8888;
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

    //connection class constructor
    public Connection(Socket aClientSocket) {

        try {
            clientSocket = aClientSocket;
            //create input/output stream with the client socket passed into the constructor
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {

        //create drone object for current thread
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
                    for (Drone drone : TCPServer.droneList) {
                        if (drone.getDroneID() == thisDrone.getDroneID()) {
                            TCPServer.droneList.set(TCPServer.droneList.indexOf(drone), thisDrone);
                        }
                    }
                    //if the droneList does not already contain the drone passed by the client, add it into the list.
                    if (!TCPServer.droneList.contains(thisDrone)) {
                        TCPServer.droneList.add(thisDrone);
                    }
                    
                    //save drones into hard disk
                    saveDrones();
                    //redraw map on gui
                    gui.paintMap(TCPServer.droneList);

                }

                if (droneMessage instanceof Position) {

                    //collect variable from the droneMessage Position object
                    int dronePositionX = ((Position) droneMessage).getX();
                    int dronePositionY = ((Position) droneMessage).getY();

                    //check if drone is out of bouds with new position
                    if (0 > dronePositionX && dronePositionX > 400) {
                        out.writeObject(thisDrone.position);
                        System.out.println("Drone's X position is out of bounds");
                    } else if (0 > dronePositionY && dronePositionY > 400) {
                        out.writeObject(thisDrone.position);
                        System.out.println("Drone's Y position is out of bounds");
                    } else {
                        //set thisDrone's position using variables collected
                        thisDrone.position.setX(dronePositionX);
                        thisDrone.position.setY(dronePositionY);
                    }

                    //save drones into hard disk
                    saveDrones();
                    //redraw map
                    gui.paintMap(TCPServer.droneList);

                }

                if (droneMessage instanceof FireDetection) {
                    //create new ForeDetection object and assign the variables from object
                    FireDetection fire = new FireDetection();
                    fire.setFireID(TCPServer.fireList.size());
                    fire.setFirePositionx(((FireDetection) droneMessage).getFirePositionx());
                    fire.setFirePositiony(((FireDetection) droneMessage).getFirePositiony());
                    fire.setFireSeverity(((FireDetection) droneMessage).getFireSeverity());
                    fire.setReportingDroneID(((FireDetection) droneMessage).reportingDroneID);
                    //add to linked list
                    TCPServer.fireList.add(fire);
                    //run consoleFireReport method passing in this fire object
                    TCPServer.gui.consoleFireReport(fire);
                }
            }

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

    //method called to save drones into hard disk
    public void saveDrones() {
        String filename = "drones";
        FileOutputStream fos = null;
        ObjectOutputStream out = null;

        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            for (Drone drone : TCPServer.droneList) {
                out.writeObject(drone);
            }
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //method called to load drones from hard disk
    public void loadSavedDrones() {
        String filename = "drones";
        Boolean hasNextObject = true;

        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            while (hasNextObject = true) {
                try {
                    TCPServer.droneList.add((Drone) in.readObject());
                } catch (EOFException e) {
                    hasNextObject = false;
                }
            }
            in.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}

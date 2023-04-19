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

    public static void main(String args[]) {

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

        try {
            Drone drone = (Drone) in.readObject();
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
}

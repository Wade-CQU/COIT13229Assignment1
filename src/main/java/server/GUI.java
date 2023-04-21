/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static server.TCPServer.fireList;

/**
 *
 * @author Wade
 */
public class GUI implements ActionListener{

    //instantiate java swing components for the GUI
    private static JLabel functionControlsLabel;
    private static JTextArea functionControlsTextArea;
    private static JLabel systemMessagesLabel;
    private static JTextArea systemMessagesTextArea;
    private static JPanel droneMap;
    //instantiate components for the map
    private static JTextField drone;
    private static JTextField fire;

    public GUI() {
        //create frame and panel
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(680, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        //set label and position
        functionControlsLabel = new JLabel("Function Controls");
        functionControlsLabel.setBounds(10, 20, 150, 25);
        panel.add(functionControlsLabel);

        //set text area for function controlls
        functionControlsTextArea = new JTextArea();
        functionControlsTextArea.setBounds(10, 65, 150, 150);
        panel.add(functionControlsTextArea);

        //set label and position
        systemMessagesLabel = new JLabel("System Messages");
        systemMessagesLabel.setBounds(10, 235, 150, 25);
        panel.add(systemMessagesLabel);

        //set text area for system output
        systemMessagesTextArea = new JTextArea();
        systemMessagesTextArea.setBounds(10, 280, 150, 150);
        systemMessagesTextArea.setLineWrap(true);
        systemMessagesTextArea.setWrapStyleWord(true);
        panel.add(systemMessagesTextArea);

        //create and position drone map
        droneMap = new JPanel();
        droneMap.setLayout(null);
        droneMap.setBounds(200, 65, 400, 400);
        droneMap.setBorder(BorderFactory.createLineBorder(Color.red));
        droneMap.setBackground(Color.white);
        panel.add(droneMap);

        //make frame visible
        frame.setVisible(true);
    }

    //function called to redraw the map with current drone and fire locations
    public void paintMap(LinkedList<Drone> droneList) {
        //clear the drone map
        droneMap.removeAll();
        //redraw all the drones
        for (Drone droneObj : droneList) {
            drone = new JTextField(droneObj.getDroneName());
            drone.setBackground(Color.blue);
            droneMap.add(drone);
            drone.setBounds(droneObj.position.getX(), droneObj.position.getY(), 50, 25);
        }
        //redraw fire images on the map
        for (FireDetection fireObj : fireList) {
            fire = new JTextField(fireObj.getFireID());
            fire.setBackground(Color.red);
            droneMap.add(fire);
            fire.setBounds(fireObj.getFirePositionx(), fireObj.getFirePositiony(), 55, 40);
        }
        //prevent the drone showing in both the last position and the new position
        droneMap.revalidate();
        droneMap.repaint();
    }

    //print fire information into console text area
    public void consoleFireReport(FireDetection fire) {
        systemMessagesTextArea.setText("Fire reported by drone " + fire.getReportingDroneID() + ", severity level : "
                + fire.getFireSeverity() + ". Fire ID number: " + fire.getFireID());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userInput = functionControlsTextArea.getText();
        switch(userInput) {
            case "1":
                TCPServer.fireList.clear();
                systemMessagesTextArea.setText("All fires removed");
                break;
            case "2":
                System.exit(1);
            default:
               systemMessagesTextArea.setText("Unknown command"); 
            
        }
    }
    
}

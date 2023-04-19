/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author Wade
 */
public class Drone implements Serializable {
    public int droneID;
    public String droneName;
    public Position position;
    public FireDetection fireDetection;
    

    public Drone() {
    }

    public Drone(int droneID, String droneName, Position position, FireDetection fireDetection) {
        this.droneID = droneID;
        this.droneName = droneName;
        this.position = position;
        this.fireDetection = fireDetection;
    }

    public int getDroneID() {
        return droneID;
    }

    public void setDroneID(int droneID) {
        this.droneID = droneID;
    }

    public String getDroneName() {
        return droneName;
    }

    public void setDroneName(String droneName) {
        this.droneName = droneName;
    }

    

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public FireDetection getFireDetection() {
        return fireDetection;
    }

    public void setFireDetection(FireDetection fireDetection) {
        this.fireDetection = fireDetection;
    }
    
    
}


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
public class FireDetection implements Serializable {
    //parameters
    public int fireID;
    public int FirePositionx;
    public int FirePositiony;
    public int reportingDroneID;
    public int fireSeverity;

    //constructors
    public FireDetection() {
    }

    public FireDetection(int fireID, int FirePositionx, int FirePositiony, int reportingDroneID, int fireSeverity) {
        this.fireID = fireID;
        this.FirePositionx = FirePositionx;
        this.FirePositiony = FirePositiony;
        this.reportingDroneID = reportingDroneID;
        this.fireSeverity = fireSeverity;
    }

    //setters and getters
    public int getFireID() {
        return fireID;
    }

    public void setFireID(int fireID) {
        this.fireID = fireID;
    }

    public int getFirePositionx() {
        return FirePositionx;
    }

    public void setFirePositionx(int FirePositionx) {
        this.FirePositionx = FirePositionx;
    }

    public int getFirePositiony() {
        return FirePositiony;
    }

    public void setFirePositiony(int FirePositiony) {
        this.FirePositiony = FirePositiony;
    }

    public int getReportingDroneID() {
        return reportingDroneID;
    }

    public void setReportingDroneID(int reportingDroneID) {
        this.reportingDroneID = reportingDroneID;
    }

    public int getFireSeverity() {
        return fireSeverity;
    }

    public void setFireSeverity(int fireSeverity) {
        this.fireSeverity = fireSeverity;
    }
    
}

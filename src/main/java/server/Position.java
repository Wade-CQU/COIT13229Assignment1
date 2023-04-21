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
public class Position implements Serializable{
    //parameters
    private int x;
    private int y;

    //constructors
    public Position(){
    }
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //setters and getters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    
}

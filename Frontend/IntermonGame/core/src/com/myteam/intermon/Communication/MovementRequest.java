package com.myteam.intermon.Communication;

public class MovementRequest
{
    private int xL, xR, yU, yL, oldPlayerX, oldPlayerY, newPlayerX, newPlayerY;
    private String requestType;
    
    public MovementRequest(int xL, int xR, int yU, int yL, int oldPlayerX, int oldPlayerY, int newPlayerX, int newPlayerY, String requestType)
    {
        this.xL = xL;
        this.xR = xR;
        this.yU = yU;
        this.yL = yL;
        this.oldPlayerX = oldPlayerX;
        this.oldPlayerY = oldPlayerY;
        this.newPlayerX = newPlayerX;
        this.newPlayerY = newPlayerY;
        this.requestType = requestType;
    }
}

package message;

import java.io.File;
import java.io.Serializable;

/**
 * used to send serialized data from home to interlayerServer
 */
public class Message implements Serializable
{
    //some data and get-methods
    private int lightLevel;
    private float temperature;
    private float humidity;
    private byte armStatus; // change to enum?
    private boolean motionDetected;
    private boolean relayStatus;
    private byte [] image;
    //private File photo; really needs?

    public int getLightLevel()
    {
        return lightLevel;
    }

    public float getTemperature()
    {
        return temperature;
    }

    public byte getArmStatus()
    {
        return armStatus;
    }

    public float getHumidity()
    {
        return humidity;
    }

    public boolean getMotionDetected()
    {
        return motionDetected;
    }

    public boolean getRelayStatus()
    {
        return relayStatus;
    }

    public byte [] getImage()
    {
        return image;
    }

    public Message() //just to test connection, needs to be rewrited
    {
        temperature = 20;
        armStatus = 1;
    }

    public Message(int lightLevel, float tempetarure, float humidity, boolean motion, boolean relayStatus)
    {
        this.lightLevel = lightLevel;
        this.temperature=tempetarure;
        this.humidity = humidity;
        this.motionDetected = motion;
        this.relayStatus = relayStatus;
    }

    public Message(int lightLevel, float tempetarure, float humidity, boolean motion, boolean relayStatus, byte [] image)
    {
        this.lightLevel = lightLevel;
        this.temperature=tempetarure;
        this.humidity = humidity;
        this.motionDetected = motion;
        this.relayStatus = relayStatus;
        this.image = image;
    }

}
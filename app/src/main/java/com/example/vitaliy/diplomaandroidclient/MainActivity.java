package com.example.vitaliy.diplomaandroidclient;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.PrintWriter;
import java.net.Socket;

import message.Message;

public class MainActivity extends AppCompatActivity
{

    int port = 1235;
    String ip = "192.168.10.101"; //change it to ip of interlayer server

    String sendMessage;
    EditText input;
    TextView temperatureTextView;

    Socket socket;
    PrintWriter writer;
    ObjectInputStream reader;

    Message message;

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText)findViewById(R.id.input);
        temperatureTextView = (TextView)findViewById(R.id.temperature);
        h = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                Log.d("DEBUGGG", "handled");
                temperatureTextView.setText(Integer.toString(msg.what));
            }
        };

        initSocket();
        messageListener();
    }

    void initSocket()
    {
        new Thread (new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    socket = new Socket(ip, port);
                    writer = new PrintWriter(socket.getOutputStream());
                    reader = new ObjectInputStream(socket.getInputStream());

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onSendClick(View view)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                sendMessage = input.getText().toString();
                writer.write(sendMessage);
                writer.close();
            }
        }).start();
        initSocket();
    }

    public void messageListener()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    if(reader!=null)
                    {
                        try
                        {
                            message = (Message) reader.readObject();
                            //temperatureTextView.setText((int) message.getTemperature());
                            Log.d("DEBUGGG", "recieved temp="+message.getTemperature());
                            h.sendEmptyMessage((int)message.getTemperature());
                            //h.sendEmptyMessage(Integer.parseInt(Float.toString(message.getTemperature())));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (OptionalDataException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}

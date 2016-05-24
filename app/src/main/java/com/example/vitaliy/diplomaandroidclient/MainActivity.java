package com.example.vitaliy.diplomaandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity
{

    int port = 1235;
    String ip = "192.168.10.101"; //change it to ip of interlayer server

    String sendMessage;
    EditText input;

    Socket socket;
    PrintWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText)findViewById(R.id.input);

        initSocket();
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
}

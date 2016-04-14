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
    EditText input = (EditText)findViewById(R.id.input);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSendClick(View view)
    {
        Thread sendThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Socket socket = new Socket(ip, port);
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    sendMessage = input.getText().toString();
                    writer.write(sendMessage);
                    writer.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        sendThread.run();
    }
}

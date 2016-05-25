package com.example.vitaliy.diplomaandroidclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    TextView humidityTextView;
    TextView relayStateTextView;
    TextView motionView;
    TextView lightLevelTextView;
    ImageView imageView;

    Socket socket;
    PrintWriter writer;
    ObjectInputStream reader;

    FileOutputStream outputStream;
    //ByteArrayInputStream

    Message message;
    File img;
    byte [] binariedImage;
    ByteArrayInputStream imageInput;
    Drawable image;
    Bitmap bitmap;

    Handler handlerTemperature;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText)findViewById(R.id.input);
        temperatureTextView = (TextView)findViewById(R.id.dataTemperature);
        humidityTextView = (TextView)findViewById(R.id.dataHumidity);
        relayStateTextView = (TextView)findViewById(R.id.dataRelayState);
        motionView = (TextView)findViewById(R.id.dataMotionDetect);
        lightLevelTextView = (TextView)findViewById(R.id.dataLightLevel);
        imageView = (ImageView)findViewById(R.id.imageView);
        //imageView.setImageBitmap( imageUtil.getImageBitmap() );


        /*handlerTemperature = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                Log.d("DEBUGGG", "handled");
                temperatureTextView.setText(Integer.toString(msg.what));
                //ImageReader r = new ImageReader();

                imageView.setImageBitmap(bitmap);
            }
        };*/ //become useless?

        handler = new Handler()
        {
            @Override
            public void handleMessage(android.os.Message msg)
            {
                //super.handleMessage(msg);
                Message m = (Message)msg.obj;
                temperatureTextView.setText(Float.toString(m.getTemperature()));
                humidityTextView.setText(Float.toString(m.getHumidity()));
                if(m.getRelayStatus()==true)
                {
                    relayStateTextView.setText("on");
                }
                else
                {
                    relayStateTextView.setText("off");
                }
                motionView.setText(Boolean.toString(m.getMotionDetected()));
                lightLevelTextView.setText(Integer.toString(m.getLightLevel()));
                imageView.setImageBitmap(bitmap);
            }
        };

        //imageView.setImageResource(R.drawable.noimage);

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
                            Log.d("DEBUGGG", "recieved temp=" + message.getTemperature());

                            binariedImage = message.getImage();
                            //imageInput = new ByteArrayInputStream(binariedImage);
                            Log.d("DEBUGGG", "img size "+binariedImage.length);
                            bitmap = BitmapFactory.decodeByteArray(binariedImage, 0, binariedImage.length);
                            Log.d("DEBUGGG", "recieved image and not crashed");

                            //handlerTemperature.sendEmptyMessage((int) message.getTemperature());
                            android.os.Message msg = new android.os.Message();
                            msg.obj = message;
                            handler.sendMessage(msg);
                            //outputStream = openFileOutput("cameraimage.png", Context.MODE_PRIVATE);
                            //outputStream.close();

                            //Log.d("DEBUGGG", "writed image and not crashed");

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

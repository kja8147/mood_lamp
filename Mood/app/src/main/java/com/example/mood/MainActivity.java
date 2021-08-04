package com.example.mood;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;

    NumberPicker picker1,picker2;
    ImageButton sendtime;
    ImageView imageView;
    View mView;
    ImageButton btnSend;
    ImageButton btnvoice;

    String red;
    String green;
    String blue;
    String rgb_result;

    Switch back,bluetooth;

    int pixel;

    Bitmap bitmap;
    ConstraintLayout layback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();



        // Tab1 Setting

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab1");
        tabSpec1.setIndicator("color"); // Tab Subject
        tabSpec1.setContent(R.id.tab1); // Tab Content
        tabHost.addTab(tabSpec1);

        // Tab2 Setting

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab2");
        tabSpec2.setIndicator("timer"); // Tab Subject
        tabSpec2.setContent(R.id.tab2); // Tab Content
        tabHost.addTab(tabSpec2);

        // Tab3 Setting

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("Tab3");
        tabSpec3.setIndicator("setting"); // Tab Subject
        tabSpec3.setContent(R.id.tab3); // Tab Content
        tabHost.addTab(tabSpec3);

        // show First Tab Content

        tabHost.setCurrentTab(0);

        btnvoice = (ImageButton) findViewById(R.id.btnvoice);//보이스 버튼
        back= (Switch) findViewById(R.id.switch1);
        layback=(ConstraintLayout)findViewById(R.id.layback);
        bluetooth=(Switch)findViewById(R.id.switch2);

        bt = new BluetoothSPP(this); //Initializing


//****************************보이스 *******************************************
        btnvoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
                try{
                    startActivityForResult(intent,200);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"intent problem",Toast.LENGTH_SHORT).show();
                }
            }
        });

//*******************************************************************************

//********************** 타이머 **********************************************************

        picker1 = (NumberPicker)findViewById(R.id.numberPicker);

        picker1.setMinValue(0);
        picker1.setMaxValue(24);

         picker2 = (NumberPicker)findViewById(R.id.numberPicker2);

        picker2.setMinValue(0);
        picker2.setMaxValue(59);

        sendtime=(ImageButton)findViewById(R.id.button2);

        sendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetooth.isChecked()==false){
                    Toast.makeText(getApplicationContext(),"블루투스를 연결해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    bt.send(String.valueOf(picker1.getValue() * 60 + picker2.getValue()), true);
                    Toast.makeText(getApplicationContext(), "시간이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


//*************************************************************************

//***************************** bluetooth ***********************************

        bluetooth.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v){

                if (bluetooth.isChecked()) {
                    ischeck(true);
                } else {
                    ischeck(false);


                }


            }
        });

/*
        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가능 하면,
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

*/




//******************************** color pick **********************************

        imageView=(ImageView)findViewById(R.id.imageView);
        mView=(View)findViewById(R.id.colorView);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);

        //imageview on touch listener
        imageView.setOnTouchListener(new View.OnTouchListener(){//이미지 터치했을때
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                            bitmap = imageView.getDrawingCache();

                            pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());

                            //getting rgb values
                            int r = Color.red(pixel);
                            int g = Color.green(pixel);
                            int b = Color.blue(pixel);

                            if (r >= 0 && r <= 9) red = "00" + String.valueOf(r);

                            if (r >= 10 && r <= 99) red = "0" + String.valueOf(r);

                            if (r >= 100) red = String.valueOf(r);

                            if (g >= 0 && g <= 9) green = "00" + String.valueOf(g);

                            if (g >= 10 && g <= 99) green = "0" + String.valueOf(g);

                            if (g >= 100) green = String.valueOf(g);

                            if (b >= 0 && b <= 9) blue = "00" + String.valueOf(b);

                            if (b >= 10 && b <= 99) blue = "0" + String.valueOf(b);

                            if (b >= 100) blue = String.valueOf(b);


                            rgb_result = red + green + blue;

                            //set background color
                            mView.setBackgroundColor(Color.rgb(r, g, b));




                }
                return true;
            }


        });


        //**********************************************************************
//********************************테마 스위치*********************************

    back.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(back.isChecked()){
                layback.setBackgroundResource(R.drawable.paper2_dark);
            }else{
                layback.setBackgroundResource(R.drawable.paper2_light);
            }
        }
    });

//*****************************************************************************
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setups();
            }
        }
    }

    public void setups() {
        btnSend = (ImageButton) findViewById(R.id.btnsend); //데이터 전송

        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bluetooth.isChecked()==true) {
                    bt.send(rgb_result, true);
                    Toast.makeText(getApplicationContext(), "색상이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"블루투스를 연결해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int  resultCode, Intent data) {
        //*********************블루투스*************************************8
        /*
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setups();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
*/
        //*********************************보이스*******************************

        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==200){
            if(resultCode==RESULT_OK && data !=null ){

                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                    if(bluetooth.isChecked()==true) {

                            bt.send(result.get(0), true);
                            Toast.makeText(getApplicationContext(), "켜줘와 꺼줘만 지원합니다.( " + result.get(0)+" )", Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getApplicationContext(),"블루투스를 연결해주세요", Toast.LENGTH_SHORT).show();
                    }

                //bt.send(result.get(0),true);
               // Toast.makeText(getApplicationContext(),result.get(0),Toast.LENGTH_SHORT).show();


            }
        }

    }


public void ischeck(boolean b){

    if(b==true){
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);

    }else if(b==false){
        bt.disconnect();
    }



}


}





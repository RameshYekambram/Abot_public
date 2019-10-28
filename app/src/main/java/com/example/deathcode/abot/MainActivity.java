package com.example.deathcode.abot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deathcode.abot.adapter.MessageAdapter;
import com.example.deathcode.abot.model.ResponseMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;
    static TextView data;
    Button userInput;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<ResponseMessage> responseMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        userInput.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.RECORD_AUDIO)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                        //MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    speechRecog.startListening(intent);
                }

                /*ResponseMessage responseMessage = new ResponseMessage(userInput.getText().toString(), true);
                responseMessageList.add(responseMessage);
                ResponseMessage responseMessage2 = new ResponseMessage(userInput.getText().toString(), false);
                responseMessageList.add(responseMessage2);
                messageAdapter.notifyDataSetChanged();
                if (!isLastVisible())
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);*/
            }
        } );

        initializeTextToSpeech();
        initializeSpeechRecognizer();
        // ATTENTION: This was auto-generated to handle app links.

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

    }

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }
    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                    processResult(result_arr.get(0));

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });

        }
    }

    private void processResult(String result_message)   {
        result_message = result_message.toLowerCase();
        ResponseMessage responseMessage2 = new ResponseMessage(result_message , false);
        responseMessageList.add(responseMessage2);
        messageAdapter.notifyDataSetChanged();
        if (!isLastVisible())
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
// 
        String res = "empty";
        if(result_message.indexOf("what") != -1){
            if(result_message.indexOf("your name") != -1){
               // data.setText("My Name is Mr.Android. Nice to meet you!");
                speak("My Name is Mr.Android. Nice to meet you!");
                res = "My Name is Mr.Android. Nice to meet you!";
                
            }
            if (result_message.indexOf("time") != -1){
                String time_now = DateUtils.formatDateTime(this, new Date().getTime(),DateUtils.FORMAT_SHOW_TIME);
               // data.setText("The time is now: " + time_now);
                speak("The time is now: " + time_now);
                res = "The time is now: " + time_now;
            }
        } else if (result_message.indexOf("hi") != -1){
            //data.setText("Hello. Nice to meet you!");
            speak("hello. Nice to meet you!");
             res = "hello. Nice to meet you!";
           
        }
        ResponseMessage responseMessage = new ResponseMessage(res, true);
        responseMessageList.add(responseMessage);
        messageAdapter.notifyDataSetChanged();
        if (!isLastVisible())
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

    }

    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(MainActivity.this, getString(R.string.tts_no_engines),Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage( Locale.US);

                    speak("Hello Sir,i am ikyams voice assistant. How may i help you?");
                    //data.setText("Hello Sir,i am ikyams voice assistant. How may i help you? ");
                    String Text ="Hello Sir,i am ikyams voice assistant. How may i help you? " ;
                    ResponseMessage responseMessage = new ResponseMessage(Text, true);
                    responseMessageList.add(responseMessage);
                    messageAdapter.notifyDataSetChanged();
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                }
            }
        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        tts.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializeSpeechRecognizer();
        initializeTextToSpeech();
    }
}

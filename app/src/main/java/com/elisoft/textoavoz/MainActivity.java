package com.elisoft.textoavoz;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    Button bt_voz,bt_grabar;
    EditText et_escribir;
    TextToSpeech tv_voz;
    TextView tv_texto;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_voz=new TextToSpeech(this, this);
        et_escribir=(EditText)findViewById(R.id.et_escribir);
        bt_voz=(Button)findViewById(R.id.bt_voz);
        bt_grabar=(Button)findViewById(R.id.bt_grabar);
        tv_texto=(TextView)findViewById(R.id.tv_texto);

        bt_voz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speatOut();
            }
        });

        bt_grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar_ahora();
            }
        });
    }

    private void speatOut() {
        String texto=et_escribir.getText().toString();
        tv_voz.speak(texto,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS){
            int resultado=tv_voz.setLanguage(Locale.getDefault());
            if(resultado==TextToSpeech.LANG_NOT_SUPPORTED || resultado==TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS","Lenguaje no soportado");
            }else{
                bt_voz.setEnabled(true);
                speatOut();
            }
        }else{
            Log.e("TTS","Inicializacion de lenguaje es fallida.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.
                                    EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);

                    tv_texto.setText(strSpeech2Text);
                }

                break;
            default:

                break;
        }
    }

    public void hablar_ahora() {

        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }

    }
}

package eazy_dev.talk2me;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import static android.os.Build.ID;

public class MainActivity extends AppCompatActivity {

    private TextView _resultView;
    private TextToSpeech _toSpeech;
    private int _result;
    private final int REQUEST_CODE_SPEECH = 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView _talk2me = (ImageView) findViewById(R.id.talk2me);
        _resultView = (TextView) findViewById(R.id.resultView);

        _talk2me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                talk2me_func();

            }
        });

        _toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS)
                {
                    _result = _toSpeech.setLanguage(Locale.US);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Maaf, perangkat tidak support", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void talk2me_func()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hai Cantik");

        try
        {
            startActivityForResult(i, REQUEST_CODE_SPEECH);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(MainActivity.this, "Maaf, perangkat tidak support", Toast.LENGTH_LONG).show();
        }
    }

    private void bicara()
    {
        if (_result == TextToSpeech.LANG_MISSING_DATA || _result == TextToSpeech.LANG_NOT_SUPPORTED)
        {
            Toast.makeText(MainActivity.this, "Maaf, perangkat tidak support", Toast.LENGTH_LONG).show();
        }
        else
        {
            _toSpeech.speak(_resultView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH: {
                if (resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> inText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    _resultView.setText(inText.get(0));

                    bicara();
                }

                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (_toSpeech != null)
        {
            _toSpeech.stop();
            _toSpeech.shutdown();
        }
    }
}

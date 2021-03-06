package com.example.jasonchi.blinkiddemo;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.microblink.activity.ScanCard;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognitionResult;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognizerSettings;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.ocr.OcrResult;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button scan;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.linear_1);
        layout.removeAllViews();

        scan = (Button)findViewById(R.id.btn_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this, ScanCard.class);

                it.putExtra(ScanCard.EXTRAS_LICENSE_KEY, "VJ6VTCWU-ZGBQPGK5-PAGRDMO5-CJUE2PJB-BJHS4MIE-TCS34Z3O-H2FNRZ2B-LKBWPGFH");

                RecognitionSettings settings = new RecognitionSettings();

                settings.setRecognizerSettingsArray(setupSettingsArray());

                it.putExtra(ScanCard.EXTRAS_RECOGNITION_SETTINGS, settings);

                startActivityForResult(it, 10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10){
            if(resultCode == ScanCard.RESULT_OK && data != null){

                Bundle extras = data.getExtras();
                RecognitionResults result = data.getParcelableExtra(ScanCard.EXTRAS_RECOGNITION_RESULTS);

                BaseRecognitionResult[] resultArray = result.getRecognitionResults();

                for(BaseRecognitionResult baseResult : resultArray){
                    if(baseResult instanceof MRTDRecognitionResult){
                        MRTDRecognitionResult info = (MRTDRecognitionResult) baseResult;

                        if(info.isValid() && !info.isEmpty()){
                            ArrayList<String> informations = new ArrayList<>();
                            informations.add(info.getAlienNumber());
                            informations.add(info.getPrimaryId());
                            informations.add(info.getSecondaryId());
                            informations.add(info.getSex());
                            informations.add(info.getRawDateOfBirth());
                            informations.add(info.getDocumentType().toString());
                            informations.add(info.getDocumentNumber());
                            informations.add(info.getRawDateOfExpiry());
                            informations.add(info.getNationality());
                            informations.add(info.getImmigrantCaseNumber());
                            informations.add(info.getMRZText());

                            buildView(informations);
                        }else {
                            OcrResult rawOcr = info.getOcrResult();
                        }
                    }
                }

            }
        }
    }

    private RecognizerSettings[] setupSettingsArray() {
        MRTDRecognizerSettings sett = new MRTDRecognizerSettings();

        return new RecognizerSettings[] { sett };
    }

    public void buildView(ArrayList<String> infos){
        for(String info: infos){
            TextView showView = new TextView(this);
            showView.setText(info);
            layout.addView(showView);
        }
    }

}

package ro.pub.cs.systems.eim.colocviu1_245.service;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import ro.pub.cs.systems.eim.colocviu1_245.R;
import ro.pub.cs.systems.eim.colocviu1_245.general.Constants;

public class Colocviu1_245SecondaryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(Constants.ALL_TERMS)) {

            String allTerms = intent.getStringExtra(Constants.ALL_TERMS);
            int sum = 0;
            for (String term: allTerms.split(Pattern.quote(" + "))) {
                sum += Integer.valueOf(term);
            }
            setResult(sum, null);
        }

        finish();
    }
}

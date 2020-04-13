package ro.pub.cs.systems.eim.colocviu1_245.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ro.pub.cs.systems.eim.colocviu1_245.R;
import ro.pub.cs.systems.eim.colocviu1_245.general.Constants;
import ro.pub.cs.systems.eim.colocviu1_245.service.Colocviu1_245SecondaryActivity;
import ro.pub.cs.systems.eim.colocviu1_245.service.Colocviu1_245Service;

public class Colocviu1_245MainActivity extends AppCompatActivity {
    private EditText nextTermEditText;
    private EditText allTermsEditText;
    private Button addButton;
    private Button computeButton;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private int lastSumValue;
    private String lastAllTerms;

    private int serviceStatus;

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_245_main);

        nextTermEditText = (EditText)findViewById(R.id.next_term_editText);
        allTermsEditText = (EditText)findViewById(R.id.all_terms_editText);
        addButton = (Button)findViewById(R.id.add_button);
        computeButton = (Button)findViewById(R.id.compute_button);

        addButton.setOnClickListener(buttonClickListener);
        computeButton.setOnClickListener(buttonClickListener);

        if (savedInstanceState != null) {
            lastSumValue = savedInstanceState.getInt(Constants.SAVED_SUM);
            if (savedInstanceState.getString(Constants.SAVED_TERMS) != null) {
                lastAllTerms = savedInstanceState.getString(Constants.SAVED_TERMS);
                allTermsEditText.setText(savedInstanceState.getString(Constants.SAVED_TERMS));
            }
        }

        serviceStatus = Constants.SERVICE_STOPPED;
        intentFilter.addAction("test");
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, Colocviu1_245Service.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(Constants.SAVED_SUM, lastSumValue);
        savedInstanceState.putString(Constants.SAVED_TERMS, lastAllTerms);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastSumValue = savedInstanceState.getInt(Constants.SAVED_SUM);
        if (savedInstanceState.getString(Constants.SAVED_TERMS) != null) {
            lastAllTerms = savedInstanceState.getString(Constants.SAVED_TERMS);
            allTermsEditText.setText(savedInstanceState.getString(Constants.SAVED_TERMS));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Toast.makeText(this, "The sum is " + resultCode, Toast.LENGTH_LONG).show();
        lastSumValue = resultCode;

        if (lastSumValue > 10 && serviceStatus == Constants.SERVICE_STOPPED) {
            Intent intent1 = new Intent(getApplicationContext(), Colocviu1_245Service.class);
            intent1.putExtra(Constants.SAVED_SUM, lastSumValue);
            getApplicationContext().startService(intent1);
            serviceStatus = Constants.SERVICE_STARTED;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_button:
                    if (!nextTermEditText.getText().toString().isEmpty()) {
                        int value = Integer.valueOf(nextTermEditText.getText().toString());
                        if (allTermsEditText.getText().toString().isEmpty())
                            allTermsEditText.setText(String.valueOf(value));
                        else
                            allTermsEditText.setText(allTermsEditText.getText().toString() + " + " + String.valueOf(value));
                    }
                    break;
                case R.id.compute_button:
                    if (lastAllTerms != null && lastAllTerms.equals(allTermsEditText.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "The sum is " + lastSumValue, Toast.LENGTH_LONG).show();
                        break;
                    }

                    lastAllTerms = allTermsEditText.getText().toString();

                    Intent intent = new Intent(getApplicationContext(), Colocviu1_245SecondaryActivity.class);
                    intent.putExtra(Constants.ALL_TERMS, allTermsEditText.getText().toString());
                    startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }
        }
    }

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_TAG, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA));
        }
    }
}

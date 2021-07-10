package com.romainrbn.projseio_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Document;

public class QuizResultActivity extends AppCompatActivity {

    public static final String PREFS_KEY = "doctorEmail";
    public static final String HAS_REMEMBER_ACTIVATED = "rememberMe";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences shared = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_quiz_result);

        setTitle(R.string.examResultTitle);

        final EditText doctorEmailET = findViewById(R.id.doctorEmailEditText);
        EditText patientNameEditText = findViewById(R.id.patientNameEditText);
        TextView resultTV = findViewById(R.id.resultTV);
        final Button seePDFButton = findViewById(R.id.seePDFButton);
        Button exportPDFButton = findViewById(R.id.exportPDFButton);
        final Switch rememberMeSwitch = findViewById(R.id.rememberEmailSwitch);



        int score = getIntent().getIntExtra("score", 0);
        String scoreResult = Integer.toString(score);
        resultTV.setText("" + scoreResult);
        resultTV.setTextColor(determineColorFromScore(score));

        patientNameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        seePDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seePDF();
            }
        });

        exportPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportPDF();
            }
        });

        Boolean hasRememberActivated = shared.getBoolean(HAS_REMEMBER_ACTIVATED, false);
        rememberMeSwitch.setChecked(hasRememberActivated);

        String doctorEmail = shared.getString(PREFS_KEY, null);

        if(doctorEmail != null) {
            doctorEmailET.setText(doctorEmail);
        }

        doctorEmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                if(rememberMeSwitch.isChecked()) {
                    editor.putString(PREFS_KEY, doctorEmailET.getText().toString());
                    editor.apply();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(doctorEmailET.getText().toString().length() > 0) {
                    rememberMeSwitch.setEnabled(true);
                } else if (doctorEmailET.getText().toString().length() == 0) {
                    rememberMeSwitch.setEnabled(false);
                    rememberMeSwitch.setChecked(false);
                } else {
                    rememberMeSwitch.setEnabled(false);
                    rememberMeSwitch.setChecked(false);
                }
            }
        });

        rememberMeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                if(!b) {
                    editor.remove(PREFS_KEY);
                    editor.putBoolean(HAS_REMEMBER_ACTIVATED, false);
                    editor.apply();
                } else if (b && doctorEmailET.getText().toString().length() > 0) {
                    editor.putString(PREFS_KEY, doctorEmailET.getText().toString());
                    editor.putBoolean(HAS_REMEMBER_ACTIVATED, true);
                    editor.apply();
                }
            }
        });
    }

    int determineColorFromScore(int score) {
        if(score < 4) {
            return Color.parseColor("#2dad1c");
        } else if (score >= 4 && score < 8) {
            return Color.parseColor("#e3780e");
        } else if (score >= 8) {
            return Color.parseColor("#e30e0e");
        } else {
            return Color.parseColor("#828282");
        }
    }

    private void seePDF() {
        final EditText doctorEmailET = findViewById(R.id.doctorEmailEditText);
        EditText patientNameEditText = findViewById(R.id.patientNameEditText);

        if(doctorEmailET.getText().toString().length() == 0 || patientNameEditText.getText().toString().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.alertErrorTitle)
                    .setMessage(R.string.allFieldsRequiredMessage)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Visualiser le PDF
        }
    }

    private void exportPDF() {
        final EditText doctorEmailET = findViewById(R.id.doctorEmailEditText);
        EditText patientNameEditText = findViewById(R.id.patientNameEditText);

        if(doctorEmailET.getText().toString().length() == 0 || patientNameEditText.getText().toString().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.alertErrorTitle)
                    .setMessage(R.string.allFieldsRequiredMessage)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Exporter le PDF

        }
    }
}
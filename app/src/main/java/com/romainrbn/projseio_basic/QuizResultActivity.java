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
import android.view.Menu;
import android.view.MenuItem;
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

    TextView diagnosisTV;

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

        diagnosisTV = findViewById(R.id.diagnosisTextView);





        int score = getIntent().getIntExtra("score", 0);
        String scoreResult = Integer.toString(score);
        resultTV.setText("" + scoreResult);
        resultTV.setTextColor(determineColorFromScore(score));

        String diagnosisForScore = determineDiagnosisForScore(score);

        diagnosisTV.setText(diagnosisForScore);

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

    private String determineDiagnosisForScore(int score) {
        if (score < 5) {
            return getString(R.string.diagnosisLow);
        } else if (score >= 5 && score < 8) {
            return getString(R.string.diagnosisMedium);
        } else {
            return getString(R.string.diagnosisHigh);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    int determineColorFromScore(int score) {
        if(score <= 4) {
            return Color.parseColor("#2dad1c");
        } else if (score > 4 && score < 8) {
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

            String q1 = getIntent().getStringExtra("rep0");
            String q2 = getIntent().getStringExtra("rep1");
            String q3 = getIntent().getStringExtra("rep2");
            String q4 = getIntent().getStringExtra("rep3");
            String q5 = getIntent().getStringExtra("rep4");

            String i1 = getIntent().getStringExtra("int0");
            String i2 = getIntent().getStringExtra("int1");
            String i3 = getIntent().getStringExtra("int2");
            String i4 = getIntent().getStringExtra("int3");
            String i5 = getIntent().getStringExtra("int4");

            // Visualiser le PDF
            Intent intent = new Intent(QuizResultActivity.this, PdfPreviewActivity.class);
            intent.putExtra("patientName", patientNameEditText.getText().toString());
            intent.putExtra("doctorEmail", doctorEmailET.getText().toString());
            intent.putExtra("score", getIntent().getIntExtra("score", 0));
            intent.putExtra("q1", q1);
            intent.putExtra("q2", q2);
            intent.putExtra("q3", q3);
            intent.putExtra("q4", q4);
            intent.putExtra("q5", q5);
            intent.putExtra("i1", i1);
            intent.putExtra("i2", i2);
            intent.putExtra("i3", i3);
            intent.putExtra("i4", i4);
            intent.putExtra("i5", i5);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.close_quiz_btn) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exportPDF() {
        final EditText doctorEmailET = findViewById(R.id.doctorEmailEditText);
        EditText patientNameEditText = findViewById(R.id.patientNameEditText);
        EditText patientFirstNameEditText = findViewById(R.id.patientFirstNameEditText);
        EditText patientBirthYearEditText = findViewById(R.id.patientBirthYearEditText);

        if(doctorEmailET.getText().toString().length() == 0 || patientNameEditText.getText().toString().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.alertErrorTitle)
                    .setMessage(R.string.allFieldsRequiredMessage)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            String q1 = getIntent().getStringExtra("rep0");
            String q2 = getIntent().getStringExtra("rep1");
            String q3 = getIntent().getStringExtra("rep2");
            String q4 = getIntent().getStringExtra("rep3");
            String q5 = getIntent().getStringExtra("rep4");

            String i1 = getIntent().getStringExtra("int0");
            String i2 = getIntent().getStringExtra("int1");
            String i3 = getIntent().getStringExtra("int2");
            String i4 = getIntent().getStringExtra("int3");
            String i5 = getIntent().getStringExtra("int4");

            // Exporter le PDF
            Intent intent = new Intent(QuizResultActivity.this, ShareResultActivity.class);
            intent.putExtra("patientName", patientNameEditText.getText().toString());
            intent.putExtra("doctorEmail", doctorEmailET.getText().toString());
            intent.putExtra("patientFirstName", patientFirstNameEditText.getText().toString());
            intent.putExtra("patientBirthYear", patientBirthYearEditText.getText().toString());
            intent.putExtra("score", getIntent().getIntExtra("score", 0));
            intent.putExtra("q1", q1);
            intent.putExtra("q2", q2);
            intent.putExtra("q3", q3);
            intent.putExtra("q4", q4);
            intent.putExtra("q5", q5);
            intent.putExtra("i1", i1);
            intent.putExtra("i2", i2);
            intent.putExtra("i3", i3);
            intent.putExtra("i4", i4);
            intent.putExtra("i5", i5);
            startActivity(intent);
        }
    }
}
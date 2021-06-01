package com.romainrbn.projseio_basic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        setTitle(R.string.examResultTitle);

        TextView resultTV = findViewById(R.id.resultTV);
        String scoreResult = Integer.toString(getIntent().getIntExtra("score", 0));
        resultTV.setText("Votre score est de " + scoreResult);
    }
}
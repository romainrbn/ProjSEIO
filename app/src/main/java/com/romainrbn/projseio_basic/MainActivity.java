package com.romainrbn.projseio_basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permet d'assigner une action au bouton flottant (aller au quizz).
        FloatingActionButton fab = findViewById(R.id.fab);
        Button logOutButton = findViewById(R.id.logOutButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQuizCreator();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    /**
     * Déconnecte l'utilisateur.
     */
    void logOut() {
        SharedPreferences preferences = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        editor.clear();
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), R.string.logOutIndicationMessage,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Ouvre le quizz.
     */
    void openQuizCreator() {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }
}
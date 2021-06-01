package com.romainrbn.projseio_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputEditText editTextRPPS = (TextInputEditText) findViewById(R.id.textInputEditText);
        Button goHomeButton = (Button) findViewById(R.id.showHomeScreenBtn);
        TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.textInputEditTextLayout);

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleGoHomeButtonClick(editTextRPPS);
            }
        });

        // Changer la couleur de fond de l'activité en blanc
        View root = goHomeButton.getRootView();
        root.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    /**
     * Permet de connecter l'utilisateur puis d'aller sur le menu principal en cas de réussite
     * de connexion
     * @param editText L'entrée correspondant au numéro RPPS entré par le médecin.
     */
    void handleGoHomeButtonClick(TextInputEditText editText) {
        int nbCharactersInEdit = editText.getText().length();

        // TODO: Vérifier que le numéro RPPS correspond bien à un médecin dans la base de données
        if(nbCharactersInEdit != 11) {
            editText.setError(getString(R.string.RPPSInputErrorMessage));
        } else {
            editText.setError(null);

            // On ouvre l'activité "MainActivity" contenant la page d'accueil
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            editText.setText("");
        }
    }

    /**
     * Permet de masquer le clavier logiciel lorsque l'utilisateur appuye sur une partie extérieure
     * à ce dernier
     * @param ev L'évènement tactile (appui sur le clavier typiquement)
     * @return Valeur booléenne retournée par le super (implémentation obligatoire).
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}
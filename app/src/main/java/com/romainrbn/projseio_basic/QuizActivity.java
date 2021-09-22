package com.romainrbn.projseio_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private int mCurrentPosition = 1;
    private List<Question> questionsList = null;
    private List<TextView> options = new ArrayList<>();
    private List<Integer> scoresForQuestions = new ArrayList<>();
    private int mSelectedOptionPosition = 0;
    private int finalScore = 0;
    private Boolean hasSelectedOption = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setTitle(R.string.newQuizTitle);

        // Permet de récupérer les données des questions à partir d'un fichier JSON
        // (fichier situé dans le répertoire res/raw)
        DataParser parser = new DataParser(this);
        questionsList = parser.getQuestions();
        setupQuestion(false);

        Button btnSubmit = findViewById(R.id.btn_submit);
        Button backButton = findViewById(R.id.back_button);

        if(mCurrentPosition == 1) {
            backButton.setVisibility(View.INVISIBLE);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNextButtonClicked(view);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackButtonPressed(view);
            }
        });
    }

    /**
     * Actionne le bouton "Suivant"
     * @param view La vue passée du View.OnClickListener(...).
     */
    private void handleNextButtonClicked(View view) {
        Button backButton = findViewById(R.id.back_button);

        if(!hasSelectedOption) {
            Snackbar.make(view, R.string.not_answered_question_error, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            // On ajoute le score en fonction de l'index du boutton, si la question est
            // 'scorable'
            backButton.setVisibility(View.VISIBLE);
            Question answeredQuestion = questionsList.get(mCurrentPosition - 1);

            if(mCurrentPosition == scoresForQuestions.size() + 1) {
                if(answeredQuestion.getScorable()) {
                    finalScore += mSelectedOptionPosition;
                    scoresForQuestions.add(mSelectedOptionPosition);
                } else {
                    scoresForQuestions.add(0);
                }
            } else {
                if(answeredQuestion.getScorable()) {
                  //  finalScore += mSelectedOptionPosition;
                    scoresForQuestions.set(mCurrentPosition-1, mSelectedOptionPosition);
                } else {
                    scoresForQuestions.set(mCurrentPosition-1, 0);
                }
            }

            mCurrentPosition++;
            // On configure la question suivante
            if(mCurrentPosition <= questionsList.size()) {
                // On passe à la question suivante.
                setupQuestion(false);
            } else {
                // Fin du quizz
                int scoreSum = 0;
                for(Integer i: scoresForQuestions) {
                    scoreSum += i;
                }
                Intent intent = new Intent(this, QuizResultActivity.class);
                for(int i = 0 ; i < scoresForQuestions.size() ; i++) {
                    int score1 = scoresForQuestions.get(i);
                    String rep = questionsList.get(i).getChoix().get(score1);
                    String intitule = questionsList.get(i).getIntitule();
                    intent.putExtra("rep" + Integer.toString(i), rep);
                    intent.putExtra("int" + Integer.toString(i), intitule);
                }
                intent.putExtra("score", scoreSum);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * Actionne le bouton "Précédent".
     * @param view La vue passée du View.OnClickListener(...).
     */
    private void handleBackButtonPressed(View view) {
        Button backButton = findViewById(R.id.back_button);
        mCurrentPosition--;
        System.out.println(mCurrentPosition);
        setupQuestion(true);
        if(mCurrentPosition <= 1) {
            backButton.setVisibility(View.INVISIBLE);
        } else {
            backButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Configure une question (données & interface).
     */
    private void setupQuestion(Boolean isBack) {
        resetData();
        addOptionsButtons(isBack);
        setupProgressBar();
        setupQuestionTitles();
    }

    /**
     * Réinitialise les données (option sélectionnée...) lors du passage à une autre question.
     */
    private void resetData() {
        for(TextView option: options) {
            ((ViewGroup) option.getParent()).removeView(option);
        }

        mSelectedOptionPosition = 0;
        hasSelectedOption = false;
        options = new ArrayList<>();
        setDefaultOptionStyle();
    }

    /**
     * Ajoute à l'interface les boutons des différentes options.
     */
    private void addOptionsButtons(Boolean isBack) {
        List<String> choix = questionsList.get(mCurrentPosition - 1).getChoix();
        LinearLayout linearLayout = findViewById(R.id.btns_ll);

        Button btnSubmit = findViewById(R.id.btn_submit);

        // Si on est sur la dernière question.
        if(mCurrentPosition >= questionsList.size()) {
            btnSubmit.setText(R.string.submitButtonFormTitle);
        }

        // On ajoute toutes les options en tant que TextView (statique).
        for(int i = 0; i < choix.size() ; i++) {
            TextView textView = createTextView(choix.get(i));
            if(textView.getParent() != null) {
                ((ViewGroup) textView.getParent()).removeView(textView);
            }
            linearLayout.addView(textView, i);
            options.add(textView);
        }

        // On convertit chaque TextView en un bouton en ajoutant un OnClickListener(...).
        for(int i = 0; i < options.size() ; i++ ) {
            final TextView option = options.get(i);
            final int finalI = i;


            if(isBack) {
                int selectedOptionBack = scoresForQuestions.get(mCurrentPosition-1);
                if(selectedOptionBack == i) {
                    setOptionAsSelected(finalI, option);
                }
            }

            // Changer le style du bouton lors de l'appui dessus.
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setOptionAsSelected(finalI, option);
                }
            });
        }
    }

    private void setOptionAsSelected(int finalI, TextView option) {
        hasSelectedOption = true;
        setDefaultOptionStyle();
        mSelectedOptionPosition = finalI;
        option.setTextColor(Color.parseColor("#363A43"));
        option.setTypeface(option.getTypeface(), Typeface.BOLD);
        option.setBackgroundResource(R.drawable.selected_option_border_bg);
    }

    /**
     * Configure le style des boutons d'options.
     */
    private void setDefaultOptionStyle() {
        for(TextView option: options) {
            option.setTextColor(Color.parseColor("#7A8089"));
            option.setTypeface(Typeface.DEFAULT);
            option.setBackgroundResource(R.drawable.default_option_border_bg);
        }
    }

    /**
     * Configure les titres de la question (titre + intitulé).
     */
    private void setupQuestionTitles() {
        Question question = questionsList.get(mCurrentPosition - 1);
        TextView titleTextView = findViewById(R.id.tv_question_titre);
        TextView intituleTextView = findViewById(R.id.tv_question_intitule);

        titleTextView.setText(question.intitule);
        intituleTextView.setText(question.titre);
    }

    /**
     * Configure la barre d'avancement.
     */
    private void setupProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(questionsList.size());
        progressBar.setProgress(mCurrentPosition);

        TextView progressBarTextView = findViewById(R.id.tv_progress);
        progressBarTextView.setText(mCurrentPosition+"/"+progressBar.getMax());
    }

    /**
     * Créé la TextView correspondant à une option de question.
     * @param title Le titre de l'option à ajouter.
     * @return Une TextView affichant l'option.
     */
    private TextView createTextView(String title) {
        TextView textView = new TextView(this);
        textView.setText(title);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 20, 10, 20);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.default_option_border_bg);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(35, 35, 35, 35);
        textView.setTextColor(Color.parseColor("#718089"));
        textView.setTextSize(18);

        return textView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
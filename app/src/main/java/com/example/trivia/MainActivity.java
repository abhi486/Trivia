package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.example.trivia.Data.AnswerQuestionAsync;
import com.example.trivia.Data.QuestionBank;
import com.example.trivia.Model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questiontextview,questioncountertextview,scoretextview;
    private Button truebutton,falsebutton;
    private ImageButton prevbutton,nextbutton;
    private int currentquestionindex=0;
    private List<Question> questionList;
    private int score = 0;
    private final String DATA_ID = "DATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questiontextview = findViewById(R.id.question_textview);
        questioncountertextview = findViewById(R.id.counter);
        truebutton = findViewById(R.id.true_button);
        falsebutton = findViewById(R.id.false_button);
        prevbutton = findViewById(R.id.prev_button);
        nextbutton = findViewById(R.id.next_button);
        scoretextview = findViewById(R.id.score_textview);

        prevbutton.setOnClickListener(this);
        nextbutton.setOnClickListener(this);
        truebutton.setOnClickListener(this);
        falsebutton.setOnClickListener(this);
         questionList = new QuestionBank().getQuestions(new AnswerQuestionAsync() {
            @Override
            public void Processfinished(ArrayList<Question> questionArrayList) {

                questiontextview.setText(questionList.get(currentquestionindex).getAnswer());
                questioncountertextview.setText("Q. " + (currentquestionindex+1) + " out of " + questionList.size());
                scoretextview.setText(" Points " + score + "/" + questionList.size());
                //Log.d("Main", "Processfinished: " + questionArrayList);
            }
        });
         SharedPreferences getsharedpref = getSharedPreferences(DATA_ID,MODE_PRIVATE);
         score = getsharedpref.getInt("Score",score);
         currentquestionindex = getsharedpref.getInt("Currentindex",currentquestionindex);

        //Log.d("Main", "onCreate: " + questionList);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.prev_button:
                prevques();
                break;

            case R.id.true_button:
                Check(true);

                break;

            case R.id.false_button:
                Check(false);
                break;

            case R.id.next_button:
                nextques();
                break;
        }
    }
    private void nextques()
    {
        if (currentquestionindex==questionList.size()) {
            currentquestionindex = 0;
            Toast.makeText(MainActivity.this,"You have reached end of questions",Toast.LENGTH_SHORT).show();
        }
        else currentquestionindex+=1;
        questiontextview.setText(questionList.get(currentquestionindex).getAnswer());
        questioncountertextview.setText("Q. " + (currentquestionindex+1) + " out of " + questionList.size());
    }
    private void prevques()
    {
        if (currentquestionindex==0) {
            Toast.makeText(MainActivity.this, "This is the first Question", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            currentquestionindex-=1;
            questiontextview.setText(questionList.get(currentquestionindex).getAnswer());
            questioncountertextview.setText("Q. " + (currentquestionindex+1) + " out of " + questionList.size());
        }

    }

    private void Check(boolean c) {
        if (c == questionList.get(currentquestionindex).isAnswertrue()) {
            Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            nextques();
            ++score;
            scoretextview.setText(" Points " + score + "/" + questionList.size());
            fadeview();
        } else {
            Toast.makeText(MainActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
            shakeanimation();
            nextques();
        }
    }
    private void shakeanimation()
    {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.anim);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeview()
    {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Score",score);
        editor.putInt("Currentindex",currentquestionindex);
        editor.apply();
    }
}

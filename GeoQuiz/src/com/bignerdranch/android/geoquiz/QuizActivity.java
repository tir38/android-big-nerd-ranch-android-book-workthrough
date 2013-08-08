package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.media.MediaRouter.UserRouteInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

	// =============== member variables ===================
	private Button mTrueButton;
	private Button mFalseButton;
	private Button mNextButton;
	private TextView mQuestionTextView;
	private int mCurrentQuestionIndex = 0; // this is the index in the questionBank array; it is NOT the question string's resource ID
	
	private Question[] mQuestionBank = new Question[]{
			new Question(R.string.question_africa, false),
			new Question(R.string.question_americans, true),
			new Question(R.string.question_asia, true),
			new Question(R.string.question_mideast, false),
			new Question(R.string.question_oceans, true),
	};

	// =============== class methods ==================
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
       
        // on startup, get the first question
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);  	// get view to populate
        updateQuetionText();														// update question
        
        // create true button and create its listener
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(true);
			}
		});
		
        // create false button and create its listener
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		checkAnswer(false);
        	}
		});
    
        // create next button and create its listener
        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get the next questionTextView by using next questions's resource ID
		        mCurrentQuestionIndex = (mCurrentQuestionIndex+1) % mQuestionBank.length;			// increment question index, reset to zero if at end
		        updateQuetionText();
			}
		});
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    private void updateQuetionText(){
    	  int currentStringResID = mQuestionBank[mCurrentQuestionIndex].getStringResID(); 	// get the resource ID of the string of the current question
    	  mQuestionTextView.setText(currentStringResID); 										// set the text of the view
    }
    
    private void checkAnswer(boolean userPressedTrue){
    	
    	int messageResID = 0;
    	boolean correctAnswer = mQuestionBank[mCurrentQuestionIndex].getAnswer();
    	
    	// get Toast
    	if (userPressedTrue == correctAnswer){ 	// user got the question right
    		messageResID = R.string.correct_toast;
    	}	
    	else {									// user got the question wrong
    		messageResID = R.string.incorrect_toast;
    	}
    	
    	// display Toast
    	Toast.makeText(this, messageResID, Toast.LENGTH_SHORT)
			 .show();
    	
    	// if user got answer correct move on to next question
    	if (userPressedTrue == correctAnswer){
	        mCurrentQuestionIndex = (mCurrentQuestionIndex+1) % mQuestionBank.length;			// increment question index, reset to zero if at end
        	updateQuetionText();
    	}
    }
}

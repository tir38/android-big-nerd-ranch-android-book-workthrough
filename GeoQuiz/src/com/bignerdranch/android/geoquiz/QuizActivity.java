package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

	// =============== member variables ===================
	private Button mTrueButton;
	private Button mFalseButton;
	private Button mCheatButton;
	private ImageButton mNextButton;
	private ImageButton mPreviousButton;
	
	private TextView mQuestionTextView;
	private int mCurrentQuestionIndex = 0; // this is the index in the questionBank array; it is NOT the question string's resource ID
	private boolean mIsCheater;
	
	private Question[] mQuestionBank = new Question[]{
			new Question(R.string.question_africa, false),
			new Question(R.string.question_americans, true),
			new Question(R.string.question_asia, true),
			new Question(R.string.question_mideast, false),
			new Question(R.string.question_oceans, true),
	};
	
	private static final String TAG = "QuizActivity"; // activity tag for Log
	private static final String KEY_INDEX = "index"; // key for the key/value pair to store mCurrentQuestionIndex in Bundle

	// =============== class methods ==================
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 		// call superclass method
        setContentView(R.layout.activity_quiz); 	// get view for this controller

        Log.d(TAG, "onCreate(Bundle) called");
        
        // check for saved state
        if (savedInstanceState != null){
        	mCurrentQuestionIndex = savedInstanceState.getInt(KEY_INDEX, 0); // 0 = default value
        }
        
        // on startup, get the first question
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);  	// get view to populate
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		        mCurrentQuestionIndex = (mCurrentQuestionIndex+1) % mQuestionBank.length; // increment question index, reset to zero if at end
				updateQuetionText();	// ... and update question
				
			}
		});
		
        updateQuetionText(); 	// update question (first time through)
        
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
        
        // create cheat button and its listener
        mCheatButton= (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent toCheatActivityIntent = new Intent(QuizActivity.this, CheatActivity.class); // create intent to pass to CheatActivity
				boolean tempAnswer = mQuestionBank[mCurrentQuestionIndex].getAnswer(); 	// get current question's answer
				toCheatActivityIntent.putExtra(CheatActivity.EXTRA_ANSWER, tempAnswer);	// add answer to extra
//				startActivity(toCheatActivityIntent); 									// launch Cheat Activity with intent
				startActivityForResult(toCheatActivityIntent, 0); 						// launch Cheat Activity with intent, expect return data
					}
		});
    
        // create previous image button and create its listener
        mPreviousButton = (ImageButton)findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mCurrentQuestionIndex == 0){
					mCurrentQuestionIndex = mQuestionBank.length-1; // assign index of last question, in case I get a negative number by going from 0 -> -1
				}
				else{
					mCurrentQuestionIndex = (mCurrentQuestionIndex-1) % mQuestionBank.length; // all other indices
				}
				mIsCheater = false; // reset cheater status for previous question
				updateQuetionText();
			}
		});
        
        // create next image button and create its listener
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get the next questionTextView by using next questions's resource ID
		        mCurrentQuestionIndex = (mCurrentQuestionIndex+1) % mQuestionBank.length;			// increment question index, reset to zero if at end
				mIsCheater = false; // reset cheater status for next question
		        updateQuetionText();
			}
		});
	}

	
	@Override
	public void onStart(){
		super.onStart();
		Log.d(TAG, "onStart() called");
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Log.d(TAG, "onPause() called");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.d(TAG, "onResume() called");
	}
	
	@Override
	public void onStop(){
		super.onStop();
		Log.d(TAG, "onStop() called");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(TAG, "onDestroy() called");
	}
	
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		Log.i(TAG, "saving state");
		savedInstanceState.putInt(KEY_INDEX, mCurrentQuestionIndex);
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
    	if (mIsCheater){	// first check for cheater
    		messageResID = R.string.judgement_toast;
    	}
    	else{ /// then if not cheater...
	    	if (userPressedTrue == correctAnswer){ 	// user got the question right
	    		messageResID = R.string.correct_toast;
	    	}	
	    	else {									// user got the question wrong
	    		messageResID = R.string.incorrect_toast;
	    	}
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

    @Override // get returned results from CheatActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
    	if(returnedIntent == null){ // if intent not returned
    		return;
    	}
    	
    	mIsCheater = returnedIntent.getBooleanExtra(CheatActivity.EXTRA_IS_CHEATER, false);
    }
}
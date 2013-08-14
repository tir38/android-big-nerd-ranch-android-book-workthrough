package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {
	
	// extra keys for intents
	public static final String EXTRA_ANSWER = "com.bignerdranch.android.geoquiz.answer"; // i want the full path of my intent extra so that I can call this Activity from anywhere
	public static final String EXTRA_IS_CHEATER = "com.bignerdranch.android.geoquiz.isCheater";
	
	public boolean mAnswer;
	public Button mShowAnswerButton;
	public TextView mAnswerTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);			// call superclass method
		setContentView(R.layout.activity_cheat);	// get view

		// get answer from intent
		Intent inputIntent = getIntent(); 							// could be combined into one line...
		mAnswer = inputIntent.getBooleanExtra(EXTRA_ANSWER,false); 	// ... split for understanding
		
		// set text view
		mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
		
		// set default return intent
		setIsCheater(false);
		
		// create show answer button and its listener
		mShowAnswerButton = (Button)findViewById(R.id.showAnswerButton);
		mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// show the correct answer in mAnswerTextView
				if (mAnswer){
					mAnswerTextView.setText(R.string.true_button); // reuse same text as button text
				}
				else{
					mAnswerTextView.setText(R.string.false_button);
				}
				setIsCheater(true);
			}
		});
		
	}

	
	
	private void setIsCheater(boolean isCheater){
		Intent outputIntent = new Intent(); 				// don't define package or class
		outputIntent.putExtra(EXTRA_IS_CHEATER, isCheater);	// add key/value pair extra to intent
		setResult(RESULT_OK, outputIntent);					// set intent
	}
}

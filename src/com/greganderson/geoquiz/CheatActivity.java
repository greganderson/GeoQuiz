package com.greganderson.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class CheatActivity extends Activity {
	
	public static final String EXTRA_ANSWER_IS_TRUE = "com.greganderson.geoquiz.answer_is_true";
	public static final String EXTRA_ANSWER_SHOWN = "com.greganderson.geoquizj.answer_shown";

	private static final String KEY_INDEX = "index";

	private boolean mAnswerIsTrue;
	private boolean mIsCheater;

	private TextView mAnswerTextView;
	private Button mShowAnswer;


	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

		mIsCheater = false;

		if (savedInstanceState != null)
			mIsCheater = savedInstanceState.getBoolean(KEY_INDEX, false);

		mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

		mAnswerTextView = (TextView)findViewById(R.id.answerTextView);

		setAnswerShownResult(mIsCheater);
		
		mShowAnswer = (Button)findViewById(R.id.showAnswerButton);
		mShowAnswer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAnswerIsTrue)
					mAnswerTextView.setText(R.string.true_button);
				else
					mAnswerTextView.setText(R.string.false_button);
				mIsCheater = true;
				setAnswerShownResult(mIsCheater);
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean(KEY_INDEX, mIsCheater);
	}

	private void setAnswerShownResult(boolean isAnswerShown) {
		Intent data = new Intent();
		data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
		setResult(RESULT_OK, data);
	}
}

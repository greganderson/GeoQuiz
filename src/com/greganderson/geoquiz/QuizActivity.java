package com.greganderson.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;
import android.content.Intent;
import android.app.ActionBar;
import android.os.Build;

public class QuizActivity extends Activity {

	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private static final String CHEATER_INDEX = "cheater";

	private Button mTrueButton;
	private Button mFalseButton;
	private Button mPreviousButton;
	private Button mNextButton;
	private TextView mQuestionTextView;
	private Button mCheatButton;
	private TextView mApiLevelTextView;

	private TrueFalse[] mQuestionBank = new TrueFalse[] {
		new TrueFalse(R.string.question_oceans, true),
		new TrueFalse(R.string.question_mideast, false),
		new TrueFalse(R.string.question_africa, false),
		new TrueFalse(R.string.question_americas, true),
		new TrueFalse(R.string.question_asia, true)
	};

	private boolean[] mCheatedOnQuestion = new boolean[mQuestionBank.length];

	private int mCurrentIndex = 0;

	private boolean mIsCheater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Bodies of Water");

		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
			mCheatedOnQuestion = savedInstanceState.getBooleanArray(CHEATER_INDEX);
			mIsCheater = mCheatedOnQuestion[mCurrentIndex];
		}

		mApiLevelTextView = (TextView)findViewById(R.id.api_level);
		mApiLevelTextView.setText("API level: " + Build.VERSION.SDK_INT);

		mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
		updateQuestion();
		mQuestionTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();
			}
		});

		mTrueButton = (Button)findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(true);
			}
		});

		mFalseButton = (Button)findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(false);
			}
		});

		mPreviousButton = (Button)findViewById(R.id.previous_button);
		mPreviousButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentIndex--;
				if (mCurrentIndex < 0)
					mCurrentIndex = mQuestionBank.length - 1;
				updateQuestion();
			}
		});

		mNextButton = (Button)findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();
			}
		});

		mCheatButton = (Button)findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(QuizActivity.this, CheatActivity.class);
				boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
				i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
				startActivityForResult(i, 0);
			}
		});
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.i(TAG, "onSaveInstanceState");
		savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
		savedInstanceState.putBooleanArray(CHEATER_INDEX, mCheatedOnQuestion);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		boolean cheated = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
		if (cheated)
			mCheatedOnQuestion[mCurrentIndex] = true;
		mIsCheater = mCheatedOnQuestion[mCurrentIndex];
	}

	private void updateQuestion() {
		int question = mQuestionBank[mCurrentIndex].getQuestion();
		mQuestionTextView.setText(question);
		mIsCheater = mCheatedOnQuestion[mCurrentIndex];
	}

	private void checkAnswer(boolean userPressedTrue) {
		boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

		int messageResId = 0;

		if (mIsCheater) {
			messageResId = R.string.judgment_toast;
		}
		else {
			if (userPressedTrue == answerIsTrue)
				messageResId = R.string.correct_toast;
			else
				messageResId = R.string.incorrect_toast;
		}

		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_quiz, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart() called");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause() called");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume() called");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop() called");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy() called");
	}
}

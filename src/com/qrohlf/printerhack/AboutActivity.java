package com.qrohlf.printerhack;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutlayout);
		TextView aboutText = (TextView)findViewById(R.id.abouttext);
		aboutText.setMovementMethod(LinkMovementMethod.getInstance());
	}

}

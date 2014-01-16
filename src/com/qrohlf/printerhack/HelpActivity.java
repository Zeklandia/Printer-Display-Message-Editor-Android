package com.qrohlf.printerhack;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helplayout);
		TextView helpText = (TextView)findViewById(R.id.helptext);
		helpText.setMovementMethod(LinkMovementMethod.getInstance());
	}

}

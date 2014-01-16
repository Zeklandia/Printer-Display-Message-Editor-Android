package com.qrohlf.printerhack;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class DonateActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donatelayout);
		TextView donateText = (TextView)findViewById(R.id.donate_text);
		donateText.setMovementMethod(LinkMovementMethod.getInstance());
	}
}

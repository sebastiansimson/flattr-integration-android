package com.flattr.android.intentintegration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

public class FlattrIntentIntegrationActivity extends Activity implements OnClickListener {
	RadioGroup radioGroup;
	EditText thing;
	EditText title;
	Button go;
	CheckBox flattr;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        thing = (EditText) findViewById(R.id.editText1);
        go = (Button) findViewById(R.id.button1);
        go.setOnClickListener(this);
        flattr = (CheckBox) findViewById(R.id.checkBox1);
        title = (EditText) findViewById(R.id.editText2);
    }

	@Override
	public void onClick(View v) {
		if (radioGroup.getCheckedRadioButtonId() == R.id.radio_intent) {
			if (flattr.isChecked()) {
				FlattrIntentIntegrator.flattrThing(this, thing.getText().toString(), title.getText().toString());
			} else {
				FlattrIntentIntegrator.showThing(this, thing.getText().toString(), flattr.isChecked());
			}
		} else {
			String uri = "";
			if (flattr.isChecked()) {
				uri = "flattr://flattr/?";
				uri = uri + "id=" + thing.getText().toString();
				uri = uri + "&title=" + title.getText().toString();
			} else {
				uri = "flattr://thing/?";
				if (thing.getText().toString().startsWith("http://") || thing.getText().toString().startsWith("https://")) {
					uri = uri + "url=" + thing.getText().toString();
				} else {
					uri = uri + "id=" + thing.getText().toString();
				}
			}

			//uri = uri + "&flattr=" + flattr.isChecked();
			Log.d("flattr", uri);
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
		}
	}
}
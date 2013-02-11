package br.com.cesaretransportesMobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AcessarGmail extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.acessar_gmail);
		Button botaoGmail = (Button)findViewById(R.id.acessarGmail_btAcesso);
		botaoGmail.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Uri uri = Uri.parse("http://www.gmail.com");
		startActivity(new Intent(Intent.ACTION_VIEW, uri));		
	}

}

package br.scapegoat.scapay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tv = (TextView) findViewById(R.id.txtCadastroVendedor);
        TextView tv2 = (TextView) findViewById(R.id.txtCadastroComprador);
        makeTextViewHyperlink(tv);
        makeTextViewHyperlink(tv2);
    }

    public void compradorClickCadastro(View v){
        Intent intent = new Intent(getApplicationContext(), CadastroCompradorActivity.class);
        startActivity(intent);
    }

    public void vendedorClickCadastro(View v){
        Intent intent = new Intent(getApplicationContext(), CadastroVendedorActivity.class);
        startActivity(intent);
    }

    public static void makeTextViewHyperlink(TextView tv) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(tv.getText());
        ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    public void fazerLogin(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}

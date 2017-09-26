package br.scapegoat.scapay;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static br.scapegoat.scapay.LoginActivity.makeTextViewHyperlink;

public class CadastroVendedorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_vendedor);
        TextView tv = (TextView) findViewById(R.id.linkAjudaPaypal);
        makeTextViewHyperlink(tv);

    }
    public void linkAjudaPaypalMe(View v){
        String urlPaypalMe = "https://www.paypal.com/paypalme/grab?country.x=US&locale.x=en_US&_ga=1.131223567.1473471820.1505781338";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlPaypalMe));
        startActivity(intent);
    }
}

package br.scapegoat.scapay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        EditText usernameTxt = (EditText) findViewById(R.id.editUsername);
        String aux = usernameTxt.getText().toString();
        EditText passwordTxt = (EditText) findViewById(R.id.editPassword);
        String aux2 = passwordTxt.getText().toString();

        if(aux.equalsIgnoreCase("scapay@adm.com") && aux2.equalsIgnoreCase("admscapay")) {
            Toast.makeText(LoginActivity.this, "Bem Vindo Administrador!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else {
            BuscaLoginTask buscar = new BuscaLoginTask();
            buscar.execute(aux);
        }
    }

    private class BuscaLoginTask extends AsyncTask<String,Void,String> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(LoginActivity.this,
                    "Aguarde","Chamando o servidor");
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            if (s != null){
                try {
                    JSONObject json = new JSONObject(s);
                    String nome = json.getString("PrimeiroNome");
                    String email = json.getString("Email");
                    String senha = json.getString("Senha");
                    int numeroCartao = json.getInt("NumeroCartao");
                    int codSeg = json.getInt("CodigoSeguranca");
                    EditText passwordTxt = (EditText) findViewById(R.id.editPassword);
                    String aux = passwordTxt.getText().toString();
                    if(aux.equalsIgnoreCase(senha)){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Senha Incorreta", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(LoginActivity.this, "Usuario Incorreto", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://localhost:50693/api/Comprador/5/" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept","application/json");
                if (connection.getResponseCode() == 200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder retorno = new StringBuilder();
                    String linha;
                    while ((linha = reader.readLine()) != null){
                        retorno.append(linha);
                    }
                    connection.disconnect();
                    return retorno.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

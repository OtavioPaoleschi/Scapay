package br.scapegoat.scapay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public void cadastraLoja(View v){
        try{
            EditText nomeEdit = (EditText) findViewById(R.id.nomeLoja);
            EditText emailEdit = (EditText) findViewById(R.id.emailComprador);
            EditText telefoneEdit = (EditText) findViewById(R.id.telefoneLoja);
            EditText cnpjEdit = (EditText) findViewById(R.id.cnpjLoja);
            EditText senhaEdit = (EditText) findViewById(R.id.senhaLoja);
            EditText linkEdit = (EditText) findViewById(R.id.linkPaypalMe);

            String nome = nomeEdit.getText().toString();
            String email = emailEdit.getText().toString();
            String telefone = telefoneEdit.getText().toString();
            String cnpj = cnpjEdit.getText().toString();
            String senha = senhaEdit.getText().toString();
            String link = linkEdit.getText().toString();

            CadastroVendedorTask cadastroTask = new CadastroVendedorTask();
            cadastroTask.execute(link,nome,email,telefone,cnpj,senha);
        }catch (Exception e){
            Toast.makeText(CadastroVendedorActivity.this,"Erro ao Cadastrar Usuario!",Toast.LENGTH_SHORT).show();
            Toast.makeText(CadastroVendedorActivity.this,"Verifique Sua Conexao com a Internet e Tente Novamente",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    }

    private class CadastroVendedorTask extends AsyncTask<String,Void,Integer> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CadastroVendedorActivity.this,"Aguarde","Enviando dados..");
        }

        @Override
        protected void onPostExecute(Integer status) {
            progressDialog.dismiss();
            if (status == 201){
                Toast.makeText(CadastroVendedorActivity.this,"Usuario Cadastrado com Sucesso!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(CadastroVendedorActivity.this,"Erro ao Cadastrar Usuario!",Toast.LENGTH_SHORT).show();
                Toast.makeText(CadastroVendedorActivity.this,"Verifique Sua Conexao com a Internet e Tente Novamente",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                URL url = new URL("http://localhost:50693/api/Vendedor");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");
                JSONStringer json = new JSONStringer();
                json.object();
                json.key("Link").value(params[0]);
                json.key("NomeComercio").value(params[1]);
                json.key("Email").value(params[2]);
                json.key("Telefone").value(params[3]);
                json.key("Cnpj").value(params[4]);
                json.key("Senha").value(params[5]);
                json.endObject();
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(json.toString());
                writer.close();
                return connection.getResponseCode();
            }catch (Exception e){
                e.printStackTrace();
            }
            return 1;
        }
    }
}

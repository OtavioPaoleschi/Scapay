package br.scapegoat.scapay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class CadastroCompradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_comprador);
    }

    public void cadastrarComprador(View v){
        EditText nomeEdit = (EditText) findViewById(R.id.nomeComprador);
        EditText sobrenomeEdit = (EditText) findViewById(R.id.sobrenomeComprador);
        EditText emailEdit = (EditText) findViewById(R.id.emailComprador);
        EditText dataNascEdit = (EditText) findViewById(R.id.dataNascimentoComprador);
        EditText telefoneEdit = (EditText) findViewById(R.id.telefoneComprador);
        EditText cpfEdit = (EditText) findViewById(R.id.cpfComprador);
        EditText senhaEdit = (EditText) findViewById(R.id.senhaComprador);
        EditText nomeCartaoEdit = (EditText) findViewById(R.id.nomeCartaoComprador);
        EditText numeroCartaoEdit = (EditText) findViewById(R.id.numeroCartaoComprador);
        EditText dataVencEdit = (EditText) findViewById(R.id.dataVencimentoCartaoComprador);
        EditText codSegEdit = (EditText) findViewById(R.id.codigoCartaoComprador);

        String nome = nomeEdit.getText().toString();
        String sobrenome = sobrenomeEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String dataNasc = dataNascEdit.getText().toString();
        String telefone = telefoneEdit.getText().toString();
        String cpf = cpfEdit.getText().toString();
        String senha = senhaEdit.getText().toString();
        String nomeCartao = nomeCartaoEdit.getText().toString();
        String numeroCartao = numeroCartaoEdit.getText().toString();
        String dataVenc = dataVencEdit.getText().toString();
        String codSeg = codSegEdit.getText().toString();

        CadastroCompradorTask cadastroTask = new CadastroCompradorTask();
        cadastroTask.execute(numeroCartao,dataVenc,codSeg,nome,sobrenome,email,dataNasc,telefone,cpf,senha);
    }

    private class CadastroCompradorTask extends AsyncTask<String,Void,Integer> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CadastroCompradorActivity.this,"Aguarde","Enviando dados..");
        }

        @Override
        protected void onPostExecute(Integer status) {
            progressDialog.dismiss();
            if (status == 201){
                Toast.makeText(CadastroCompradorActivity.this,"Usuario Cadastrado com Sucesso!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(CadastroCompradorActivity.this,"Erro ao Cadastrar Usuario!",Toast.LENGTH_SHORT).show();
                Toast.makeText(CadastroCompradorActivity.this,"Verifique Sua Conexao com a Internet e Tente Novamente",Toast.LENGTH_SHORT).show();
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
                json.key("NumeroCartao").value(params[0]);
                json.key("DtVencimento").value(params[1]);
                json.key("CodigoSegurança").value(params[2]);
                json.key("PrimeiroNome").value(params[3]);
                json.key("Sobrenome").value(params[4]);
                json.key("Email").value(params[5]);
                json.key("DtNascimento").value(params[6]);
                json.key("Telefone").value(params[7]);
                json.key("Cpf").value(params[8]);
                json.key("Senha").value(params[9]);
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

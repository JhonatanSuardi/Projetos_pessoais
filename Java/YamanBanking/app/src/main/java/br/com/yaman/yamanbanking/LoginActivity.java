package br.com.yaman.yamanbanking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_entrar;
    private TextInputEditText nAgencia, nConta, nSenha;
    private TextInputLayout lAgencia, lConta, lSenha;
    private ProgressBar prBar;
    private ConnectivityManager connectivityManager;
    private Boolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // BOTÃO ENTRAR
        btn_entrar = findViewById(R.id.btn_entrar);
        //CAMPOS DE TEXTO
        nAgencia = findViewById(R.id.nAgenciaLogin);
        nConta = findViewById(R.id.nContaLogin);
        nSenha = findViewById(R.id.nSenhaLogin);
        //CAMPOS DO LAYOUT
        lAgencia = findViewById(R.id.nEditAgencia);
        lConta = findViewById(R.id.nEditConta);
        lSenha = findViewById(R.id.nEditSenha);
        //CARREGAMENTO
        connectivityManager = (ConnectivityManager) LoginActivity.this.getSystemService((Context.CONNECTIVITY_SERVICE));
        prBar = findViewById(R.id.progressBar);

//        connectivityManager = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        exibirProgress(false);
//        Bundle dados = new Bundle();
//        dados.putString("Agencia", nAgencia.toString());

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit();
//                SharedPreferences preferences = getApplication().getSharedPreferences("DADOS", MODE_PRIVATE);
//                SharedPreferences.Editor editor;
//                editor = preferences.edit();
//                Log.i("Agencia", nAgencia.getText().toString());
//                editor.putString("agencia", nAgencia.getText().toString());
//                editor.putString("conta", nConta.getText().toString());
//                editor.putString("password", nSenha.getText().toString());
//                editor.apply();
            }
        });
    }

    private void exibirProgress(Boolean exibir) {
        if (connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isAvailable() &&
                connectivityManager.getActiveNetworkInfo().isConnected()) {
            prBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
        } else {
            Toast.makeText(LoginActivity.this, "Verifique sua conexão com a rede", Toast.LENGTH_LONG).show();
            prBar.setVisibility(View.GONE);
        }
    }

    public void submit() {
        if (!validaAgencia() || !validaConta() || !validaSenha()) {
            return;
        }
            verifyLogin();
    }


//    private void verifyLogin() {
//        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                exibirProgress(true);
//            }
//            @Override
//            protected Void doInBackground(Integer... integers) {
//                OkHttpClient cliente = new OkHttpClient();
//                MultipartBody.Builder builder = new MultipartBody.Builder();
//                builder.setType(MultipartBody.FORM);
//                builder.addFormDataPart("agencia", nAgencia.getText().toString().trim());
//                builder.addFormDataPart("numeroConta", nConta.getText().toString().trim());
//                builder.addFormDataPart("senha", nSenha.getText().toString().trim());
//                RequestBody corpo = builder.build();
//                Request request = new Request.Builder()
//                        .url("https://api-yaman-banking.herokuapp.com/operacao/login")
//                        .post(corpo)
//                        .build();
//                try {
//                    Response response = cliente.newCall(request).execute();
//                    if (response.code() == 200) {
//                        error = false;
//                    } else {
//                        error = true;
//                    }
//                }  catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }  catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                exibirProgress(false);
//                if(!error) {
//                    SharedPreferences preferences = getApplication().getSharedPreferences("dados", MODE_PRIVATE);
//                    SharedPreferences.Editor editor;
//                    editor = preferences.edit();
//                    editor.putString("agencia", nAgencia.getText().toString());
//                    editor.putString("conta", nConta.getText().toString());
//                    editor.putString("password", nSenha.getText().toString());
//                    editor.apply();
//                    Intent home = new Intent(getApplicationContext(), DrawerActivity.class);
//                    startActivity(home);
//                    finish();
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Verifique os dados inseridos no login", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }; task.execute();
//    }

    private void verifyLogin() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            public void onPreExecute() {
                super.onPreExecute();
                exibirProgress(true);
            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient cliente = new OkHttpClient();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("agencia", nAgencia.getText().toString().trim());
                builder.addFormDataPart("numeroConta", nConta.getText().toString().trim());
                builder.addFormDataPart("senha", nSenha.getText().toString().trim());
                RequestBody corpo = builder.build();
                Request request = new Request.Builder()
                        .url("https://api-yaman-banking.herokuapp.com/operacao/login")
                        .post(corpo)
                        .build();
                try {
                    Response response = cliente.newCall(request).execute();
                    if (response.code() == 200) {
                        error = false;
                    }
                    else{
                        error = true;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                exibirProgress(false);
                if (!error) {
                    SharedPreferences preferences = getApplication().getSharedPreferences("DADOS", MODE_PRIVATE);
                    SharedPreferences.Editor editor;
                    editor = preferences.edit();
                    editor.putString("agencia", nAgencia.getText().toString());
                    editor.putString("numeroConta", nConta.getText().toString());
                    editor.putString("senha", nSenha.getText().toString());
                    editor.apply();

                    Intent drawer = new Intent(getApplicationContext(), DrawerActivity.class);
                    startActivity(drawer);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Verifique os dados do login", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    public boolean validaAgencia() {
        if (nAgencia.getText().toString().trim().isEmpty() || nAgencia.length() < 4) {
            lAgencia.setError("Numero de caracteres insuficiente");
            requestFocus(nAgencia);
            return false;
        } else {
            lAgencia.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validaConta() {
        if (nConta.getText().toString().trim().isEmpty() || nConta.length() < 6) {
            lConta.setError("Numero de caracteres insuficiente");
            requestFocus(nConta);
            return false;
        } else {
            lAgencia.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validaSenha() {
        if (nSenha.getText().toString().trim().isEmpty() || nSenha.length() < 6) {
            lSenha.setError("Numero de caracteres insuficiente");
            requestFocus(nSenha);
            return false;
        } else {
            lSenha.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View View) {
        if (View.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT);
        }
    }

}

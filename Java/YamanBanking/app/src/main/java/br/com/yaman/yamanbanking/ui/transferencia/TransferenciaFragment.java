package br.com.yaman.yamanbanking.ui.transferencia;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import br.com.yaman.yamanbanking.DrawerActivity;
import br.com.yaman.yamanbanking.R;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;
import static br.com.yaman.yamanbanking.R.layout.confirmpassword;

public class TransferenciaFragment extends Fragment {

    private TransferenciaViewModel transferenciaViewModel;

    private Button btn_confirmar,btn_confirmaSenhaTransf;
    private TextView saldoccText;
    private TextInputLayout inputAgencial, inputContal, inputValorl, inputSenhal;
    private TextInputEditText inputAgenciat, inputContat, inputValort,inputSenhat;
    private String senha, agencia, conta;
    private SharedPreferences preferences;
    private double saldocc;
    private boolean error;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transferenciaViewModel =
                ViewModelProviders.of(this).get(TransferenciaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transferencia, container, false);

        btn_confirmar = root.findViewById(R.id.transfConfButton);
        inputAgencial = root.findViewById(R.id.transfAgencial);
        inputContal = root.findViewById(R.id.transfContal);
        inputValorl = root.findViewById(R.id.transfValorl);

        inputAgenciat = root.findViewById(R.id.transfAgenciat);
        inputContat = root.findViewById(R.id.transfContat);
        inputValort = root.findViewById(R.id.transfValort);
        saldoccText = root.findViewById(R.id.saldo_transf);

        preferences = getActivity().getSharedPreferences("DADOS", MODE_PRIVATE);
        agencia = preferences.getString("agencia", agencia);
        conta = preferences.getString("numeroConta", conta);
        senha = preferences.getString("senha", senha);
        requestTransferBalance();
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();

            }
        });
        return root;
    }

    private void confirmAlert(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.confirmpassword, null);

        inputSenhat = view.findViewById(R.id.confirmSenhaAlertText);
        btn_confirmaSenhaTransf = view.findViewById(R.id.btn_confirmaTransf);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Confirme sua senha");
        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.show();

        btn_confirmaSenhaTransf.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                    validatePassword();
            }

            private void validatePassword(){
                if(inputSenhat.getText().toString().equals(senha)){
                    requestValidAccount();
                    Toast.makeText(getContext(), "A senha foi confirmada com sucesso", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    final View view = inflater.inflate(R.layout.confirmpassword, null);
                    dialog.dismiss();
                    alert.setTitle("SENHA INVÁLIDA");
                    alert.setMessage("A senha não confere. Tente outra vez");
                    alert.setView(view);
                    alert.setCancelable(true);
                    alert.show();
                }
            }
        });

    }

    private void requestValidAccount() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                exibirProgress(true);
            }
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("destinatarioAgencia", inputAgenciat.getText().toString().trim());
                builder.addFormDataPart("destinatarioNumeroConta", inputContat.getText().toString().trim());
                builder.addFormDataPart("destinatarioTipoProdutoFinanceiro", "2");
                builder.addFormDataPart("remetenteAgencia", agencia.toString().trim());
                builder.addFormDataPart("remetenteNumeroConta", conta.toString().trim());
                builder.addFormDataPart("remetenteTipoProdutoFinanceiro", "2");
                builder.addFormDataPart("valorDaTransferencia", inputValort.getText().toString().trim());
                RequestBody corpo = builder.build();
                Request request = new Request.Builder()
                        .url("https://api-yaman-banking.herokuapp.com/operacao/transferir")
                        .post(corpo)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        error = false;
                        JSONObject I = new JSONObject(response.body().string());
                        saldocc = I.getDouble("valorContaCorrente");
                        ResponseBody responseBody = response.body();
                        responseBody.close();
                    }else {
                        Toast.makeText(getContext(),"CONTA INVÁLIDA",Toast.LENGTH_SHORT).show();
                        error = true;
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @SuppressLint("DefaultLocale")
            @Override
            protected void onPostExecute(Void aVoid) {
//                exibirProgress(false);
                if (!error) {
                    saldoccText.setText(String.format("Seu saldo: R$ %.2f", saldocc));
                    preferences = getActivity().getSharedPreferences("DADOS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editar = preferences.edit();
                    editar.putString("saldoCC", String.valueOf(saldoccText));
                    editar.apply();
                }
                else {
                    Toast.makeText(getContext(), "Verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                }
            }
        }; task.execute();
    }

    private void requestTransferBalance() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                exibirProgress(true);
            }
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api-yaman-banking.herokuapp.com/operacao/buscar-saldo?agencia="+agencia+"&numeroConta="+conta)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        error = false;
                        JSONObject I = new JSONObject(response.body().string());
                        saldocc = I.getDouble("valorContaCorrente");
                        ResponseBody responseBody = response.body();
                        responseBody.close();
                    }
                    else {
                        error = true;
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @SuppressLint("DefaultLocale")
            @Override
            protected void onPostExecute(Void aVoid) {
//                exibirProgress(false);
                if (!error) {
                    saldoccText.setText(String.format("Seu saldo: R$ %.2f", saldocc));
                    preferences = getActivity().getSharedPreferences("dados", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editar = preferences.edit();
                    editar.putString("saldoCC", String.format("R$ %.2f", saldocc));
                    editar.apply();
                }
                else {
                    Toast.makeText(getContext(), "Verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                }
            }
        }; task.execute();
    }

    public void submit() {
        if (!validaAgencia() || !validaConta()) {
            return;
        }

        confirmAlert();
//        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//        alert.setTitle("Confirme sua senha");
//        alert.setView(view);
//        alert.setCancelable(true);
//        final AlertDialog dialog = alert.show();
        return;
    }

    private boolean validaAgencia() {
        if (inputAgenciat.toString().trim().isEmpty() || inputAgenciat.length() < 4) {
            inputAgencial.setError("Numero de caracteres insuficiente");
//            requestFocus(inputAgenciat);
            return false;
        } else {
            inputAgencial.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validaConta() {
        if (inputContat.getText().toString().trim().isEmpty()) {
            inputContal.setError("Numero de caracteres insuficiente");
//            requestFocus(inputContat);
            return false;
        } else {
            inputContal.setErrorEnabled(false);
        }
        return true;
    }

//    private void requestFocus(View View) {
//        if (View.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT);
//        }
//    }
}

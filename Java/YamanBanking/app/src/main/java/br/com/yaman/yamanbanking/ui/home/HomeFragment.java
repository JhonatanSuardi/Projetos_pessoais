package br.com.yaman.yamanbanking.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import br.com.yaman.yamanbanking.LoginActivity;
import br.com.yaman.yamanbanking.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;
import static br.com.yaman.yamanbanking.R.id.valor_cc;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private String agence, conta, senha;
    private SharedPreferences preferences;
    private double valorCC,valorCP;
    private TextView saldoCC, saldoCP;
    private Boolean error;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        textAgencia =  root.findViewById(R.id.num_agencia);


//        vindo = root.findViewById(R.id.bem_vindo);
        saldoCC = root.findViewById(R.id.valor_cc);
        saldoCP = root.findViewById(R.id.valor_cp);


        preferences = this.getActivity().getSharedPreferences("DADOS", MODE_PRIVATE);
        agence = preferences.getString("agencia", agence);
        conta = preferences.getString("numeroConta", conta);
        senha = preferences.getString("senha", senha);
//        Log.i("teste", agence);
//        vindo.setText(agence);


//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        requestBalances();
        return root;
    }

    private void requestBalances() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                exibirProgress(true);
            }
            @Override
            protected Void doInBackground(Integer... integers) {
                Log.i("LOG_ENVIO", "GET_SALDOS");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                .url("https://api-yaman-banking.herokuapp.com/operacao/buscar-saldo?agencia="+agence+"&numeroConta="+conta)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        error = false;
                        JSONObject I = new JSONObject(response.body().string());
                        valorCC = I.getDouble("valorContaCorrente");
                        valorCP = I.getDouble("valorContaPoupanca");
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
                    saldoCC.setText(String.format("R$ %.2f", valorCC));
                    saldoCP.setText(String.format("R$ %.2f", valorCP));
                    preferences = getActivity().getSharedPreferences("dados", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editar = preferences.edit();
                    editar.putString("saldoCC", String.format("R$ %.2f", valorCC));
                    editar.putString("saldoCP", String.format("R$ %.2f", valorCP));
                    editar.apply();
                }
                else {
                    Toast.makeText(getContext(), "Verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                }
            }
        }; task.execute();
    }

//    private void exibirProgress(Boolean exibir) {
//        if (connectivityManager.getActiveNetworkInfo() != null &&
//                connectivityManager.getActiveNetworkInfo().isAvailable() &&
//                connectivityManager.getActiveNetworkInfo().isConnected()) {
//            prBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
//        } else {
//            Toast.makeText(LoginActivity.this, "Verifique sua conexão com a rede", Toast.LENGTH_LONG).show();
//            prBar.setVisibility(View.GONE);
//        }
//    }


}

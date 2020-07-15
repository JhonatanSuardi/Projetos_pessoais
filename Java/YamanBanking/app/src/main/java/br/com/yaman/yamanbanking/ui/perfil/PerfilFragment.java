package br.com.yaman.yamanbanking.ui.perfil;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import br.com.yaman.yamanbanking.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;

public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilViewModel;
    private boolean error;
    private String nomep, emailp, telefonep, agenciap, contap;
    private TextInputLayout lNomePerf,lTelefonePerf,lEmailPerf,lAgenciaPerf,lContaPerf;
    private TextInputEditText tNomePerf,tTelefonePerf,tEmailPerf,tAgenciaPerf,tContaPerf;
    private SharedPreferences preferences;
    private Button btnSalvar, btnEditar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilViewModel =
                ViewModelProviders.of(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        tNomePerf = root.findViewById(R.id.tNomePerf);
        tEmailPerf = root.findViewById(R.id.tEmailPerf);
        tTelefonePerf = root.findViewById(R.id.tTelefonePerf);
        tAgenciaPerf = root.findViewById(R.id.tAgPerf);
        tContaPerf = root.findViewById(R.id.tContaPerf);

        lNomePerf = root.findViewById(R.id.lNomePerf);
        lEmailPerf = root.findViewById(R.id.lEmailPerf);
        lTelefonePerf = root.findViewById(R.id.lTelPerf);
        lAgenciaPerf = root.findViewById(R.id.lAgPerf);
        lContaPerf = root.findViewById(R.id.lContaPerf);

        btnSalvar = root.findViewById(R.id.btn_salvarPerf);
        btnEditar = root.findViewById(R.id.btn_editarperf);
        btnEditar.setVisibility(View.GONE);
        preferences = this.getActivity().getSharedPreferences("DADOS", MODE_PRIVATE);
        agenciap = preferences.getString("agencia", agenciap);
        contap = preferences.getString("numeroConta", contap);


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Seus dados foram salvos com sucesso", Toast.LENGTH_SHORT).show();
            }
        });
        requestAccount();
        return root;
    }

    private void requestAccount() {
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
                        .url("https://api-yaman-banking.herokuapp.com/operacao/perfil?agencia="+agenciap+"&numeroConta="+contap)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        error = false;
                        JSONObject I = new JSONObject(response.body().string());
                        nomep = I.getString("nome");
                        emailp = I.getString("email");
                        telefonep = I.getString("telefone");
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
                    tNomePerf.setText(nomep);
                    tEmailPerf.setText(emailp);
                    tTelefonePerf.setText(telefonep);
                    tAgenciaPerf.setText(agenciap);
                    tContaPerf.setText(contap);
                    btnEditar.setVisibility(View.VISIBLE);
//                    saldoccText.setText(String.format("Seu saldo: R$ %.2f", saldocc));
//                    preferences = getActivity().getSharedPreferences("dados", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editar = preferences.edit();
//                    editar.putString("saldoCC", String.format("R$ %.2f", saldocc));
//                    editar.apply();
                }
                else {
                    Toast.makeText(getContext(), "Verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                }
            }
        }; task.execute();
    }
}

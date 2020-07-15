package br.com.yaman.yamanbanking.ui.extrato.contapoupanca;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.com.yaman.yamanbanking.R;
import br.com.yaman.yamanbanking.ui.extrato.contapoupanca.ListExtratoCP;
import br.com.yaman.yamanbanking.ui.extrato.contapoupanca.ListaExtratoCPViewAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;

public class ContaPoupancaFragment extends Fragment {

    private ConnectivityManager connectivityManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean error;
    private SharedPreferences preferences;
    private String agence, conta;
    private int i;
    public ArrayList<String> arrayDatas = new ArrayList<>();
    public ArrayList<String> arrayDescricao = new ArrayList<>();
    public ArrayList<String> arrayValor = new ArrayList<>();
    public List<ListExtratoCP> listaExtratoCP = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contapoupanca, container, false);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        mRecyclerView = view.findViewById(R.id.recycleViewCP);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getBaseContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        mAdapter = new ListaExtratoCPViewAdapter(listaExtratoCP);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        preferences = getActivity().getSharedPreferences("DADOS", MODE_PRIVATE);
        agence = preferences.getString("agencia", agence);
        conta = preferences.getString("conta", conta);

        carregarExtratoCC();
        return view;
    }

    private void carregarExtratoCC() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            public void onPreExecute() {
                super.onPreExecute();
//                exibirProgress(true);
            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient cliente = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api-yaman-banking.herokuapp.com/operacao/exibir-extrato?numeroConta=" + conta +
                                "&agencia=" + agence + "&tipoProdutoFinanceiro=1&dataInicio=2019-11-01&dataFim=2020-03-10")
                        .build();
                try {
                    Response response = cliente.newCall(request).execute();
                    if (response.code() == 200) {
                        error = false;
                        JSONArray array = new JSONArray(response.body().string());
                        for (i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            arrayDatas.add(object.getString("data"));
                            arrayDescricao.add(object.getString("descricao"));
                            arrayValor.add(object.getString("valor"));

                            listaExtratoCP.add(new ListExtratoCP(
                                    arrayDatas.get(i),
                                    arrayDescricao.get(i),
                                    arrayValor.get(i)));

                            ResponseBody responseBody = response.body();
                            responseBody.close();
                        }
                    } else {
                        error = true;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                exibirProgress(false);
                if (!error) {
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getActivity(), "Verifique os dados do login", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

}

package br.com.yaman.yamanbanking.ui.extrato.contacorrente;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.yaman.yamanbanking.R;

class ListaExtratoCCViewHolder extends RecyclerView.ViewHolder {

    public TextView datacc, valorcc, descrcc;

    public  ListaExtratoCCViewHolder(View view){
        super(view);
        datacc = view.findViewById(R.id.textDataCC);
        descrcc = view.findViewById(R.id.textDescrCC);
        valorcc = view.findViewById(R.id.textTransCC);
    }

}

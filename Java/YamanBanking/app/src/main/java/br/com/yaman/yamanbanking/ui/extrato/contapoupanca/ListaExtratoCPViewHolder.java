package br.com.yaman.yamanbanking.ui.extrato.contapoupanca;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.yaman.yamanbanking.R;

class ListaExtratoCPViewHolder extends RecyclerView.ViewHolder {

    public TextView datacp, valorcp, descrcp;

    public ListaExtratoCPViewHolder(View view){
        super(view);
        datacp = view.findViewById(R.id.textDataCP);
        descrcp = view.findViewById(R.id.textDescrCP);
        valorcp = view.findViewById(R.id.textTransCP);
    }

}

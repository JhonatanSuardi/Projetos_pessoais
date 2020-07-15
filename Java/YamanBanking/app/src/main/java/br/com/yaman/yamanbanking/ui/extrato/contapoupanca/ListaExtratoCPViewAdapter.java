package br.com.yaman.yamanbanking.ui.extrato.contapoupanca;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.yaman.yamanbanking.R;

class ListaExtratoCPViewAdapter extends RecyclerView.Adapter<ListaExtratoCPViewHolder> {

    public List<ListExtratoCP> listaExtratoCP;


    public ListaExtratoCPViewAdapter(List<ListExtratoCP> listaExtratoCP) {
        this.listaExtratoCP = listaExtratoCP;
    }

    @Override
    public ListaExtratoCPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_extratocp, parent, false);
        ListaExtratoCPViewHolder mViewHolder = new ListaExtratoCPViewHolder(itemView);

        return mViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void  onBindViewHolder(ListaExtratoCPViewHolder holder, int position){
        ListExtratoCP itemcp = listaExtratoCP.get(position);
        holder.datacp.setText(itemcp.getDataCC());
        holder.valorcp.setText(itemcp.getValorCC());
        holder.descrcp.setText(itemcp.getDescrCC());

        if (itemcp.getValorCC().contains("-")){
            holder.valorcp.setTextColor(R.color.colorAccent);
        }
    }

    public int getItemCount() {
        return listaExtratoCP.size();
    }
}


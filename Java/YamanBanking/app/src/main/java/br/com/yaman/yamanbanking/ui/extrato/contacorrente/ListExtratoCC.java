package br.com.yaman.yamanbanking.ui.extrato.contacorrente;

class ListExtratoCC {

    private String DataCC, ValorCC, DescrCC;

    public String getDescrCC() {
        return DescrCC;
    }

    public String getValorCC() {
        return ValorCC;
    }

    public String getDataCC() {
        return DataCC;
    }

    public ListExtratoCC(String dataCC, String descrCC, String valorCC){
        DataCC = dataCC;
        DescrCC = descrCC;
        ValorCC = valorCC;
    }
}

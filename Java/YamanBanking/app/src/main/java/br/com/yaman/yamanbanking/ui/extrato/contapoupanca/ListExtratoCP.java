package br.com.yaman.yamanbanking.ui.extrato.contapoupanca;

class ListExtratoCP {

    private String DataCP, ValorCP, DescrCP;

    public String getDescrCC() {
        return DescrCP;
    }

    public String getValorCC() {
        return ValorCP;
    }

    public String getDataCC() {
        return DataCP;
    }

    public ListExtratoCP(String dataCP, String descrCP, String valorCP){
        DataCP = dataCP;
        DescrCP = descrCP;
        ValorCP = valorCP;
    }
}

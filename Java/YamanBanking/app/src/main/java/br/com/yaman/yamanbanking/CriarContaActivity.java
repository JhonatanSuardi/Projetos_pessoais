package br.com.yaman.yamanbanking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CriarContaActivity extends AppCompatActivity {

    private Button btnEditar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_perfil);
        btnEditar = findViewById(R.id.btn_editarperf);
        btnEditar.setVisibility(View.GONE);
    }
}

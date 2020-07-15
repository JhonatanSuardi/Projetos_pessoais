package br.com.yaman.yamanbanking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    //    Button acessarConta = (Button) findViewById(R.id.acessar_conta);
    private Button bEntrar, bCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bEntrar = findViewById(R.id.acessar_conta);
        bCriarConta = findViewById(R.id.criar_conta);

        bEntrar.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent it = new Intent(MainActivity.this, LoginActivity.class);
                                           startActivity(it);
                                       }
                                   }

        );


        bCriarConta.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
//                                               View root = inflater.inflate(R.layout.fragment_perfil, container, false);
//                                               startActivity(it);
                                               Intent criarConta = new Intent(getApplicationContext(), CriarContaActivity.class);
                                               startActivity(criarConta);
                                           }
                                       }

        );

    }

//    acessarConta.OnClickListener(new View.OnClickListener()){
//        public void onClick(View V){
//            Intent it = new Intent(MainActivity.this, MainLogin.class);
//            startActivity(it);
//        }
//    }
//    public void login(){
//        Intent intent = new Intent(getApplicationContext(), MainLogin.class);
//        startActivity(intent);
//    }

}

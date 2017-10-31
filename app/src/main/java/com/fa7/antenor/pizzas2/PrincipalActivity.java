package com.fa7.antenor.pizzas2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fa7.antenor.pizzas2.domain.User;

import fa7.antenor.pizzas2.R;

/**
 * Created by Antenor on 24/10/2017.
 */

public class PrincipalActivity extends AppCompatActivity{

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Intent it = getIntent();
        user = (User) it.getSerializableExtra("user");
        TextView txtName = (TextView) findViewById(R.id.textName);
        txtName.setText(user.getName());

        String nome = user.getName();
        String email = user.getEmail();
    }
//
//    public void cadastrarCurso(View v) {
//        Intent intent = new Intent(this, EditarCursoActivity.class);
//        intent.putExtra("user", user);
//        startActivity(intent);
//    }
//
//    public void listarCurso(View v) {
//
//        EditText edtNomeCurso = (EditText) findViewById(R.id.edtNomeCurso);
//        String nomeCurso = edtNomeCurso.getText().toString();
//
//        Intent intent = new Intent(this, ListarCursoActivity.class);
//        intent.putExtra("nomecurso", nomeCurso);
//        intent.putExtra("user", user);
//        startActivity(intent);
//    }
}

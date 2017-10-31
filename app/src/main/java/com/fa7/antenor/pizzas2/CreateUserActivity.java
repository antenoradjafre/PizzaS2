package com.fa7.antenor.pizzas2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fa7.antenor.pizzas2.domain.User;
import com.fa7.antenor.pizzas2.domain.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fa7.antenor.pizzas2.R;

/**
 * Created by Antenor on 31/10/2017.
 */

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private FirebaseAuth auth;
    EditText edtName;
    EditText edtMail;
    EditText edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_activity);

        edtName = (EditText) findViewById(R.id.edtName);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtPass = (EditText) findViewById(R.id.edtPass);

        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        if (googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext()) != ConnectionResult.SUCCESS) {
            googleApiAvailability.makeGooglePlayServicesAvailable(this);
        }

        auth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        CreateUserActivity _this = this;
        String name = edtName.getText().toString();
        String mail = edtMail.getText().toString();
        String password = edtPass.getText().toString();
        user = new User(name, mail, password);

        auth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in authUser's information
                            FirebaseUser authUser = auth.getCurrentUser();
                            FirebaseDatabase data = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = data.getReference("USER");
                            myRef.setValue(user);
                            updateUI(authUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }

    private void updateUI(FirebaseUser authUser) {

        Util.idUser = authUser.getUid();
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra("user", this.user);
        startActivity(intent);
    }
}

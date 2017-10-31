package com.fa7.antenor.pizzas2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fa7.antenor.pizzas2.domain.User;
import com.fa7.antenor.pizzas2.domain.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import fa7.antenor.pizzas2.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtMail;
    EditText edtPassword;
    TextView mTextMensagem;
    String retrievedName;
    private FirebaseAuth mAuth;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMail = (EditText) findViewById(R.id.edtMail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        View view = getLayoutInflater().inflate(R.layout.activity_login, null);
        mTextMensagem = view.findViewById(android.R.id.empty);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        if (googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext()) != ConnectionResult.SUCCESS) {
            googleApiAvailability.makeGooglePlayServicesAvailable(this);
        }

        mAuth = FirebaseAuth.getInstance();
    }


    public void updateUI(FirebaseUser user) {

        Util.idUser = user.getUid();

        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra("user", this.user);
        startActivity(intent);
    }

    public void create(View v) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        final LoginActivity _this = this;

        final String email = edtMail.getText().toString();
        final String senha = edtPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG.sucesso", "signInWithEmail:success");
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users/"+mAuth.getCurrentUser().getUid());
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map values = (HashMap) dataSnapshot.getValue();
                                    if(values.containsKey("name")){
                                        retrievedName = (String) values.get("name");
                                        user = new User(retrievedName, email, senha);
                                        updateUI(mAuth.getCurrentUser());
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG.falha", "signInWithEmail:failure", task.getException());
                            Toast.makeText(_this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
}

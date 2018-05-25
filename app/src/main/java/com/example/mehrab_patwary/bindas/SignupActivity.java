package com.example.mehrab_patwary.bindas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private EditText emamilEt;
    private EditText passEt;
    private TextView statusTv;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emamilEt = findViewById(R.id.editTextEmail);
        passEt = findViewById(R.id.editTextPass);
        statusTv = findViewById(R.id.textViewStatus);
        auth = FirebaseAuth.getInstance();
    }

    public void singnUpButton(View view) {
        final String email = emamilEt.getText().toString();
        final String pass = passEt.getText().toString();
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email,pass); // cuwa method return a Task, so we store in task
        // now we set diffrent types of listner to return a message
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // if registration successfully compleated, onCompleate mdthod will be call and return task.
                if (task.isSuccessful()){
                    currentUser = auth.getCurrentUser();
                    statusTv.setText(currentUser.getEmail()+ " Welcome to Bindas");
                    statusTv.setVisibility(View.VISIBLE);
                    startActivity(new Intent(SignupActivity.this,DrawerActivity.class));
                }
                    /*if (currentUser !=null){
                        startActivity(new Intent(SignupActivity.this,DrawerActivity.class));
                    }*/
                }

        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                statusTv.setText(e.getMessage()); // why use e ??
                statusTv.setVisibility(View.VISIBLE);

            }
        });
    }
}

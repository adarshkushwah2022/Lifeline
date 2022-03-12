package com.example.mcproject;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    TextView signup,login,signup_second;
    Animation leftAnim, topAnim;
    ImageView login_logo;
    RelativeLayout left_shadow;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText edtTextEmail, edtTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        signup = findViewById(R.id.txtViewSignUp);
        login = findViewById(R.id.login_text_view);
        signup_second = findViewById(R.id.signup_text_view_1);
        signup.setOnClickListener(this);
        signup_second.setOnClickListener(this);

        login_logo = (ImageView)findViewById(R.id.login_logo);

        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        login_logo.setAnimation(topAnim);
        left_shadow = (RelativeLayout) findViewById(R.id.left_shadow);
        left_shadow.setAnimation(leftAnim);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        edtTextEmail = findViewById(R.id.edtTextLoginEmail);
        edtTextPassword = findViewById(R.id.edtTextLoginPassword);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.txtViewSignUp:
                Intent intent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
                LoginRegisterActivity.this.startActivity(intent);
                break;
            case R.id.btnLogin:
                progressBar.setVisibility(View.VISIBLE);
                login();
                break;
        }
    }

    public void login(){
        String email = edtTextEmail.getText().toString().trim();
        String password = edtTextPassword.getText().toString().trim();
        if(email.isEmpty()){
            progressBar.setVisibility(GONE);
            edtTextEmail.setError("Email is required");
            edtTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            progressBar.setVisibility(GONE);
            edtTextEmail.setError("Please provide valid email");
            edtTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            progressBar.setVisibility(GONE);
            edtTextPassword.setError("Password is required");
            edtTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            progressBar.setVisibility(View.GONE);
            edtTextPassword.setError("Minimum password length should be 6 characters");
            edtTextPassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(GONE);
                            Toast.makeText(LoginRegisterActivity.this, R.string.wrong_username_password, Toast.LENGTH_SHORT).show();
                            edtTextEmail.requestFocus();

                        } else {
                            checkIfEmailVerified();
                        }
                    }
                });
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            String uid = mAuth.getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
//                    Toast.makeText(LoginRegisterActivity.this, user.getUsername(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(GONE);
                    Intent intent = new Intent(LoginRegisterActivity.this, mainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else
        {
            progressBar.setVisibility(GONE);
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(LoginRegisterActivity.this, R.string.email_unverified, Toast.LENGTH_SHORT).show();
            startActivity(getIntent());
        }
    }
}
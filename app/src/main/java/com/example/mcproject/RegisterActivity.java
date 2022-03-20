package com.example.mcproject;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    TextView login;
    Animation topAnim, rightAnim;
    ImageView login_logo;
    RelativeLayout right_shadow;
    EditText edtTextUsername, edtTextEmail, edtTextPassword, edtTextCnfPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar progressBar;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        login=findViewById(R.id.Login_in_activity_register);
        login.setOnClickListener(this);
        right_shadow = (RelativeLayout)findViewById(R.id.left_shadow);
        login_logo = (ImageView)findViewById(R.id.signup_logo);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        login_logo.setAnimation(topAnim);
        right_shadow.setAnimation(rightAnim);
        progressBar = findViewById(R.id.progressBar);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtTextUsername = findViewById(R.id.edtTextUsername);
        edtTextEmail = findViewById(R.id.edtTextLoginEmail);
        edtTextPassword = findViewById(R.id.edtTextPassword);
        edtTextCnfPassword = findViewById(R.id.edtTextConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.Login_in_activity_register:
                Intent intent = new Intent(RegisterActivity.this,LoginRegisterActivity.class);
                RegisterActivity.this.startActivity(intent);
                break;
            case R.id.btnSignUp:
                signUpUser();
                break;
        }
    }

    private void signUpUser(){
        String username = edtTextUsername.getText().toString().trim();
        String email = edtTextEmail.getText().toString().trim();
        String password = edtTextPassword.getText().toString().trim();
        String cnfPassword= edtTextCnfPassword.getText().toString().trim();

        if(username.isEmpty()){
            edtTextUsername.setError("Username is required");
            edtTextUsername.requestFocus();
            return;
        }
        if(email.isEmpty()){
            edtTextEmail.setError("Email is required");
            edtTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtTextEmail.setError("Please provide valid email");
            edtTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            edtTextPassword.setError("Password is required");
            edtTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            edtTextPassword.setError("Minimum password length should be 6 characters");
            edtTextPassword.requestFocus();
            return;
        }
        if(cnfPassword.isEmpty()){
            edtTextCnfPassword.setError("Re-enter your password");
            edtTextCnfPassword.requestFocus();
            return;
        }
        if(!password.equals(cnfPassword)){
            edtTextCnfPassword.setError("Password doesn't match");
            edtTextCnfPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    progressBar.setVisibility(View.GONE);
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        edtTextEmail.setError("Email already exists!");
                                        edtTextEmail.requestFocus();
                                        return;
                                    }
                                    catch (Exception e)
                                    {
                                    }
                                }
                                else{
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, R.string.verify_email_successful, Toast.LENGTH_SHORT).show();
                                                User user = new User(username,email,password);
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.setVisibility(View.GONE);
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent = new Intent(RegisterActivity.this,LoginRegisterActivity.class);
                                                        RegisterActivity.this.startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(RegisterActivity.this, R.string.verify_email_unsuccessful , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }
                        }
                );
    }
}
package com.example.mcproject;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    TextView signup,login, forgotPassword;
    Animation leftAnim, topAnim;
    ImageView login_logo;
    RelativeLayout left_shadow;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText edtTextEmail, edtTextPassword;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "MCpref";
    private static final String KEY_UID = "UID";
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_FULLNAME = "FULLNAME";
    private static final String KEY_CONTACT = "CONTACTNO";
    private static final String KEY_BLOODGROUP = "BLOODGROUP";
    private static final String KEY_ADDRESS = "ADDRESS";
    private static final String KEY_PINCODE = "PINCODE";
    private ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        signup = findViewById(R.id.txtViewSignUp);
        login = findViewById(R.id.login_text_view);
        signup.setOnClickListener(this);
        forgotPassword = findViewById(R.id.txtViewForgotPassword);
        forgotPassword.setOnClickListener(this);
        connectivityManager = (ConnectivityManager)getSystemService(LoginRegisterActivity.CONNECTIVITY_SERVICE);
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
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
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
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()){
                    login();
                }
                else{
                    progressBar.setVisibility(GONE);
                    Toast.makeText(LoginRegisterActivity.this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtViewForgotPassword:
                progressBar.setVisibility(View.VISIBLE);
                sendForgotPasswordMail();
                break;
        }
    }

    private void sendForgotPasswordMail() {
        String email = edtTextEmail.getText().toString().trim();
        if(email.isEmpty()){
            progressBar.setVisibility(GONE);
            edtTextEmail.setError("Enter email to reset password");
            edtTextEmail.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginRegisterActivity.this, R.string.wrong_email, Toast.LENGTH_SHORT).show();
                            edtTextEmail.requestFocus();

                        } else {
                            Toast.makeText(LoginRegisterActivity.this, R.string.password_email_successful, Toast.LENGTH_SHORT).show();
                            edtTextPassword.requestFocus();
                        }
                    }
                });
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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_UID, uid);
                    editor.putString(KEY_USERNAME, user.getUsername());
                    editor.putString(KEY_FULLNAME, user.getFullname());
                    editor.putString(KEY_CONTACT, user.getContactNumber());
                    editor.putString(KEY_BLOODGROUP, user.getBloodGroup());
                    editor.putString(KEY_ADDRESS, user.getAddress());
                    editor.putString(KEY_PINCODE, user.getPincode());
                    editor.apply();
                    progressBar.setVisibility(GONE);
                    Intent intent = new Intent(LoginRegisterActivity.this, mainMenuActivity.class);
                    intent.putExtra(KEY_UID,uid);
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
            finish();
            startActivity(getIntent());
        }
    }
}
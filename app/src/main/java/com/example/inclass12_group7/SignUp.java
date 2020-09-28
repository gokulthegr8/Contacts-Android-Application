package com.example.inclass12_group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    Button bt_signup,bt_cancel;
    EditText et_firstName,et_lastName,et_email,et_password,et_confirmPassword;
    private FirebaseAuth mAuth;
    UserDetails us;
    static String USKEY="user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");
        mAuth = FirebaseAuth.getInstance();

        bt_signup=findViewById(R.id.bt_signup);
        bt_cancel=findViewById(R.id.bt_cancel);
        et_firstName=findViewById(R.id.et_firstName);
        et_lastName=findViewById(R.id.et_lastName);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        et_confirmPassword=findViewById(R.id.et_confirmPassword);


        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_firstName.getText().toString().equals("")) {
                    et_firstName.setError("Please enter first name");
                    Toast.makeText(SignUp.this, "First name is empty", Toast.LENGTH_SHORT).show();
                }
                else if (et_lastName.getText().toString().equals("")) {
                    et_lastName.setError("Please enter last name");
                    Toast.makeText(SignUp.this, "Last name is empty", Toast.LENGTH_SHORT).show();
                }
                else if (et_email.getText().toString().equals("")) {
                    et_email.setError("Please enter email");
                    Toast.makeText(SignUp.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (et_password.getText().toString().equals("")) {
                    et_password.setError("Please enter Password");
                    Toast.makeText(SignUp.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (et_confirmPassword.getText().toString().equals("")) {
                    et_confirmPassword.setError("Please enter confirm password");
                    Toast.makeText(SignUp.this, "Confirm Password field is empty", Toast.LENGTH_SHORT).show();
                }


                else if(!(et_password.getText().toString().equals(et_confirmPassword.getText().toString()))) {
                    et_password.setError("Password and Confirm Password does not match");
                    et_confirmPassword.setError("Password and Confirm Password does not match");

                    Toast.makeText(SignUp.this, "Password and Confirm Password does not match", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                            .addOnCompleteListener(SignUp.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("demo", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(getApplicationContext(), "User has been created",
                                                Toast.LENGTH_SHORT).show();
                                        us=new UserDetails(user.getEmail());
                                        Intent intent = new Intent(SignUp.this, ContactList.class);
                                        us.setEmail(user.getEmail());
                                        intent.putExtra(USKEY, us);

                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("demo", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), task.getException().toString(),
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });

                }

                }




        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    ImageView signin;
    EditText email,passowrd;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    TextView signupform;
    RelativeLayout r1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        signupform=(TextView)findViewById(R.id.signupform);
        email=(EditText)findViewById(R.id.username);
        passowrd=(EditText)findViewById(R.id.userpassword);
        signin=(ImageView)findViewById(R.id.signin_button);
        r1=findViewById(R.id.r1);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        overridePendingTransition(0,0);
        View relativeLayout=findViewById(R.id.login_container);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        relativeLayout.startAnimation(animation);


//        r1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(MainActivity.this,Registration.class);
//                startActivity(intent);
//            }
//        });
        if(user != null)

        {
            finish();
            startActivity(new Intent(MainActivity.this,Dashboard.class));
        }
//        else if(user.getEmail() =="iMuzammal@outlook.com"){
//            finish();
//            startActivity(new Intent(MainActivity.this,AdminPanel.class));
//        }



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Email=email.getText().toString();
                String Password=passowrd.getText().toString();

                if (email.getText().length() == 0) {
                    email.setError( "Enter email" );
                }
                else if (!isEmailValid(email.getText().toString())) {
                    email.setError("Enter Valid Email");
                }
                else if (passowrd.getText().length() == 0) {
                    passowrd.setError( "Enter password" );
                }



                else {

                    if(Email.equalsIgnoreCase("iMuzammal@outlook.com") && Password .equalsIgnoreCase("123456")){

                        progressDialog.setMessage( "Verificating..." );
                        progressDialog.show();
                        Intent intent= new Intent(MainActivity.this,AdminPanel.class);
                        startActivity(intent);
                     }
                    else{

                        login();
                    }


                    }

                }

        });

    }
    public void login() {

        progressDialog.setMessage( "Verificating..." );
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword( email.getText().toString(), passowrd.getText().toString() )
                .addOnCompleteListener( MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                        } else {
                            String  id = task.getResult().getUser().getUid();


                            SharedPreferencesManager.setSomeStringValue(getApplication(),id);
                            progressDialog.dismiss();
                            Intent intent = new Intent( MainActivity.this, Dashboard.class );
                            startActivity( intent );
                        }
                    }
                } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        passowrd.setText("");
        email.setText("");

    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

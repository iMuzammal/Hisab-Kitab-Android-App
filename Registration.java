package com.example.AccountingAppAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.AccountingAppAdmin.DataModel.UserProfile;
import com.example.AccountingAppAdmin.SharePerfernce.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration extends AppCompatActivity {
    EditText name, email, password;
    ImageView Signup;
    private String u;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    TextView signinform;
    ImageView uploadimg,showimg;
    private static final int PICK_IMAGE = 100;
    Uri imguri;
    RelativeLayout r1;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
//        signinform=(TextView)findViewById(R.id.signinform);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        showimg= findViewById(R.id.viewimg);
        uploadimg= findViewById(R.id.uploadimg);
        password = (EditText) findViewById(R.id.password);
//        r1=findViewById(R.id.r1);
        Signup = (ImageView) findViewById(R.id.signup_button);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog( this );
//        r1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent( Registration.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengellery();
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().length() == 0) {
                    name.setError("Enter Full name");
                }
                else if (!isEmailValid(email.getText().toString())) {
                    email.setError("Enter Valid Email");
                }
                else if (showimg.getDrawable() == null) {

                    Toast.makeText(Registration.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().length() == 0) {
                    email.setError("Enter  Email");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter  Password");
                } else {

                    singup();



                }

            }
        });

    }
    public void singup() {

        progressDialog.setMessage("Verificating...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            u = task.getResult().getUser().getUid();

                            SharedPreferencesManager.setSomeStringValue(getApplication(),u);
                            sendDataToDB(u);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sendDataToDB(String uid) {


        mDatabase.child("Users").child(uid).setValue(new UserProfile(uid, name.getText().toString(), "Image", email.getText().toString(), password.getText().toString()


        )).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                uploadimgstorage();
                startActivity(new Intent(Registration.this, AdminPanel.class));

            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void uploadimgstorage() {





        StorageReference riversRef= FirebaseStorage.getInstance().getReference().child(SharedPreferencesManager.getSomeStringValue(getApplication())).child( "Users-Profile-Pic" ).child( "Profile"+".jpg" );


        riversRef.putFile( imguri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Get a URL to the uploaded content


                Task<Uri> abc = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                abc.addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String url=uri.toString();


                        mDatabase.child("Users").child(SharedPreferencesManager.getSomeStringValue(getApplication())).setValue(new UserProfile(SharedPreferencesManager.getSomeStringValue(getApplication()), name.getText().toString(), url, email.getText().toString(), password.getText().toString()


                        )).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }
                } );

            }
        } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                } );
    }

    private void opengellery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int
            reusltCode, Intent data) {
        if (reusltCode == RESULT_OK && requestCode == PICK_IMAGE) {

            imguri = data.getData();
            showimg.setImageURI(imguri);
        }
    }

}

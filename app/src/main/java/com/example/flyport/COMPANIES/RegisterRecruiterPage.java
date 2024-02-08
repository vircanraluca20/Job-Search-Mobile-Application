package com.example.flyport.COMPANIES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flyport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterRecruiterPage extends AppCompatActivity {

    ImageView backButton5;
    Button signInButton;
    EditText companyNameEt, emailEt, passwordEt, countryEt, cityEt, phoneNumberEt;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://flyport-3bf8f-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    boolean passwordVisibility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_recruiter_page);

        backButton5=findViewById(R.id.backButton5);
        backButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginRecruiterPage.class);
                startActivity(intent);
            }
        });

        companyNameEt = findViewById(R.id.companyNameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        countryEt = findViewById(R.id.countryEt);
        cityEt = findViewById(R.id.cityEt);
        phoneNumberEt = findViewById(R.id.phoneNumberEt);
        signInButton=findViewById(R.id.signInButton);


        passwordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX()>=passwordEt.getRight() - passwordEt.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=passwordEt.getSelectionEnd();
                        if(passwordVisibility){
                            //set drawable image ee
                            passwordEt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_password_toggle,0);

                            //for hide
                            passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisibility=false;
                        }else{
                            //set drawable image ee
                            passwordEt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_visibility_off_24,0);

                            //for hide
                            passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisibility=true;

                        }
                        passwordEt.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryNameTxt=companyNameEt.getText().toString();
                String emailTxt=emailEt.getText().toString();
                String passwordTxt=passwordEt.getText().toString();
                String countryTxt=countryEt.getText().toString();
                String cityTxt=cityEt.getText().toString();
                String phoneNumberTxt = phoneNumberEt.getText().toString();

                if(countryNameTxt.isEmpty()|| emailTxt.isEmpty() || passwordTxt.isEmpty() || countryTxt.isEmpty() ||cityTxt.isEmpty() ||phoneNumberTxt.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{

                    auth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        String userId = user.getUid();

                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("companyName", countryNameTxt);
                                        userData.put("email", emailTxt);
                                        userData.put("password", passwordTxt);
                                        userData.put("country", countryTxt);
                                        userData.put("city", cityTxt);
                                        userData.put("phoneNumber", phoneNumberTxt);
                                        userData.put("tipUtilizator", "companie"); // Setează tipul de utilizator ca "companie"
                                        userData.put("isApproved", false); // Setează valoarea implicită a câmpului "isApproved" la "false"

                                        reference.child("users").child(userId).setValue(userData);

                                        Toast.makeText(getApplicationContext(), "User registration successful", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "User registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }
        });

    }
}
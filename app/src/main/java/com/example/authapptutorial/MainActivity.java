package com.example.authapptutorial;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    TextView fullName, email,phone, verifySMS;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button sendCode;
    Button resetPassLocal;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPassword);

        sendCode = findViewById(R.id.resendCode);
        verifySMS = findViewById(R.id.verifyMsg);

        fAuth = FirebaseAuth.getInstance();
        fStore =FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            verifySMS.setVisibility(View.VISIBLE);
            sendCode.setVisibility(View.VISIBLE);

            sendCode.setOnClickListener(v -> user.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(),"Verification Email Has Been Sent", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d("tag","onFailure: Email not sent " + e.getMessage() )));
        }

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, error) -> {
            fullName.setText(documentSnapshot.getString("fName"));
            email.setText(documentSnapshot.getString("email"));
            phone.setText(documentSnapshot.getString("phone"));
        });

        resetPassLocal.setOnClickListener(v -> {
            final EditText resetPassword = new EditText(v.getContext());

            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password");
            passwordResetDialog.setMessage("Enter New Password > 6 characters long");
            passwordResetDialog.setView(resetPassword);

            passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {
                //extract the email and send reset link
                String newPassword = resetPassword.getText().toString();
                user.updatePassword(newPassword).addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this,"Password Reset Succesfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Password reset failed", Toast.LENGTH_SHORT).show());

            });

            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {
                //closes dialog and returns to login page
            });

            passwordResetDialog.create().show();
        });
    }


    public void logout(View view){
        FirebaseAuth.getInstance().signOut();  //logout
        startActivity(new Intent(getApplicationContext(),Login.class));  //send user to login activity class again
        finish();

    }
}
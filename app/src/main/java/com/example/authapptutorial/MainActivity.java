package com.example.authapptutorial;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    Button sendCode, changeProfileImage;
    Button resetPassLocal;
    FirebaseUser user;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPassword);
        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfilePic);

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

        changeProfileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data); //data is what we got (image) from the gallery
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();  //logout
        startActivity(new Intent(getApplicationContext(),Login.class));  //send user to login activity class again
        finish();

    }
}
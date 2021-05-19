package com.example.authapptutorial;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class AccountFragment extends  AppCompatActivity{
    TextView fullName, email,phone, verifySMS;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button sendCode, changeProfileImage;
    Button resetPassLocal;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //initialise and assign variable
        BottomNavigationView bottomNav = findViewById(R.id.menu_navigation);
        //set chatbot main selected
        bottomNav.setSelectedItemId(R.id.account);

        //perform itemselectedlistner
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.account:
                    return true;
                case R.id.list:
                    startActivity(new Intent(getApplicationContext(), List.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.chatbot:
                    startActivity(new Intent(getApplicationContext(), MainChatbot.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.library:
                    startActivity(new Intent(getApplicationContext(), Library.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPassword);
        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfilePic);


        sendCode = findViewById(R.id.resendCode);
        verifySMS = findViewById(R.id.verifyMsg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        // ^.^ we can now upload images to firebase storage

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));


        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            verifySMS.setVisibility(View.VISIBLE);
            sendCode.setVisibility(View.VISIBLE);

            sendCode.setOnClickListener(v -> user.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d("tag", "onFailure: Email not sent " + e.getMessage())));
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
                user.updatePassword(newPassword).addOnSuccessListener(aVoid -> Toast.makeText(AccountFragment.this, "Password Reset Succesfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(AccountFragment.this, "Password reset failed", Toast.LENGTH_SHORT).show());

            });

            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {
                //closes dialog and returns to login page
            });

            passwordResetDialog.create().show();
        });

        changeProfileImage.setOnClickListener(v -> {
            //open gallery

            Intent i = new Intent(v.getContext(), EditAccount.class);
            i.putExtra("fullName", fullName.getText().toString());
            i.putExtra("email", email.getText().toString());
            i.putExtra("phone", phone.getText().toString());
            startActivity(i);
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //  super.onActivityResult(requestCode, resultCode, data); //data is what we got (image) from the gallery
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }



    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase storage
        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                //Toast.makeText(MainActivity.this,"Image Uploaded", Toast.LENGTH_SHORT).show()
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage))
        ).addOnFailureListener(e -> Toast.makeText(AccountFragment.this,"Image Upload Fail", Toast.LENGTH_SHORT).show());
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();  //logout
        startActivity(new Intent(getApplicationContext(),Login.class));  //send user to login activity class again
        finish();

    }

}
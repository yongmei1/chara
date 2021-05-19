 package com.example.authapptutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

 public class EditAccount extends AppCompatActivity  {

     public static final String TAG = "TAG";
     EditText profileFullName,profileEmail,profilePhone;
     ImageView profileImageView;
     Button saveBtn;
     FirebaseAuth fAuth;
     FirebaseFirestore fStore;
     FirebaseUser user;
     StorageReference storageReference;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user =fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        profileEmail = findViewById(R.id.profileEmailAddress);
        profileFullName = findViewById(R.id.profileFullName);
        profilePhone = findViewById(R.id.profilePhoneNo);
        profileImageView = findViewById(R.id.profileImageView);
        saveBtn = findViewById(R.id.saveProfileInfo);

         StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
         profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageView));

         profileImageView.setOnClickListener(v -> {
             Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
             startActivityForResult(openGalleryIntent,1000);
         });

        saveBtn.setOnClickListener(v -> {
            if (profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() || profilePhone.getText().toString().isEmpty()) {
                Toast.makeText(EditAccount.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            final String email1 = profileEmail.getText().toString();
            user.updateEmail(email1).addOnSuccessListener(aVoid -> {
                DocumentReference docRef = fStore.collection("users").document(user.getUid());
                Map<String, Object> edited = new HashMap<>();
                edited.put("email", email1);
                edited.put("fName", profileFullName.getText().toString());
                edited.put("phone", profilePhone.getText().toString());

                docRef.update(edited).addOnSuccessListener(aVoid1 -> Toast.makeText(EditAccount.this, "Profile Updated", Toast.LENGTH_SHORT).show());
                startActivity(new Intent(getApplicationContext(), AccountFragment.class));
                Toast.makeText(EditAccount.this, "Email is changed.", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> Toast.makeText(EditAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show());


            profileImageView.setOnClickListener(View -> Toast.makeText(EditAccount.this, "Profile Image Clicked", Toast.LENGTH_SHORT).show());

            profileEmail.setText(email1);
            profileFullName.setText(fullName);
            profilePhone.setText(phone);

            Log.d(TAG, "onCreate: " + fullName + " " + email1 + " " + phone);


        });
     }    @Override
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
                 fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageView))
         ).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Image Upload Fail", Toast.LENGTH_SHORT).show());


     }
 }

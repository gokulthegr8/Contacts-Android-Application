package com.example.inclass12_group7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

public class CreateContact extends AppCompatActivity {
    ImageView iv_createContactImage;
    EditText et_name,et_email,et_phone;
    Button bt_submit;
    FirebaseStorage storage;
    StorageReference storageRef;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    static String CTKEY="contact";
    static String USKEY="user";
    ContactDetails contacts;
    UserDetails us;
    private FirebaseFirestore db;


    Bitmap bitmap;
    Boolean isTakenPhoto = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        setTitle("Create New Contact");
        iv_createContactImage=findViewById(R.id.iv_createContactImage);
        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_phone=findViewById(R.id.et_phone);
        bt_submit=findViewById(R.id.bt_submit);

        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();
        if(getIntent()!= null && getIntent().getExtras()!=null){


            if(getIntent().getExtras().containsKey(ContactList.USKEY)){


                us = (UserDetails) getIntent().getExtras().getSerializable(ContactList.USKEY);


            }
        }
        iv_createContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().equals("")){
                    et_name.setError("Please enter name");
                    Toast.makeText(CreateContact.this, "Name is empty", Toast.LENGTH_SHORT).show();
                }
                else if(et_phone.getText().toString().equals("")){
                    et_phone.setError("Please enter Phone number");
                    Toast.makeText(CreateContact.this, "Phone number is empty", Toast.LENGTH_SHORT).show();
                }
                else if(et_email.getText().toString().equals("")){
                    et_email.setError("Please enter email");
                    Toast.makeText(CreateContact.this, "Email is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                if(isValid(et_email.getText().toString())){
                if(isTakenPhoto){
//                    final StorageReference imageStorageRef = storageRef.child("images/image.jpg");
                    final StorageReference imageStorageRef = storageRef.child("images/"+ new Date().getTime());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = imageStorageRef.putBytes(data);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return imageStorageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Log.d("demo", "Image URI: "+downloadUri);
                                contacts=new ContactDetails(et_name.getText().toString(),et_email.getText().toString(),et_phone.getText().toString(),downloadUri.toString(),"");
                                Log.d("gokul1","contacts is "+contacts);
                                db = FirebaseFirestore.getInstance();

                                db.collection(us.email)
                                        .add(contacts)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                contacts.DocID = documentReference.getId();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("gokul","Fail");

                                            }
                                        });


                                Intent intent = new Intent(CreateContact.this, ContactList.class);
                                Log.d("goku3","before intent contacts is"+contacts);
                                intent.putExtra(CTKEY, contacts);
                                intent.putExtra(USKEY, us);

                                startActivity(intent);
                                finish();

//                                Write method to upload all the contact data to Cloud Firestore.....
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });

                }
                else{
                    db = FirebaseFirestore.getInstance();
                    contacts=new ContactDetails(et_name.getText().toString(),et_email.getText().toString(),et_phone.getText().toString(),"","");

                    db.collection(us.email)
                            .add(contacts)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    contacts.DocID = documentReference.getId();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("gokul","Fail");

                                }
                            });
//                    Toast.makeText(CreateContact.this, "Please take a photo first!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateContact.this, ContactList.class);
                    Log.d("goku3","before intent contacts is"+contacts);
                    intent.putExtra(CTKEY, contacts);
                    intent.putExtra(USKEY, us);

                    startActivity(intent);
                    finish();

                }
                }
                else{
                    Toast.makeText(CreateContact.this, "Email Address format is incorrect. Please check.", Toast.LENGTH_SHORT).show();
                }

            }
            }

        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bitmap = imageBitmap;
            iv_createContactImage.setImageBitmap(imageBitmap);
            isTakenPhoto = true;
        }
    }
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}



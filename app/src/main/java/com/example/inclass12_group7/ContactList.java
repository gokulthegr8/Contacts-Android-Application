package com.example.inclass12_group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactList extends AppCompatActivity implements MyAdapter.InteractWithContactList {
    Button bt_createNewContact;
    ImageView iv_off;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;
    ArrayList<ContactDetails> contact=new ArrayList<ContactDetails>();
    ContactDetails ct;
    UserDetails us;
    static String USKEY="user";
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        setTitle("Contacts");
        bt_createNewContact=findViewById(R.id.bt_createNewContact);
        iv_off=findViewById(R.id.iv_off);
        mAuth = FirebaseAuth.getInstance();
//        if(getIntent()!= null && getIntent().getExtras()!=null){
//
//
//            if(getIntent().getExtras().containsKey(CreateContact.CTKEY)){
//
//
//                ct = (ContactDetails) getIntent().getExtras().getSerializable(CreateContact.CTKEY);
//                contact.add(ct);
//
//
//            }
//        }
//        if(getIntent()!= null && getIntent().getExtras()!=null){
//
//
//            if(getIntent().getExtras().containsKey(MainActivity.USKEY)){
//
//
//                us = (UserDetails) getIntent().getExtras().getSerializable(MainActivity.USKEY);
//                us.setEmail(us.email);
//
//            }
//        }
//        if(getIntent()!= null && getIntent().getExtras()!=null){
//
//
//            if(getIntent().getExtras().containsKey(CreateContact.USKEY)){
//
//
//                us = (UserDetails) getIntent().getExtras().getSerializable(CreateContact.USKEY);
//                us.setEmail(us.email);
//
//
//            }
//        }
        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        Log.d("newinst","user is"+user);
        us=new UserDetails(user.getEmail());
        String abc=user.getUid();
        Log.d("newinst","abc is"+abc);

        Log.d("goku3","Before doc get "+us.email);
        db.collection(us.email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ct = new ContactDetails(document.getString("name"),document.getString("email"), document.getString("phone"),  document.getString("image"),document.getId());
                                Log.d("goku2", document.getId() + " => " + document.getData());
                                contact.add(ct);

                            }
                            Log.d("goku2", "List is "+contact);
                            if(contact.size()>0){
                                recyclerView = findViewById(R.id.recyclerView);

                                recyclerView.setHasFixedSize(true);

                                rv_layoutManager = new LinearLayoutManager(ContactList.this);
                                recyclerView.setLayoutManager(rv_layoutManager);
                                rv_adapter = new MyAdapter(contact, ContactList.this);
                                recyclerView.setAdapter(rv_adapter);
                                rv_adapter.notifyDataSetChanged();


                            }

                        } else {
                            Log.d("goku2", "Error getting documents: ", task.getException());
                        }
                    }
                });

        iv_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ContactList.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_createNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactList.this, CreateContact.class);
                intent.putExtra(USKEY, us);
                startActivity(intent);
                finish();
            }
        });
//        if(contact.size()>0){
//            recyclerView = findViewById(R.id.recyclerView);
//
//            recyclerView.setHasFixedSize(true);
//
//            rv_layoutManager = new LinearLayoutManager(ContactList.this);
//            recyclerView.setLayoutManager(rv_layoutManager);
//            rv_adapter = new MyAdapter(contact, ContactList.this);
//            Log.d("gokul","new expense list is "+ct);
//            recyclerView.setAdapter(rv_adapter);
//            rv_adapter.notifyDataSetChanged();
//
//
//        }


}

    public void deleteItem(int position){

        contact.remove(position);
        rv_adapter.notifyDataSetChanged();
        Toast.makeText(ContactList.this, "Contact Deleted", Toast.LENGTH_SHORT).show();

    }}

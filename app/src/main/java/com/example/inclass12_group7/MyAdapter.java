package com.example.inclass12_group7;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {
    public static InteractWithContactList interact;
    Context ctx;
    private FirebaseFirestore db;
    UserDetails us;
    ContactList cl=new ContactList();
    MainActivity ma=new MainActivity();
    ArrayList<ContactDetails> contacts = new ArrayList<ContactDetails>();
    private FirebaseAuth mAuth;

    static String KEY = "contacts";

    public MyAdapter(ArrayList<ContactDetails> contacts, Context ctx) {
        this.contacts = contacts;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, final int position) {
        interact = (InteractWithContactList) ctx;
        holder.tv_contactName.setText(contacts.get(position).getName());
        holder.tv_Phone.setText(contacts.get(position).getPhone());
        holder.tv_email.setText(contacts.get(position).getEmail());
//        holder.iv_contactImage.setText("$"+contacts.get(position).getCost().toString());
        String imageURL = contacts.get(position).getImage();
        String img="@drawable/random";
        if(imageURL==""){
            int imageResource = ctx.getResources().getIdentifier(img, null, ctx.getPackageName());

            Drawable res = ctx.getDrawable(imageResource);
            holder.iv_contactImage.setImageDrawable(res);
        }
        else{
            Picasso.get().load(imageURL).into(holder.iv_contactImage);

        }
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        us=new UserDetails(user.getEmail());


        Log.d("adapter123","ada "+us.email);

        holder.linearLayout_Recycler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("adapter123","user email is "+us.getEmail());
                db.collection(us.email).document(contacts.get(position).DocID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("adaptergoku", "DocumentSnapshot successfully deleted!");
                                interact.deleteItem(position);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("adaptergoku", "Error deleting document", e);
                            }
                        });
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_contactName, tv_Phone,tv_email;
        ImageView iv_contactImage;

        ContactDetails ctn;
        LinearLayout linearLayout_Recycler;

        public ViewHolder(@NonNull final  View itemView) {
            super(itemView);
            tv_contactName = itemView.findViewById(R.id.tv_contactName);
            tv_Phone = itemView.findViewById(R.id.tv_Phone);
            tv_email = itemView.findViewById(R.id.tv_email);
            iv_contactImage = itemView.findViewById(R.id.iv_contactImage);

            linearLayout_Recycler=itemView.findViewById(R.id.linearLayout_Recycler);



        }
    }

    public interface InteractWithContactList
    {
        void deleteItem(int position);
    }
}


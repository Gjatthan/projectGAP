package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gap.misc.ChatBotAdapter;
import com.example.gap.misc.ChatBotModalClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class ChatBot extends AppCompatActivity {

    RecyclerView rview;
    Spinner sp;
    ArrayList<ChatBotModalClass> arrayList=new ArrayList<>();
    ChatBotAdapter adapter;
    ProgressBar pbar;
    SharedPreferences sharedPreferences;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        rview=findViewById(R.id.chat_container);
        rview.setLayoutManager(new LinearLayoutManager(this));

        arrayList.add(new ChatBotModalClass("","Hi this is GAP ChatBot. I can help u out in performing all the operations listed below"
                +" just select the query from the list and hit the send button",""));
        adapter=new ChatBotAdapter(arrayList,this);
        rview.setAdapter(adapter);

        sharedPreferences=getSharedPreferences("user_details",MODE_PRIVATE);
        image=sharedPreferences.getString("image","");
        sp=findViewById(R.id.helpspiner);
        pbar=findViewById(R.id.idPBLoading);

    }

    public void onClickChatSend(View v)
    {
        pbar.setVisibility(View.VISIBLE);
        DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference("helper");
        String help_text=sp.getSelectedItem().toString();

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(help_text))
                    {
                        String ans_text=dataSnapshot.child("ans").getValue().toString();
                        ans_text=ans_text.replaceAll("<>","\n");
                        arrayList.add(arrayList.size(),new ChatBotModalClass(help_text,ans_text,image));
                        adapter.notifyDataSetChanged();
                        rview.scrollToPosition(adapter.getItemCount()-1);
                        pbar.setVisibility(View.GONE);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatBot.this, "cancelled", Toast.LENGTH_SHORT).show();
                pbar.setVisibility(View.GONE);
            }
        });
    }
}
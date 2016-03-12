package com.shannor.simplechatapp.model;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.shannor.simplechatapp.ChatActivity;
import com.shannor.simplechatapp.R;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Shannor on 3/9/2016.
 * Pulls for the Abstract class FirebaseListAdpater and uses the type that we want to use.
 * Creates the view for each conversation item.
 */
public class ChatAdapter  extends FirebaseListAdapter<Conversation> {

    private String currentUser;
    private LayoutInflater mLayoutInflater;
    private Firebase mFireBaseRef;

    public ChatAdapter(Query ref, Activity context, int layout, String currentUser){
        super(ref,Conversation.class,layout,context);
        this.currentUser = currentUser;
        mLayoutInflater = LayoutInflater.from(context);
        mFireBaseRef = new Firebase(ChatActivity.FIREBASE_URL).child("users");
    }


    @Override
    protected void populateView(final View v, Conversation model) {
        String sender = model.getUid();
        //Pulls the user name from FireBase
        mFireBaseRef.child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> userInfo = (Map<String, String>) dataSnapshot.getValue(Map.class);
                TextView senderName = (TextView) v.findViewById(R.id.sender_name);
                senderName.setText(userInfo.get("name"));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ((TextView)v.findViewById(R.id.message_content)).setText(model.getMessage());

        //Can add time functionality if desired. Would be the same as previously done in this method.
    }
}

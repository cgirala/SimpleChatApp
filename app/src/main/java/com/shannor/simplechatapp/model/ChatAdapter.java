package com.shannor.simplechatapp.model;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.shannor.simplechatapp.R;

import java.util.List;

/**
 * Created by Shannor on 3/9/2016.
 */
public class ChatAdapter  extends FirebaseListAdapter<Conversation> {

    private String currentUser;
    private LayoutInflater mLayoutInflater;

    public ChatAdapter(Query ref, Activity context, int layout, String currentUser){
        super(ref,Conversation.class,layout,context);
        this.currentUser = currentUser;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    protected void populateView(View v, Conversation model) {
        String sender = model.getUid();
        TextView senderName = (TextView)v.findViewById(R.id.sender_name);
        senderName.setText(sender);
        ((TextView)v.findViewById(R.id.message_content)).setText(model.getMessage());
    }
}

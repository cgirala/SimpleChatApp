package com.shannor.simplechatapp;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.shannor.simplechatapp.model.ChatAdapter;
import com.shannor.simplechatapp.model.Conversation;

import java.io.Console;

public class ChatActivity extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://bootcampchat.firebaseio.com";

    private String uId;
    private Firebase mFireBaseRef;
    private Firebase mFireBaseMessages;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        //Gets the reference to Firebase
        mFireBaseRef = new Firebase(FIREBASE_URL);
        mFireBaseMessages = new Firebase(FIREBASE_URL).child("messages");
        //Creates an account for the user
        uId = setUpAuth();
        Log.d("Uid",uId);
        Firebase userIdSave = mFireBaseRef.child("users");
        userIdSave.child(uId).setValue("Shannor Trotty");

        mFireBaseRef = new Firebase(FIREBASE_URL).child("messages");

        Button postMessage = (Button)findViewById(R.id.btnSend);
        postMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ListView listView = (ListView)findViewById(R.id.list);
        mChatAdapter = new ChatAdapter(mFireBaseRef.limitToLast(40),this,R.layout.chat_item,uId);
        listView.setAdapter(mChatAdapter);
        mChatAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatAdapter.getCount() - 1);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String setUpAuth(){

        mFireBaseRef.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("Successful Auth" + authData);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println(firebaseError);
            }
        });
       return mFireBaseRef.getAuth().getUid();
    }

    public void sendMessage(){
        EditText inputText = (EditText)findViewById(R.id.sending_messages);
        String input = inputText.getText().toString();
        if(!input.equals("")){
            ServerValue serverVal = new ServerValue();
            Conversation mConversation = new Conversation(input,uId);
            Log.d("Firebase",mFireBaseMessages.getAuth().getUid());
            mFireBaseMessages.push().setValue(mConversation);
            inputText.setText("");
        }
    }
}

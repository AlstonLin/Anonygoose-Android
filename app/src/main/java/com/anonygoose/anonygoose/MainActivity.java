package com.anonygoose.anonygoose;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.media.MediaPlayer;
import java.util.ArrayList;

/**
 * This class contains most of the functionality for the app.
 *
 * @author Alston Lin
 */
public class MainActivity extends ActionBarActivity {

    public static final int DELAY = 500;
    private Typeface tf;
    private MediaPlayer mp;
    private MessageAdapter adapter;
    private String name;
    private boolean running = false;
    private ArrayList<String[]> messages = new ArrayList<>();
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);
        mp = MediaPlayer.create(this, R.raw.quack);
        mp.setVolume(1f, 1f);
        setupInitialTypefaces();
    }
    public void setupInitialTypefaces(){
        tf = Typeface.createFromAsset(getAssets(),"comic.ttf");
        ((TextView) findViewById(R.id.intro)).setTypeface(tf);
        ((EditText) findViewById(R.id.name)).setTypeface(tf);
    }
    public void setupTypefaces(){
        ((Button) findViewById(R.id.submit)).setTypeface(tf);
        ((EditText) findViewById(R.id.inputMessage)).setTypeface(tf);
    }
    public void clickStart(View v){
        EditText editText = (EditText) findViewById(R.id.name);
        name = editText.getText().toString();
        setContentView(R.layout.activity_main);
        //Sets up the messages list
        lv = (ListView) findViewById(R.id.messages);
        adapter = new MessageAdapter(this, R.layout.list_item, messages);
        lv.setAdapter(adapter);
        //Starts update thread
        running = true;
        DAO.getInstance().setActivity(this); //Instantiates the DAO
        setupTypefaces();
    }

    public void clickSubmit(View v){
        EditText editText = (EditText) findViewById(R.id.inputMessage);
        DAO.getInstance().pushMessage(name, editText.getText().toString());
        editText.setText("");
    }

    @Override
    public void onDestroy(){
        running = false;
        super.onDestroy();
    }


    public void updateMessages(final String[] update) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(update);
                mp.start();
                adapter.notifyDataSetChanged();
                lv.setSelection(messages.size() - 1);
            }
        });
    }
    private class MessageAdapter extends ArrayAdapter{
        private ArrayList<String[]> list; //FORMAT: list{NAME, DATE, MESSAGE}
        public MessageAdapter(Context context, int resource, ArrayList<String[]> list) {
            super(context, resource, list);
            this.list = list;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_item, null);
            }

            TextView name = (TextView) v.findViewById(R.id.name);
            TextView time = (TextView) v.findViewById(R.id.time);
            TextView message = (TextView) v.findViewById(R.id.message);
            String[] item = list.get(position);

            name.setTypeface(tf);
            time.setTypeface(tf);
            message.setTypeface(tf);

            name.setText(item[0]);
            time.setText(item[1]);
            message.setText(item[2]);
            return v;
        }
    }

}


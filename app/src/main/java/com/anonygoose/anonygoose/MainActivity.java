package com.anonygoose.anonygoose;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains most of the functionality for the app.
 *
 * @author Alston Lin
 */
public class MainActivity extends ActionBarActivity {

    public static final int DELAY = 500;
    private MessageAdapter adapter;
    private String name;
    private boolean running = false;
    private ArrayList<String[]> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);
    }

    public void clickStart(View v){
        EditText editText = (EditText) findViewById(R.id.name);
        name = editText.getText().toString();
        setContentView(R.layout.activity_main);
        //Sets up the messages
        ListView lv = (ListView) findViewById(R.id.messages);
        adapter = new MessageAdapter(this, R.layout.list_item, messages);
        lv.setAdapter(adapter);
        //Starts update thread
        running = true;
        new MessageUpdater().execute();
    }

    public void clickSubmit(View v){
        EditText editText = (EditText) findViewById(R.id.inputMessage);
        String time = DateFormat.getDateTimeInstance().format(new Date());
        DAO.getInstance().pushMessage(editText.getText().toString(), name, time);
    }

    @Override
    public void onDestroy(){
        running = false;
        super.onDestroy();
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

            name.setText(item[0]);
            time.setText(item[1]);
            message.setText(item[2]);
            return v;
        }
    }

    private class MessageUpdater extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] params) {
            while (running){
                ArrayList<String[]> messages = DAO.getInstance().getMessages();

                adapter.notifyDataSetChanged();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}


package com.anonygoose.anonygoose;

import java.net.URISyntaxException;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


/**
 * The Data Access Object (Singleton) for which the app will interact with the server.
  *
 * @author Alston Lin
 */
public class DAO {
    private MainActivity activity;
    private static DAO instance;
    private Socket socket;{
        try {
            socket = IO.socket("http://159.203.4.161:3000");
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener messageListener = new Emitter.Listener() {
        @Override
        public void call(Object[] args) {
            String[] update = new String[3];
            update[0] = (String) args[2];
            update[1] = (String) args[1];
            update[2] = (String) args[0];
            activity.updateMessages(update);
        }
    };
    private DAO(){
        socket.connect();
        socket.on("refresh feed", messageListener);
    } //Prevents other instances


    public static DAO getInstance(){
        if (instance == null) instance = new DAO();
        return instance;
    }


    public void setActivity(MainActivity activity){
        this.activity = activity;
    }
    /**
     * Publishes a message containing name, and text.
     */
    public void pushMessage(String name, String text){
        socket.emit("status added", text, name);
    }


}

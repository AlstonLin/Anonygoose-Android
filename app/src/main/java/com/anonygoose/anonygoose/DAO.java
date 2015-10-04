package com.anonygoose.anonygoose;

import java.util.ArrayList;

/**
 * The Data Access Object (Singleton) for which the app will interact with the server.
  *
 * @author Alston Lin
 */
public class DAO {
    public static ArrayList<String[]> messages = new ArrayList<>(); //TEMP

    private static DAO instance = new DAO();

    private DAO(){} //Prevents other instances

    public static DAO getInstance(){
        return instance;
    }
    public void pushMessage(String name, String time, String text){
        String[] message = new String[3];
        message[0] = name;
        message[1] = time;
        message[2] = text;
        messages.add(message);
    }

    public ArrayList<String[]> getMessages(){
        return messages;
    }
}

package src.io.catbot.conn;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by jason on 8/2/17.
 */
public class JsonHandler {
    //first string is id, second is data
    protected Map<String, String> contents;
    protected Gson gson;

    public JsonHandler(String s){

        BufferedReader br = generateReader(s);
        gson = new Gson();
        contents = gson.fromJson(br, HashMap.class);
    }

    private BufferedReader generateReader(String s){
        try {
            FileReader fr = new FileReader(s);
            BufferedReader br = new BufferedReader(fr);
            return br;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getValueFromKey(String s){

        if (contents.containsKey(s)){
            return contents.get(s);
        }

        return null;
    }

    public boolean containsField(String s){
        if (contents.containsKey(s)){
            return true;
        }
        return false;
    }

    public Set<String> getKeys(){
        return contents.keySet();
    }





}

package src.io.catbot.con;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

/**
 * Created by jason on 8/2/17.
 */
public class JSonHandler {
    //first string is id, second is data
    Map<String, Comparable> credentials;


    public JSonHandler(String s){

        BufferedReader br = generateReader(s);
        Gson gson = new Gson();
        credentials = gson.fromJson(br, Map.class);
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

    public Comparable get(String s){

        if (credentials.containsKey(s)){
            return credentials.get(s);
        }

        return null;
    }





}

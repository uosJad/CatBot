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
    Map<String, String> contents;


    public JSonHandler(String s){

        BufferedReader br = generateReader(s);
        Gson gson = new Gson();
        contents = gson.fromJson(br, Map.class);
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

    public String getValueFromField(String s){

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





}

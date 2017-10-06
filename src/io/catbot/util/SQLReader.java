package src.io.catbot.util;


import java.io.File;
import java.util.Scanner;

/**
 * Created by jason on 8/23/17.
 */
public class SQLReader {

    private String filePath;

    public SQLReader(String s) {
        filePath = s;
    }

    public String getFileString(){
        try {
            String content = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
            System.out.println(content);
            return content.replaceAll("[\\t\\n\\r]+"," ");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}

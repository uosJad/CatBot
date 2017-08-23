package src.io.catbot.db;

import java.sql.*;

/**
 * Created by jason on 7/18/17.
 */
public class DBConnector {

    private String connString;
    private String user;
    private String pass;

    public DBConnector(){}

    public void setConnString(String dbHost, String dbName, String dbUser, String dbPass) {
        connString = "jdbc:mysql://" + dbHost + "/" + dbName;
        user = dbUser;
        pass = dbPass;
    }

    private Connection getConnection(){
        Connection conn = null;
        try {
            //System.out.println("Trying to connect with " + this.connString);
            conn = DriverManager.getConnection(this.connString, user, pass);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }


    public void sendUpdate(String varSQL){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            statement.executeUpdate(varSQL);
            statement.close();
            conn.close();
            System.out.println("Updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet sendStatement(String varSQL){

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection conn =  getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(varSQL);

            displayColumnNames(rs.getMetaData());
            displayColumnValues(rs, rs.getMetaData().getColumnCount());

            return rs;


        }
        catch (Exception e){
            e.printStackTrace();
        }
        //TODO CLOSE
        /*finally {
            rs.close();
            statement.close();
            conn.close();
        }*/
        return null;
    }

    private void displayColumnNames(ResultSetMetaData rsMeta){

        try {

            int columnCount = rsMeta.getColumnCount();
            for (int col = 1;  col <=  columnCount; col++){
                System.out.format("-%-25s ", rsMeta.getColumnName(col));
            }
            System.out.println();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayColumnValues(ResultSet rs, int columnCount){

        try {
            //System.out.println(rs.next());
            while(rs.next()){
                //System.out.println(rs.next());
                for (int col = 1;  col <= columnCount; col++){
                    System.out.format("|%-25s ", rs.getString(col));

                }
                System.out.println();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}

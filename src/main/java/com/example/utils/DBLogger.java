package com.example.utils;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class DBLogger {
    static Connection con;
    static void connectToDB(){
        try {
            // Gateway Server setting
            String jumpServerHost = "ww2.cs.fsu.edu";
            String jumpServerUsername = "zlatkin";
            String jumpServerPassword = "ohDa$ic7";
            // DataBase settings
            String databaseHost = "dbsrv2.cs.fsu.edu";
            int databasePort = 3306;
            String databaseUsername = "zlatkin";
            String databasePassword = "vt3EWtO/lrM=";

            JSch jsch = new JSch();

            Session session;
            session = jsch.getSession(jumpServerUsername, jumpServerHost, 22);
            jsch.setKnownHosts("~/.ssh/known_hosts");
            session.setPassword(jumpServerPassword);
            //jsch.addIdentity("~/.ssh/id_rsa"); // Public key authentication example
            // Connect to SSH jump server
            session.connect();
            // Forward randomly chosen local port through the SSH channel to database host/port
            int forwardedPort = session.setPortForwardingL(0, databaseHost, databasePort);
            String driver = "org.mariadb.jdbc.Driver";
            Class.forName(driver);
            // Connect to the forwarded port (the local end of the SSH tunnel)
            String url = "jdbc:mariadb://localhost:" + forwardedPort + "/zlatkindb";
            con = DriverManager.getConnection(url, databaseUsername, databasePassword);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    static void insert(String line){
        try{
            Statement st = con.createStatement();
            //MariaDB Date format "YYYY-MM-DD"
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String timeStampToDB = dtf.format(now);
            System.out.println(timeStampToDB);
            String sqlQuery = "INSERT INTO SptringBootTable " +
                    "VALUES (\""+ timeStampToDB + "\", '" + line + "')";
            System.out.println(sqlQuery);
            st.executeUpdate(sqlQuery);
            System.out.println("query was successfully executed");
        }
        catch (SQLException s){
            System.out.println(s.getMessage());
            System.out.println("SQL statement is not executed!");
        }
    }

    static String getHistory(){
        try{
            Statement st = con.createStatement();
            String sql = "select * from SptringBootTable";
            ResultSet rs = st.executeQuery(sql);
            String result = "timeStamp" + "\t" + "line\n";
            // Extract result from ResultSet rs
            while(rs.next()){
                result += "" + rs.getString("timeStamp")
                        + "\t" + rs.getString("line") + "\n";
            }
            rs.close();
            return result;
        }
        catch (SQLException s){
            System.out.println(s.getMessage());
            System.out.println("SQL statement is not executed!");
            return "Internal error: 500";
        }
    }

    public static void main(String[] args){
        connectToDB();
        insert("test1");
        System.out.println(getHistory());
    }

}


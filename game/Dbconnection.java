package ru.nbki;

import javax.naming.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Dbconnection  {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.ibm.db2.jcc.DB2Driver");

        String empNo;
        Connection con;
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        // PrepStmt.setTimestamp(1, date);
        System.out.println(date);
        con = DriverManager.getConnection ("jdbc:db2://10.230.230.100:2668/cprosd22", "ros9appl", "R0s9Appl");
        con.setAutoCommit(false);
        Statement stmt;
        ResultSet rs;
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT FID FROM INDIC.ACCOUNT WHERE SERIAL_NUM = 1401");
        while (rs.next()) {
            empNo = rs.getString(1);
            System.out.println("Employee number = " + empNo);
        }
        rs.close();
        stmt.close();
        con.commit();
        con.close();




        date = new java.sql.Timestamp(new java.util.Date().getTime());
        // PrepStmt.setTimestamp(1, date);
        System.out.println(date);
        date = new java.sql.Timestamp(new java.util.Date().getTime());
        // PrepStmt.setTimestamp(1, date);
        System.out.println(date);

    }


}

// set DB in servlet

//        if(fl ){
//            try (Connection connection = ds.getConnection(); Statement statement = connection.createStatement()) {
//
//                statement.execute("CREATE TABLE logTable (" +
//                        "  request_id        INTEGER PRIMARY KEY IDENTITY," +
//                        "  request_time      TIMESTAMP," +
//                        "  sur_name          VARCHAR(900)," +
//                        "  first_name        VARCHAR(900)," +
//                        "  middle_name       VARCHAR(900)," +
//                        "  date_of_birth     DATE," +
//                        "  phone_number      VARCHAR(1000)," +
//                        "  response_time     TIMESTAMP," +
//                        "  response_msg      VARCHAR(1000)," +
//                        "  error_in_response BIT" +
//                        ");");
//                connection.commit();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            fl=false;
//        }


// check


//            try (Connection connection = ds.getConnection(); Statement statement = connection.createStatement()) {
//                ResultSet result = statement.executeQuery("SELECT * FROM PUBLIC.logTable");
//                while (result.next()){
//                    System.out.println("===================");
//                    for(int i=1;i<11;i++){
//                        System.out.println(result.getString(i));
//                    }
//
//                }
//            }

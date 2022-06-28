package com.vcc.ob.dao;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
//    @Value("${app.datasource.url}")

    private final String DB_URL = "jdbc:mysql://localhost:33061/database";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    public static Connection conn = null;

    public DAO(){
        if(conn == null){
            try {
                conn = DriverManager.getConnection (DB_URL, USERNAME, PASSWORD);


            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}

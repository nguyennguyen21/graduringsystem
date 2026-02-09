package com.devn.studentslearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class Boolenconnect {
    @Autowired
    private DataSource dataSource;
    @GetMapping("/Boolen-connect")
    public String Boolenconnect(){
        try(Connection connection = dataSource.getConnection()) {
            return "connection compelete!\n" +
                    "URL: " + connection.getMetaData().getURL() + "\n" +
                    "DB Product: " + connection.getMetaData().getDatabaseProductName();

        } catch (SQLException mess) {
            return "connect fail for:" + mess.getMessage();
        }


    }

}

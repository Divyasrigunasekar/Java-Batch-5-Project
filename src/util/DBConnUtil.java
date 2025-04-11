package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnUtil {
    public static Connection getConnection(String fileName) 
    {
        try {
        	System.out.println("Current Directory: " + new File(".").getAbsolutePath());
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(fileName);
            props.load(fis);

            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

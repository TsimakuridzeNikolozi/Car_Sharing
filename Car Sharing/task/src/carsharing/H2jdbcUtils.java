package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2jdbcUtils {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    static final String USER = "sa";
    static final String PASS = "";

    public static void createDB() {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = getConnection();
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

//            stmt.executeUpdate("DROP TABLE IF EXISTS CUSTOMER");
//            stmt.executeUpdate("DROP TABLE IF EXISTS CAR");
//            stmt.executeUpdate("DROP TABLE IF EXISTS COMPANY");

            String createCompany = "CREATE TABLE IF NOT EXISTS COMPANY" +
                    "(ID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL )";

            String createCar = "CREATE TABLE IF NOT EXISTS CAR " +
                    "(ID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "COMPANY_ID INTEGER NOT NULL, " +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID) )";

            String createCustomer = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(ID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "RENTED_CAR_ID INTEGER DEFAULT NULL, " +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID) )";


            stmt.executeUpdate(createCompany);
            stmt.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1");
            stmt.executeUpdate(createCar);
            stmt.executeUpdate("ALTER TABLE CAR ALTER COLUMN ID RESTART WITH 1");
            stmt.executeUpdate(createCustomer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}

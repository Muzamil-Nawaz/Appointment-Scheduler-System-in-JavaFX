package appointmentscheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to provide the database connection instance, to perform
 * the operations on the system database
 *
 * @author
 */
public class DbProvider {

    /**
     * A private constructor to prevent initialization of the class outside
     */
    private DbProvider() {

    }
    static Connection con = null;

    /**
     * This method uses a singleton design pattern to restrict system in making
     * more than one instance of the database connection and making it possible
     * to use same instance through out
     *
     * @return
     */
    public static Connection makeConnection() {
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                 ;
                System.out.println("Driver Not Loading");
            }

            try {
                // Replace the port with your mysql server port
                String url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08Fjs";
                // Replace this with username of your MySQL server
                String uname = "U08Fjs";
                // Replace this with password of your MySQL server 
                String pwd = "53689272175";
                con = DriverManager.getConnection(url, uname, pwd);

            } catch (SQLException e) {
                 ;
            }
        }
        return con;
    }
}

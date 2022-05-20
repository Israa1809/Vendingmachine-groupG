import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbInfo
{
    private final String JdbcUrl;
    private final String username;
    private final String password;
    public static Connection c;

    // method to hold database information
    public DbInfo() throws SQLException
    {
        this.JdbcUrl = "jdbc:mysql://localhost/vendingmachine?" + "autoReconnect=true&useSSL=false";
        this.username = "root";
        this.password = "Chokolade123";
    }

    // getters to fetch database information using return statements
    public String getJdbcUrl()
    {
        return JdbcUrl;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    // method to establish a connection to database
    public static Connection getConnection()
    {
        try
        {
            DbInfo d = new DbInfo();
            c = DriverManager.getConnection(d.getJdbcUrl(), d.getUsername(), d.getPassword());
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return c;
    }


}

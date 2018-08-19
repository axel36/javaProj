package ru.mbtc.guiceServlet.dbLog;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.mbtc.guiceServlet.data.DataHandler;
import ru.mbtc.guiceServlet.data.DataHandlerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DBloggerImplTest {
    private DBloggerImpl dBlogger;
    BasicDataSource ds;

    @Before
    public void setUp() throws Exception {
        ds = new BasicDataSource();
        ds.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        ds.setUrl("jdbc:hsqldb:mem:DBT");
        ds.setUsername("root");
        ds.setPassword("root");
        DataHandler dataHandler = new DataHandlerImpl();
        dBlogger = new DBloggerImpl(ds, dataHandler);
        initDatabase();
    }

    @After
    public void drop() throws SQLException {
        try (Connection connection = ds.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE logTable");
            connection.commit();
        }
        ds.close();
    }

    private void initDatabase() throws SQLException {
        try (Connection connection = ds.getConnection(); Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE logTable (" +
                    "  request_id        INTEGER IDENTITY PRIMARY KEY," +
                    "  request_time      TIMESTAMP," +
                    "  sur_name          VARCHAR(900)," +
                    "  first_name        VARCHAR(900)," +
                    "  middle_name       VARCHAR(900)," +
                    "  date_of_birth     DATE," +
                    "  phone_number      VARCHAR(1000)," +
                    "  response_time     TIMESTAMP," +
                    "  response_msg      VARCHAR(1000)," +
                    "  error_in_response BIT" +
                    ");");
            connection.commit();
//            statement.executeUpdate("INSERT INTO logTable (request_time, first_name, sur_name, middle_name) VALUES ( CURRENT_TIMESTAMP," +
//                    "'ivan','petrov','иваныч')");

            connection.commit();
        }
    }

    @Test
    public void connectionTest() {
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            ResultSet result = statement.executeQuery("SELECT * FROM logTable");

            if (result.next()) {
                for (int i = 1; i < 11; i++) {
                    System.out.println(result.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dBloggerNoErrorsTest() {
        final Map<String, String> query = new HashMap<>();
        query.put("firstName", "АННА");
        query.put("surName", "СИЗИКОВА");
        query.put("middleName", "АЛЕКСЕЕВНА");
        query.put("birthDate", "1988-07-29");
        query.put("number", "89603216597");
        dBlogger.setRequestTime();
        String resp = "{\"blaBla\": \"hello\"}";

        dBlogger.setResponseAndLogToDB(query, resp);
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            ResultSet result = statement.executeQuery("SELECT * FROM PUBLIC.logTable");

            if (result.next()) {
                assertEquals("АННА", result.getString("first_name"));
                assertEquals("СИЗИКОВА", result.getString("sur_name"));
                assertEquals("АЛЕКСЕЕВНА", result.getString("middle_name"));
                assertEquals("1988-07-29", result.getString("date_of_birth"));
                assertEquals("0", result.getString("error_in_response"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dBloggerErrorTest() {
        final Map<String, String> query = new HashMap<>();
        query.put("firstName", "АННА");
        query.put("surName", "СИЗИКОВА");
        query.put("middleName", "АЛЕКСЕЕВНА");
        query.put("birthDate", "1988-07-29");
        query.put("number", "89603216597");
        dBlogger.setRequestTime();
        String resp = "{\"error\": \"hello\"}";

        dBlogger.setResponseAndLogToDB(query, resp);
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            ResultSet result = statement.executeQuery("SELECT * FROM logTable");
            result.next();
            if (result.next()) {
                assertEquals("АННА", result.getString("first_name"));
                assertEquals("СИЗИКОВА", result.getString("sur_name"));
                assertEquals("АЛЕКСЕЕВНА", result.getString("middle_name"));
                assertEquals("1988-07-29", result.getString("date_of_birth"));
                assertEquals("1", result.getString("error_in_response"));

                for(int i=1;i<11;i++){
                    System.out.println(result.getString(i));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
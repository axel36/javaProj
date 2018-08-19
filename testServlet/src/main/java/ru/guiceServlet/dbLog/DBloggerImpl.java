package ru.mbtc.guiceServlet.dbLog;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ru.mbtc.guiceServlet.data.DataHandler;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class DBloggerImpl implements DBlogger {
    private DataSource ds;
    private DataHandler dataHandler;
    private Timestamp requestTs;

    @Inject
    public DBloggerImpl(@Named("logging")DataSource ds, DataHandler dataHandler) {
        this.ds = ds;
        this.dataHandler = dataHandler;
    }

    @Override
    public void setRequestTime() {
        requestTs = new Timestamp(new Date().getTime());
    }

    @Override
    public void setResponseAndLogToDB(Map<String, String> map, String resp) {
        Map<String, String> respMap;
        Integer errInt = 0;
        String sqlRequest = "insert into PUBLIC.logTable (request_time, sur_name, " +
                "first_name,  middle_name, date_of_birth, phone_number, response_time, response_msg, error_in_response) " +
                "VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?,?)";

        try {
            respMap = dataHandler.getMapFromJson(resp);
            if (respMap.get("error") != null) {
                errInt = 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try (Connection connection = ds.getConnection(); final PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {


            preparedStatement.setTimestamp(1, requestTs);
            preparedStatement.setString(2, map.get("surName"));
            preparedStatement.setString(3, map.get("firstName"));
            preparedStatement.setString(4, map.get("middleName"));

//            SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
//            System.out.println(map.get("birthDate"));
//            java.util.Date dt = formatter.parse("1988-07-29");
//            System.out.println(dt);
//            java.sql.Date birthDate;
//            birthDate = new java.sql.Date( dt.getTime());

            preparedStatement.setString(5, map.get("birthDate"));
            preparedStatement.setString(6, map.get("number"));

            Timestamp responseTs = new Timestamp(new Date().getTime());
            preparedStatement.setTimestamp(7,responseTs);
            preparedStatement.setString(8,resp);
            preparedStatement.setInt(9,errInt);

            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

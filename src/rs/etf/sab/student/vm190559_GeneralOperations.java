package rs.etf.sab.student;

import rs.etf.sab.operations.GeneralOperations;

import java.sql.Connection;
import java.util.Calendar;

public class vm190559_GeneralOperations implements GeneralOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public void setInitialTime(Calendar calendar) {

    }

    @Override
    public Calendar time(int i) {
        return null;
    }

    @Override
    public Calendar getCurrentTime() {
        return null;
    }

    @Override
    public void eraseAll() {

    }
}

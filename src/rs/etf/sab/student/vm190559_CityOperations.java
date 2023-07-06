package rs.etf.sab.student;

import rs.etf.sab.operations.CityOperations;

import java.sql.Connection;
import java.util.List;

public class vm190559_CityOperations implements CityOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createCity(String s) {
        return 0;
    }

    @Override
    public List<Integer> getCities() {
        return null;
    }

    @Override
    public int connectCities(int i, int i1, int i2) {
        return 0;
    }

    @Override
    public List<Integer> getConnectedCities(int i) {
        return null;
    }

    @Override
    public List<Integer> getShops(int i) {
        return null;
    }
}

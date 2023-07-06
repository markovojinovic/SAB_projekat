package rs.etf.sab.student;

import rs.etf.sab.operations.BuyerOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class vm190559_BuyerOperations implements BuyerOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createBuyer(String s, int i) {
        return 0;
    }

    @Override
    public int setCity(int i, int i1) {
        return 0;
    }

    @Override
    public int getCity(int i) {
        return 0;
    }

    @Override
    public BigDecimal increaseCredit(int i, BigDecimal bigDecimal) {
        return null;
    }

    @Override
    public int createOrder(int i) {
        return 0;
    }

    @Override
    public List<Integer> getOrders(int i) {
        return null;
    }

    @Override
    public BigDecimal getCredit(int i) {
        return null;
    }
}

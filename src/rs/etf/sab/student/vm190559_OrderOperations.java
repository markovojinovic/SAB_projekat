package rs.etf.sab.student;

import rs.etf.sab.operations.OrderOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

public class vm190559_OrderOperations implements OrderOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int addArticle(int i, int i1, int i2) {
        return 0;
    }

    @Override
    public int removeArticle(int i, int i1) {
        return 0;
    }

    @Override
    public List<Integer> getItems(int i) {
        return null;
    }

    @Override
    public int completeOrder(int i) {
        return 0;
    }

    @Override
    public BigDecimal getFinalPrice(int i) {
        return null;
    }

    @Override
    public BigDecimal getDiscountSum(int i) {
        return null;
    }

    @Override
    public String getState(int i) {
        return null;
    }

    @Override
    public Calendar getSentTime(int i) {
        return null;
    }

    @Override
    public Calendar getRecievedTime(int i) {
        return null;
    }

    @Override
    public int getBuyer(int i) {
        return 0;
    }

    @Override
    public int getLocation(int i) {
        return 0;
    }
}

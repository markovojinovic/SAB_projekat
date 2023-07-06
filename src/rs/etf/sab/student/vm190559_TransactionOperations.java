package rs.etf.sab.student;

import rs.etf.sab.operations.TransactionOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

public class vm190559_TransactionOperations implements TransactionOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int i) {
        return null;
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int i) {
        return null;
    }

    @Override
    public List<Integer> getTransationsForBuyer(int i) {
        return null;
    }

    @Override
    public int getTransactionForBuyersOrder(int i) {
        return 0;
    }

    @Override
    public int getTransactionForShopAndOrder(int i, int i1) {
        return 0;
    }

    @Override
    public List<Integer> getTransationsForShop(int i) {
        return null;
    }

    @Override
    public Calendar getTimeOfExecution(int i) {
        return null;
    }

    @Override
    public BigDecimal getAmmountThatBuyerPayedForOrder(int i) {
        return null;
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int i, int i1) {
        return null;
    }

    @Override
    public BigDecimal getTransactionAmount(int i) {
        return null;
    }

    @Override
    public BigDecimal getSystemProfit() {
        return null;
    }
}

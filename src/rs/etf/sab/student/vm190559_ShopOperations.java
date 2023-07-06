package rs.etf.sab.student;

import rs.etf.sab.operations.ShopOperations;

import java.sql.Connection;
import java.util.List;

public class vm190559_ShopOperations implements ShopOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createShop(String s, String s1) {
        return 0;
    }

    @Override
    public int setCity(int i, String s) {
        return 0;
    }

    @Override
    public int getCity(int i) {
        return 0;
    }

    @Override
    public int setDiscount(int i, int i1) {
        return 0;
    }

    @Override
    public int increaseArticleCount(int i, int i1) {
        return 0;
    }

    @Override
    public int getArticleCount(int i) {
        return 0;
    }

    @Override
    public List<Integer> getArticles(int i) {
        return null;
    }

    @Override
    public int getDiscount(int i) {
        return 0;
    }
}

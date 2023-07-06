package rs.etf.sab.student;

import rs.etf.sab.operations.ArticleOperations;

import java.sql.Connection;

public class vm190559_ArticleOperations implements ArticleOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createArticle(int i, String s, int i1) {
        return 0;
    }
}

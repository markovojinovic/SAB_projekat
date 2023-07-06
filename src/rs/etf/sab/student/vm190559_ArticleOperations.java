package rs.etf.sab.student;

import rs.etf.sab.operations.ArticleOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class vm190559_ArticleOperations implements ArticleOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createArticle(int shopId, String articleName, int articlePrice) {

        String query = "insert into Artikal (Ime, Cena, Kolicina, IdProd) values(?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, articleName);
            ps.setInt(2, articlePrice);
            ps.setInt(3, 0);
            ps.setInt(4, shopId);

            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}

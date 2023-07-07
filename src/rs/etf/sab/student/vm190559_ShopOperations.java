package rs.etf.sab.student;

import rs.etf.sab.operations.ShopOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class vm190559_ShopOperations implements ShopOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createShop(String name, String cityName) {

        String cityQuery = "select IdGrad from Grad where Ime = ?";

        try (PreparedStatement cityPs = connection.prepareStatement(cityQuery)) {
            cityPs.setString(1, cityName);

            ResultSet cityRs = cityPs.executeQuery();
            if (cityRs.next()) {
                int cityId = cityRs.getInt(1);
                String creatingQuery = "insert into Prodavnica (Ime, Popust, IdGrad) values (?, 0, ?)";

                try (PreparedStatement createPs = connection.prepareStatement(creatingQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    createPs.setString(1, name);
                    createPs.setInt(2, cityId);
                    createPs.execute();

                    ResultSet createRs = createPs.getGeneratedKeys();
                    if (createRs.next())
                        return createRs.getInt(1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int setCity(int shopId, String cityName) {
        String cityQuery = "select IdGrad from Grad where Ime = ?";

        try (PreparedStatement cityPs = connection.prepareStatement(cityQuery)) {
            cityPs.setString(1, cityName);

            ResultSet cityRs = cityPs.executeQuery();
            if (cityRs.next()) {
                int cityId = cityRs.getInt(1);
                String updateQuery = "update Prodavnica set IdGrad = ? where IdProd = ?";

                try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                    updatePs.setInt(1, cityId);
                    updatePs.setInt(2, shopId);

                    if (updatePs.execute())
                        return 1;
                    else
                        return -1;

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int getCity(int shopId) {

        String query = "select IdGrad from Prodavnica where IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, shopId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int setDiscount(int shopId, int discountPercentage) {

        String query = "update Prodavnica set Popust = ? where IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, discountPercentage);
            ps.setInt(2, shopId);
            if (ps.execute())
                return 1;
            else
                return -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int increaseArticleCount(int articleId, int increment) {

        String query = "Select * from Artikal where IdArt = ?";

        try (PreparedStatement ps = connection.prepareStatement(query,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            ps.setInt(1, articleId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int oldAmmount = rs.getInt("Kolicina");
                rs.updateInt("Kolicina", oldAmmount + increment);
                rs.updateRow();

                return oldAmmount + increment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int getArticleCount(int articleId) {

        String query = "select Kolicina from Artikal where IdArt = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, articleId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public List<Integer> getArticles(int shopId) {
        List<Integer> ret = new ArrayList<>();

        String query = "select IdArt from Artikal where IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, shopId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ret.add(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public int getDiscount(int shopId) {

        String query = "select Popust from Prodavnica where IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, shopId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}

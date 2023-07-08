package rs.etf.sab.student;

import rs.etf.sab.operations.BuyerOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class vm190559_BuyerOperations implements BuyerOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createBuyer(String name, int cityId) {

        String query = "insert into Kupac (Ime, Racun, PrometKupca, IdGrad) values(?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setInt(2, 0);
            ps.setBigDecimal(3, new BigDecimal(0));
            ps.setInt(4, cityId);

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

    @Override
    public int setCity(int buyerId, int cityId) {

        String query = "update Kupac set IdGrad = ? where IdKup = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, cityId);
            ps.setInt(2, buyerId);

            if (ps.executeUpdate() == 1)
                return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int getCity(int buyerId) {

        String query = "select IdGrad from Kupac where IdKup = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, buyerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public BigDecimal increaseCredit(int buyerId, java.math.BigDecimal credit) {

        String query = "select Racun from Kupac where IdKup = ?";
        BigDecimal racun;

        try (PreparedStatement ps = connection.prepareStatement(query,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {

            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                racun = rs.getBigDecimal(1);
                racun = racun.add(credit);

                rs.updateBigDecimal(1, racun);
                rs.updateRow();

                return racun;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int createOrder(int buyerId) {

        String creatingQuery = "insert into Porudzbina " +
                "(Status, KolicinaPopusta, Cena, VremePrijema, VremeSlanja, Lokacija, PopustKupca, IdKup, IdPut) " +
                "values(?, ?, ?, null, null, null, ?, ?, null)";

        try (PreparedStatement creatingPs = connection.prepareStatement(creatingQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            creatingPs.setString(1, "created");
            creatingPs.setBigDecimal(2, new BigDecimal(0));
            creatingPs.setBigDecimal(3, new BigDecimal(0));
            creatingPs.setBigDecimal(4, new BigDecimal(0));
            creatingPs.setInt(5, buyerId);

            creatingPs.execute();
            ResultSet creatingRs = creatingPs.getGeneratedKeys();
            if (creatingRs.next()) {
                return creatingRs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public List<Integer> getOrders(int buyerId) {
        List<Integer> ret = new ArrayList<>();

        String query = "select IdPor from Porudzbina where IdKup = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, buyerId);

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
    public BigDecimal getCredit(int buyerId) {

        String query = "select Racun from Kupac where IdKup = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, buyerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

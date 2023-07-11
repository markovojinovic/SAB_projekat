package rs.etf.sab.student;

import rs.etf.sab.operations.TransactionOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static rs.etf.sab.student.vm190559_GeneralOperations.timestampToCalendar;

public class vm190559_TransactionOperations implements TransactionOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int buyerId) {

        String query = "select Cena from Porudzbina where IdKup = ?";
        BigDecimal ret = new BigDecimal(0);
        ret = ret.setScale(3);

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, buyerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BigDecimal current = rs.getBigDecimal(1);
                current = current.setScale(3);
                ret = ret.add(current);
                return ret;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int shopId) {

        String query = "select Promet from Prodavnica where IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, shopId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getBigDecimal(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Integer> getTransationsForBuyer(int buyerId) {
        List<Integer> ret = new ArrayList<>();
        String query = "select t.IdTrans\n" +
                "from Transakcija t join Porudzbina p on t.IdPor = p.IdPor join Kupac k on p.IdKup = k.IdKup\n" +
                "where k.IdKup = ?";

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
    public int getTransactionForBuyersOrder(int orderId) {

        String query = "select IdTrans from Transakcija where IdPor = ? and IdProd = null";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, orderId);

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
    public int getTransactionForShopAndOrder(int orderId, int shopId) {

        String query = "select IdTrans from Transakcija where IdPor = ? and IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, orderId);
            ps.setInt(2, shopId);

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
    public List<Integer> getTransationsForShop(int shopId) {

        List<Integer> ret = new ArrayList<>();
        String query = "select IdTrans from Transakcija where IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, shopId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ret.add(rs.getInt(1));
            }

            if(ret.isEmpty())
                return null;

            return ret;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Calendar getTimeOfExecution(int transactionId) {

        String query = "select Vreme from Transakcija where IdTrans = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, transactionId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return timestampToCalendar(rs.getTimestamp(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {

        String query = "select Kolicina from Transakcija where IdPor = ? and IdProd = null";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getBigDecimal(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {

        String query = "select Kolicina from Transakcija where IdPor = ? and IdProd = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, shopId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getBigDecimal(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getTransactionAmount(int transactionId) {

        String query = "select Kolicina from Transakcija where IdTrans = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, transactionId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getBigDecimal(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getSystemProfit() {

        String query = "select Profit from Promenljive";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getBigDecimal(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

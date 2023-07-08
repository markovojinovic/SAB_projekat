package rs.etf.sab.student;

import rs.etf.sab.operations.CityOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class vm190559_CityOperations implements CityOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public int createCity(String name) {

        String query = "insert into Grad (Ime) values(?)";

        try (PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);

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
    public List<Integer> getCities() {

        List<Integer> ret = new ArrayList<>();

        String query = "select IdGrad from Grad";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

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
    public int connectCities(int cityId1, int cityId2, int distance) {

        if (cityId1 == cityId2)
            return -1;

        String checkQuery = "select * from Saobracajnica where Grad1 = ? and Grad2 = ?";

        try (PreparedStatement psCheck = connection.prepareStatement(checkQuery)) {
            psCheck.setInt(1, cityId1);
            psCheck.setInt(2, cityId2);

            ResultSet checkRs1 = psCheck.executeQuery();
            if (!checkRs1.next()) {
                try (PreparedStatement psSecond = connection.prepareStatement(checkQuery)) {

                    psSecond.setInt(1, cityId1);
                    psSecond.setInt(2, cityId2);

                    ResultSet checkRs2 = psSecond.executeQuery();
                    if (!checkRs2.next()) {
                        String insertQuery = "insert into Saobracajnica (Udaljenost, Grad1, Grad2) values (?, ?, ?)";
                        try (PreparedStatement psInsert = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

                            psInsert.setInt(1, distance);
                            psInsert.setInt(2, cityId1);
                            psInsert.setInt(3, cityId2);

                            psInsert.execute();
                            ResultSet returnRs = psInsert.getGeneratedKeys();

                            if (returnRs.next())
                                return returnRs.getInt(1);
                            else
                                return -1;

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
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
    public List<Integer> getConnectedCities(int cityId) {
        List<Integer> ret = new ArrayList<>();

        String query = "select Grad1 from Saobracajnica where Grad2 = ? " +
                "union select Grad2 from Saobracajnica where Grad1 = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, cityId);
            ps.setInt(2, cityId);

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
    public List<Integer> getShops(int cityId) {

        List<Integer> ret = new ArrayList<>();

        String query = "select IdProd from Prodavnica where IdGrad = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, cityId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ret.add(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }
}

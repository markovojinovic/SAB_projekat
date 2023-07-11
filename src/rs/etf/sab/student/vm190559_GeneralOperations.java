package rs.etf.sab.student;

import rs.etf.sab.operations.GeneralOperations;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class vm190559_GeneralOperations implements GeneralOperations {

    private static Connection connection = DB.getInstance().getConnection();

    @Override
    public void setInitialTime(Calendar calendar) {
        String query = "insert into Promenljive (Vreme, Profit) values (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setTimestamp(1, calendarToTimestamp(calendar));
            ps.setBigDecimal(2, new BigDecimal(0));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Calendar time(int days) {
        Calendar ret = this.getCurrentTime();
        systemTimeIncrease(days);
        List<Integer> sentOrder = getAllSentOrders();

        for (int currentOrder : sentOrder) {
            if (onePath(currentOrder)) {
                onePathTime(currentOrder, days);
            } else {
                morePathTime(currentOrder, days);
            }
        }

        return ret;
    }

    private void systemTimeIncrease(int days) {
        String query = "select Vreme from Promenljive";

        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp currentTime = rs.getTimestamp(1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentTime);
                calendar.add(Calendar.DAY_OF_MONTH, days);
                Timestamp increasedTimestamp = new Timestamp(calendar.getTimeInMillis());

                rs.updateTimestamp(1, increasedTimestamp);
                rs.updateRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getAllSentOrders() {
        List<Integer> ret = new ArrayList<>();
        String query = "select IdPor from Porudzbina where Status = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "sent");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                ret.add(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private boolean onePath(int orderId) {
        String query = "select IdPut from Porudzbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int currPath = rs.getInt(1);

                while (true) {
                    int next = getNextId(currPath);
                    if (next == -1)
                        break;
                    if (isFork(next))
                        return false;
                    currPath = next;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean isFork(int pathId) {
        String query = "select Fork from Put where IdPut = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pathId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1) == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void onePathTime(int orderId, int days) {
        String query = "select IdPut from Porudzbina where IdPor = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int currPath = rs.getInt(1);

                while (true) {
                    int next = getNextId(currPath);
                    if (!pathOver(currPath) || isFork(next))
                        break;

                    currPath = next;
                }

                decrease(currPath, days, orderId);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private int getForkId(int orderId) {
        String query = "select IdPut from Porudzbina where IdPor = ?";
        String query2 = "select IdPut, Fork from Put where IdPut = ?";
        int prev = -1;
        int id = -1;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                id = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try (PreparedStatement ps = connection.prepareStatement(query2)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    if (rs.getInt(2) == 0)
                        break;
                    else
                        prev = rs.getInt(1);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return prev;
    }

    private int getNextId(int pathId) {
        String query = "select IdNextPut from Put where IdPut = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pathId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int getDistance(int pathId) {

        String query = "select Dani from Put where IdPut = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pathId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int updateCurrentNext(int pathId, int days) {

        String query = "select Dani, IdNextPut from Put where IdPut = ?";

        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            ps.setInt(1, pathId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int ret = rs.getInt(2);
                int oldDays = rs.getInt(1);
                int newDays = oldDays - days;
                if (newDays < 0)
                    newDays = 0;
                rs.updateInt(1, newDays);
                rs.updateRow();

                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private boolean allArrived(int forkId) {

        String query = "select Dani, Fork from Put where IdPut = ?";
        int nextId = getNextId(forkId);

        while (nextId != -1) {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, nextId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(2) == 1) {
                        if (rs.getInt(1) != 0)
                            return false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            nextId = getNextId(nextId);
        }

        return true;
    }

    private boolean pathOver(int pathId) {
        String query = "select Dani from Put where IdPut = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pathId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1) == 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    private void morePathTime(int orderId, int days) {
        int forkId = getForkId(orderId);
        int nextId = getNextId(forkId);


        if (allArrived(forkId)) {
            onePathTime(orderId, days);
        } else {

            int maxDistance = -1;
            int difference = -1;

            while (nextId != -1) {

                int currentDistance = getDistance(nextId);
                if (currentDistance > maxDistance && currentDistance < days) {
                    maxDistance = currentDistance;
                    difference = days - currentDistance;
                }

                nextId = updateCurrentNext(nextId, days);
            }

            if (difference != -1) {
                onePathTime(orderId, difference);
            }
        }
    }

    private void decrease(int pathId, int days, int orderId) {
        String query = "select Dani, Grad from Put where IdPut = ?";

        try (PreparedStatement ps = connection.prepareStatement(query,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            ps.setInt(1, pathId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int oldDays = rs.getInt(1);
                oldDays -= days;
                if (oldDays < 0)
                    oldDays = 0;
                if (oldDays == 0) {
                    updateLocation(orderId, rs.getInt(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void updateLocation(int orderId, int location) {
        String query = "update Porudzbina set Lokacija = ? where IdPut = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, location);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Calendar getCurrentTime() {

        String query = "select Vreme from Promenljive";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
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
    public void eraseAll() {
        String query = "delete from Promenljive\n" +
                "delete from Transakcija\n" +
                "delete from Sadrzi\n" +
                "delete from Porudzbina\n" +
                "delete from Put\n" +
                "delete from Kupac\n" +
                "delete from Artikal\n" +
                "delete from Prodavnica\n" +
                "delete from Saobracajnica\n" +
                "delete from Grad";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Timestamp calendarToTimestamp(Calendar calendar) {
        if (calendar == null)
            return null;
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Calendar timestampToCalendar(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar;
    }
}

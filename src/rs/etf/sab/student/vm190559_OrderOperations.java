package rs.etf.sab.student;

import rs.etf.sab.operations.OrderOperations;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import static rs.etf.sab.student.vm190559_GeneralOperations.calendarToTimestamp;
import static rs.etf.sab.student.vm190559_GeneralOperations.timestampToCalendar;

public class vm190559_OrderOperations implements OrderOperations {

    private static final Connection connection = DB.getInstance().getConnection();

    @Override
    public int addArticle(int orderId, int articleId, int count) {

        String insertCheckQuery = "select * from Sadrzi where IdArt = ? and IdPor = ?";
        int key = -1, price;
        BigDecimal discount;

        try (PreparedStatement insertCheckPs = connection.prepareStatement(insertCheckQuery,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            insertCheckPs.setInt(1, articleId);
            insertCheckPs.setInt(2, orderId);

            ResultSet insertCheckRs = insertCheckPs.executeQuery();
            if (!insertCheckRs.next()) {

                String insertQuery = "insert into Sadrzi (Placeno, IdArt, IdPor, Broj) values (0, ?, ?, ?)";

                try (PreparedStatement insertPs = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertPs.setInt(1, articleId);
                    insertPs.setInt(2, orderId);
                    insertPs.setInt(3, count);

                    insertPs.execute();
                    ResultSet insertRs = insertPs.getGeneratedKeys();

                    if (insertRs.next())
                        key = insertRs.getInt(1);
                    else
                        return -1;

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                key = insertCheckRs.getInt(1);
                int oldCnt = insertCheckRs.getInt("Broj");

                insertCheckRs.updateInt("Broj", oldCnt + count);
                insertCheckRs.updateRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String discountQuery = "select pr.Popust, a.Cena\n" +
                "from Porudzbina p join Sadrzi s on p.IdPor = s.IdPor join Artikal a on a.IdArt = s.IdArt\n" +
                "join Prodavnica pr on pr.IdProd = a.IdProd\n" +
                "where p.IdPor = ? and a.IdArt = ?";

        try (PreparedStatement discountPs = connection.prepareStatement(discountQuery)) {

            discountPs.setInt(1, orderId);
            discountPs.setInt(2, articleId);

            ResultSet discountRs = discountPs.executeQuery();

            if (discountRs.next()) {
                discount = discountRs.getBigDecimal(1);
                discount = new BigDecimal("0.01").multiply(new BigDecimal(100).subtract(discount));
                price = discountRs.getInt(2);

                String finalPriceQuery = "select Cena, KolicinaPopusta from Porudzbina where IdPor = ?";

                try (PreparedStatement finalPricePs = connection.prepareStatement(finalPriceQuery,
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
                    finalPricePs.setInt(1, orderId);

                    ResultSet finalPriceRs = finalPricePs.executeQuery();
                    if (finalPriceRs.next()) {
                        BigDecimal oldPrice = finalPriceRs.getBigDecimal(1);
                        BigDecimal oldDiscount = finalPriceRs.getBigDecimal(2);
                        BigDecimal additionalPrice = new BigDecimal(price).multiply(new BigDecimal(count));
                        BigDecimal additionalDiscount = new BigDecimal(price * count).multiply(new BigDecimal(1).subtract(discount));

                        finalPriceRs.updateBigDecimal(1, oldPrice.add(additionalPrice));
                        finalPriceRs.updateBigDecimal(2, oldDiscount.add(additionalDiscount));
                        finalPriceRs.updateRow();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return key;
    }

    @Override
    public int removeArticle(int orderId, int articleId) {

        String query = "delete from Sadrzi where IdArt = ? and IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, articleId);
            ps.setInt(2, orderId);

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
    public List<Integer> getItems(int orderId) {
        List<Integer> ret = new ArrayList<>();

        String query = "select IdArt from Sadrzi where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, orderId);

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
    public int completeOrder(int orderId) {
        // triger kreira transkaciju, oduzima kupcu, prebacije prodavnici (prodavnicama), dodaje profit sistemu - racuna fee sistema

        callProcedure(orderId);

        int ret = 1;

        Map<Integer, List<vm190559_Edge>> graph = getGraph();
        int buyersLocation = getBuyersLocation(orderId);
        if (buyersLocation == -1 || graph.isEmpty())
            ret = -1;

        if (oneShop(orderId)) {
            int shopLocation = getShopLocation(orderId);
            if (shopLocation == -1)
                ret = -1;
            setAttributes(orderId, shopLocation);

            Map<Integer, List<vm190559_Edge>> path = graphShortestPaths(graph, shopLocation);
            int newPathKey = addNewPath(orderId, path.get(buyersLocation).get(0).distance, false, path.get(buyersLocation).get(0).destCity);
            if (newPathKey == -1)
                ret = -1;

            boolean first = true;
            for (vm190559_Edge currentDistantShop : path.get(buyersLocation)) {
                if (first) {
                    first = false;
                    continue;
                }
                newPathKey = chainPath(newPathKey, currentDistantShop.distance, false, currentDistantShop.destCity);
                if (newPathKey == -1)
                    ret = -1;
            }

        } else {
            Map<Integer, List<vm190559_Edge>> path = graphShortestPaths(graph, buyersLocation);

            int forkShopLocation = findClosestShop(path);
            if (forkShopLocation == -1)
                ret = -1;
            setAttributes(orderId, forkShopLocation);
            Map<Integer, List<vm190559_Edge>> forkPath = graphShortestPaths(graph, forkShopLocation);
            List<Integer> allOrderShops = getAllOrderShops(orderId, forkShopLocation);
            if (allOrderShops.isEmpty())
                ret = -1;

            int newPathKey = addNewPath(orderId, getDistance(forkPath.get(allOrderShops.get(0))), true, forkShopLocation);
            if (newPathKey == -1)
                ret = -1;

            boolean first = true;
            for (int currentDistantShop : allOrderShops) {
                if (first) {
                    first = false;
                    continue;
                }

                int distance = getDistance(forkPath.get(currentDistantShop));

                newPathKey = chainPath(newPathKey, distance, true, -1);
                if (newPathKey == -1)
                    ret = -1;
            }

            Collections.reverse(path.get(forkShopLocation));
            first = true;
            for (vm190559_Edge currentDistantShop : path.get(forkShopLocation)) {
                if (first) {
                    first = false;
                    continue;
                }
                newPathKey = chainPath(newPathKey, currentDistantShop.distance, false, currentDistantShop.destCity);
                if (newPathKey == -1)
                    ret = -1;
            }

        }

        createTransaction(orderId);

        return ret;
    }

    @Override
    public BigDecimal getFinalPrice(int orderId) {

        String query = "select Cena from Porudzbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal ret = rs.getBigDecimal(1);
                return ret;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getDiscountSum(int orderId) {

        String query = "select KolicinaPopusta from Porudzbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal ret = rs.getBigDecimal(1);
                ret = ret.setScale(3);
                return ret;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getState(int orderId) {

        String query = "select Status from Porudzbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public Calendar getSentTime(int orderId) {

        String query = "select VremeSlanja from Porudzbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return timestampToCalendar(rs.getTimestamp(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Calendar getRecievedTime(int orderId) {

        String query = "select VremePrijema from Porudzbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return timestampToCalendar(rs.getTimestamp(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getBuyer(int orderId) {

        String query = "select IdKup from Poruzdbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int getLocation(int orderId) {

        String query = "select Lokacija from Porudzbina where IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int ret = rs.getInt(1);
                return ret;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private class vm190559_Edge {
        int destCity, distance;

        public vm190559_Edge(int destCity, int distance) {
            this.destCity = destCity;
            this.distance = distance;
        }

        public int getCity() {
            return destCity;
        }

        public int getDistance() {
            return distance;
        }
    }

    private Map<Integer, List<vm190559_Edge>> graphShortestPaths(Map<Integer, List<vm190559_Edge>> graph, int source) {
        Map<Integer, List<vm190559_Edge>> shortestPaths = new HashMap<>();
        PriorityQueue<vm190559_Edge> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.getDistance()));
        Set<Integer> visited = new HashSet<>();

        for (int node : graph.keySet()) {
            if (node == source) {
                List<vm190559_Edge> path = new ArrayList<>();
                path.add(new vm190559_Edge(node, 0));
                shortestPaths.put(node, path);
                priorityQueue.offer(new vm190559_Edge(node, 0));
            } else {
                shortestPaths.put(node, new ArrayList<>());
            }
        }

        while (!priorityQueue.isEmpty()) {
            vm190559_Edge currentEdge = priorityQueue.poll();
            int currentCity = currentEdge.getCity();

            if (visited.contains(currentCity)) {
                continue;
            }
            visited.add(currentCity);

            List<vm190559_Edge> currentPath = shortestPaths.get(currentCity);
            List<vm190559_Edge> neighbors = graph.get(currentCity);
            if (neighbors != null) {
                for (vm190559_Edge neighbor : neighbors) {
                    int neighborCity = neighbor.getCity();
                    int edgeWeight = neighbor.getDistance();
                    int newDistance = currentEdge.getDistance() + edgeWeight;

                    List<vm190559_Edge> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);

                    List<vm190559_Edge> shortestPath = shortestPaths.get(neighborCity);
                    if (shortestPath == null) {
                        shortestPath = new ArrayList<>();
                        shortestPaths.put(neighborCity, shortestPath);
                    }

                    if (newDistance < getDistance(shortestPath)) {
                        shortestPath.clear();
                        shortestPath.addAll(newPath);
                        priorityQueue.offer(new vm190559_Edge(neighborCity, newDistance));
                    }
                }
            }
        }

        return shortestPaths;
    }

    private Map<Integer, List<vm190559_Edge>> getGraph() {
        Map<Integer, List<vm190559_Edge>> ret = new HashMap<>();
        String query = "select Grad1 from Saobracajnica";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int currentCity = rs.getInt(1);

                String conectedQuery = "select Grad1, Udaljenost from Saobracajnica where Grad2 = ? " +
                        "union select Grad2, Udaljenost from Saobracajnica where Grad1 = ?";

                try (PreparedStatement conectedPs = connection.prepareStatement(conectedQuery)) {
                    conectedPs.setInt(1, currentCity);
                    conectedPs.setInt(2, currentCity);

                    ResultSet conectedRs = conectedPs.executeQuery();
                    while (conectedRs.next()) {
                        vm190559_Edge edge = new vm190559_Edge(conectedRs.getInt(1), conectedRs.getInt(2));
                        if (ret.get(currentCity) == null)
                            ret.put(currentCity, new ArrayList<>());
                        ret.get(currentCity).add(edge);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private boolean oneShop(int orderId) {

        String query = "select count(distinct pr.IdProd)\n" +
                "from Sadrzi s join Porudzbina p on s.IdPor = p.IdPor join Artikal a on a.IdArt = s.IdArt join Prodavnica pr on pr.IdProd = a.IdProd\n" +
                "where p.IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                if (rs.getInt(1) == 1)
                    return true;
                else
                    return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void setAttributes(int orderId, int location) {
        String setCompleteQuery = "update Porudzbina set Status = ?, VremeSlanja = ?, Lokacija = ? where IdPor = ?";
        vm190559_GeneralOperations generalOperations = new vm190559_GeneralOperations();

        try (PreparedStatement setCompletePs = connection.prepareStatement(setCompleteQuery)) {
            setCompletePs.setString(1, "sent");
            setCompletePs.setTimestamp(2, calendarToTimestamp(generalOperations.getCurrentTime()));
            setCompletePs.setInt(3, location);
            setCompletePs.setInt(4, orderId);
            setCompletePs.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getBuyersLocation(int orderId) {
        String query = "select k.IdGrad\n" +
                "from Porudzbina p join Kupac k on p.IdKup = k.IdKup\n" +
                "where p.IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int getShopLocation(int orderId) {

        String query = "select pr.IdGrad\n" +
                "from Porudzbina p join Sadrzi sad on sad.IdPor = p.IdPor join Artikal a on a.IdArt = sad.IdArt join Prodavnica pr on pr.IdProd = a.IdProd\n" +
                "where p.IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int addNewPath(int orderId, int distance, boolean fork, int destCity) {
        String newPathQuery = "insert into Put (Dani, Grad, IdNextPut, Fork) values (?, ?, -1, ?)";
        String orderUpdateQuery = "update Porudzbina set IdPut = ? where IdPor = ?";
        int retKey = -1;

        try (PreparedStatement ps = connection.prepareStatement(newPathQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, distance);
            ps.setInt(2, destCity);
            if (fork)
                ps.setInt(3, 1);
            else
                ps.setInt(3, 0);

            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                try (PreparedStatement updatePs = connection.prepareStatement(orderUpdateQuery)) {
                    retKey = rs.getInt(1);
                    updatePs.setInt(1, retKey);
                    updatePs.setInt(2, orderId);
                    updatePs.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retKey;

    }

    private int chainPath(int newPathKey, int distance, boolean fork, int destCity) {
        String newPathQuery = "insert into Put (Dani, Grad, IdNextPut, Fork) values (?, ?, -1, ?)";
        String orderUpdateQuery = "update Put set IdNextPut = ? where IdPut = ?";
        int retKey = -1;

        try (PreparedStatement ps = connection.prepareStatement(newPathQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, distance);
            ps.setInt(2, destCity);
            if (fork)
                ps.setInt(3, 1);
            else
                ps.setInt(3, 0);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                try (PreparedStatement updatePs = connection.prepareStatement(orderUpdateQuery)) {
                    retKey = rs.getInt(1);
                    updatePs.setInt(1, retKey);
                    updatePs.setInt(2, newPathKey);
                    updatePs.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retKey;

    }

    private int findClosestShop(Map<Integer, List<vm190559_Edge>> path) {
        int minDistance = Integer.MAX_VALUE;
        int location = -1;

        for (int currentLocation : path.keySet()) {
            int currentDistance = getDistance(path.get(currentLocation));
            if (minDistance > currentDistance) {
                minDistance = currentDistance;
                location = currentLocation;
            }
        }

        return location;
    }

    private List<Integer> getAllOrderShops(int orderId, int forkShopLocation) {
        List<Integer> ret = new ArrayList<>();
        String query = "select pr.IdProd\n" +
                "from Sadrzi s join Porudzbina p on s.IdPor = p.IdPor join Artikal a on a.IdArt = s.IdArt join Prodavnica pr on pr.IdProd = a.IdProd\n" +
                "where p.IdPor = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int candidate = rs.getInt(1);
                if (candidate != forkShopLocation)
                    ret.add(candidate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private void createTransaction(int orderId) {

        String amountQuery = "Select Cena from Porudzbina where IdPor = ?";
        BigDecimal amount = new BigDecimal(0);

        try (PreparedStatement amountPs = connection.prepareStatement(amountQuery)) {
            amountPs.setInt(1, orderId);

            ResultSet amountRs = amountPs.executeQuery();
            if (amountRs.next())
                amount = amountRs.getBigDecimal(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertQuery = "insert into Transakcija (Vreme, Kolicina, IdPor, IdProd) values (?, ?, ?, null)";
        vm190559_GeneralOperations generalOperations = new vm190559_GeneralOperations();

        try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
            insertPs.setTimestamp(1, calendarToTimestamp(generalOperations.getCurrentTime()));
            insertPs.setBigDecimal(2, amount);
            insertPs.setInt(3, orderId);

            insertPs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void callProcedure(int orderId) {
        String procedureCall = "{call SP_FINAL_PRICE(?, ?)}";

        try (CallableStatement statement = connection.prepareCall(procedureCall)) {
            statement.setInt(1, orderId);
            statement.setBigDecimal(2, new BigDecimal(0));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getDistance(int city1, int city2) {
        String query = "select Udaljenost from Saobracajnica where Grad1 = ? and Grad2 = ?" +
                "union select Udaljenost from Saobracajnica where Grad1 = ? and Grad2 = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, city1);
            ps.setInt(2, city2);
            ps.setInt(3, city2);
            ps.setInt(4, city1);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static int getDistance(List<vm190559_Edge> path) {
        if (path == null || path.size() == 0)
            return Integer.MAX_VALUE;
        int distance = 0;
        for (vm190559_Edge edge : path) {
            distance += edge.getDistance();
        }
        return distance;
    }

}

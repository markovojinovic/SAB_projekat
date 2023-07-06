package rs.etf.sab.student;

import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        // Change this for your implementation (points will be negative if interfaces are not implemented).
        ArticleOperations articleOperations = new vm190559_ArticleOperations();
        BuyerOperations buyerOperations = new vm190559_BuyerOperations();
        CityOperations cityOperations = new vm190559_CityOperations();
        GeneralOperations generalOperations = new vm190559_GeneralOperations();
        OrderOperations orderOperations = new vm190559_OrderOperations();
        ShopOperations shopOperations = new vm190559_ShopOperations();
        TransactionOperations transactionOperations = new vm190559_TransactionOperations();

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
    }
}

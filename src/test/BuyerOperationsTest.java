/*    */ package test;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.util.List;
/*    */ import org.junit.After;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.Test;
/*    */ import rs.etf.sab.operations.BuyerOperations;
/*    */ import rs.etf.sab.operations.CityOperations;
/*    */ import rs.etf.sab.operations.GeneralOperations;
/*    */ 
/*    */ public class BuyerOperationsTest
/*    */ {
/*    */   private TestHandler testHandler;
/*    */   private GeneralOperations generalOperations;
/*    */   private CityOperations cityOperations;
/*    */   private BuyerOperations buyerOperations;
/*    */   
/*    */   @Before
/*    */   public void setUp() throws Exception {
/* 22 */     this.testHandler = TestHandler.getInstance();
/* 23 */     Assert.assertNotNull(this.testHandler);
/*    */     
/* 25 */     this.cityOperations = this.testHandler.getCityOperations();
/* 26 */     Assert.assertNotNull(this.cityOperations);
/*    */     
/* 28 */     this.generalOperations = this.testHandler.getGeneralOperations();
/* 29 */     Assert.assertNotNull(this.generalOperations);
/*    */     
/* 31 */     this.buyerOperations = this.testHandler.getBuyerOperations();
/* 32 */     Assert.assertNotNull(this.buyerOperations);
/*    */     
/* 34 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @After
/*    */   public void tearDown() throws Exception {
/* 39 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void insertBuyer() {
/* 44 */     int cityId = this.cityOperations.createCity("Kragujevac");
/* 45 */     Assert.assertNotEquals(-1L, cityId);
/*    */     
/* 47 */     int buyerId = this.buyerOperations.createBuyer("Pera", cityId);
/* 48 */     Assert.assertNotEquals(-1L, buyerId);
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void changeCity() {
/* 53 */     int cityId1 = this.cityOperations.createCity("Kragujevac");
/* 54 */     int cityId2 = this.cityOperations.createCity("Beograd");
/* 55 */     int buyerId = this.buyerOperations.createBuyer("Lazar", cityId1);
/* 56 */     this.buyerOperations.setCity(buyerId, cityId2);
/*    */     
/* 58 */     int cityId = this.buyerOperations.getCity(buyerId);
/* 59 */     Assert.assertEquals(cityId2, cityId);
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void credit() {
/* 64 */     int cityId = this.cityOperations.createCity("Kragujevac");
/* 65 */     int buyerId = this.buyerOperations.createBuyer("Pera", cityId);
/*    */     
/* 67 */     BigDecimal credit1 = (new BigDecimal("1000.000")).setScale(3);
/*    */     
/* 69 */     BigDecimal creditReturned = this.buyerOperations.increaseCredit(buyerId, credit1);
/* 70 */     Assert.assertEquals(credit1, creditReturned);
/*    */     
/* 72 */     BigDecimal credit2 = new BigDecimal("500");
/* 73 */     this.buyerOperations.increaseCredit(buyerId, credit2).setScale(3);
/*    */     
/* 75 */     creditReturned = this.buyerOperations.getCredit(buyerId);
/* 76 */     Assert.assertEquals(credit1.add(credit2).setScale(3), creditReturned);
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void orders() {
/* 81 */     int cityId = this.cityOperations.createCity("Kragujevac");
/* 82 */     int buyerId = this.buyerOperations.createBuyer("Pera", cityId);
/*    */     
/* 84 */     int orderId1 = this.buyerOperations.createOrder(buyerId);
/* 85 */     int orderId2 = this.buyerOperations.createOrder(buyerId);
/* 86 */     Assert.assertNotEquals(-1L, orderId1);
/* 87 */     Assert.assertNotEquals(-1L, orderId2);
/*    */     
/* 89 */     List<Integer> orders = this.buyerOperations.getOrders(buyerId);
/* 90 */     Assert.assertEquals(2L, orders.size());
/* 91 */     Assert.assertTrue((orders.contains(Integer.valueOf(orderId1)) && orders.contains(Integer.valueOf(orderId2))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Stevo\Desktop\jd-gui-windows-1.6.6\SAB_project_2223.jar!\rs\etf\sab\tests\BuyerOperationsTest.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
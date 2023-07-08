/*    */ package test;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.junit.After;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.Test;
/*    */ import rs.etf.sab.operations.CityOperations;
/*    */ import rs.etf.sab.operations.GeneralOperations;
/*    */ import rs.etf.sab.operations.ShopOperations;
/*    */ 
/*    */ 
/*    */ public class CityOperationsTest
/*    */ {
/*    */   private TestHandler testHandler;
/*    */   private GeneralOperations generalOperations;
/*    */   private CityOperations cityOperations;
/*    */   private ShopOperations shopOperations;
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
/*    */     
/* 32 */     this.shopOperations = this.testHandler.getShopOperations();
/* 33 */     Assert.assertNotNull(this.shopOperations);
/*    */     
/* 35 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @After
/*    */   public void tearDown() throws Exception {
/* 40 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void createCity() {
/* 45 */     int cityVranje = this.cityOperations.createCity("Vranje");
/* 46 */     Assert.assertEquals(1L, this.cityOperations.getCities().size());
/* 47 */     Assert.assertEquals(cityVranje, ((Integer)this.cityOperations.getCities().get(0)).intValue());
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void insertShops() {
/* 52 */     int cityId = this.cityOperations.createCity("Vranje");
/*    */     
/* 54 */     int shopId1 = this.shopOperations.createShop("Gigatron", "Vranje");
/* 55 */     int shopId2 = this.shopOperations.createShop("Teranova", "Vranje");
/*    */     
/* 57 */     List<Integer> shops = this.cityOperations.getShops(cityId);
/* 58 */     Assert.assertEquals(2L, shops.size());
/* 59 */     Assert.assertTrue((shops.contains(Integer.valueOf(shopId1)) && shops.contains(Integer.valueOf(shopId2))));
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void connectCities() {
/* 64 */     int cityVranje = this.cityOperations.createCity("Vranje");
/* 65 */     int cityLeskovac = this.cityOperations.createCity("Leskovac");
/* 66 */     int cityNis = this.cityOperations.createCity("Nis");
/*    */     
/* 68 */     Assert.assertNotEquals(-1L, cityLeskovac);
/* 69 */     Assert.assertNotEquals(-1L, cityVranje);
/* 70 */     Assert.assertNotEquals(-1L, cityNis);
/*    */     
/* 72 */     this.cityOperations.connectCities(cityNis, cityVranje, 50);
/* 73 */     this.cityOperations.connectCities(cityVranje, cityLeskovac, 70);
/*    */     
/* 75 */     List<Integer> connectedCities = this.cityOperations.getConnectedCities(cityVranje);
/* 76 */     Assert.assertEquals(2L, connectedCities.size());
/* 77 */     Assert.assertTrue((connectedCities.contains(Integer.valueOf(cityLeskovac)) && connectedCities.contains(Integer.valueOf(cityNis))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Stevo\Desktop\jd-gui-windows-1.6.6\SAB_project_2223.jar!\rs\etf\sab\tests\CityOperationsTest.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
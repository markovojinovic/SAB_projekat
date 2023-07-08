/*    */ package test;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.junit.After;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.Test;
/*    */ import rs.etf.sab.operations.ArticleOperations;
/*    */ import rs.etf.sab.operations.CityOperations;
/*    */ import rs.etf.sab.operations.GeneralOperations;
/*    */ import rs.etf.sab.operations.ShopOperations;
/*    */ 
/*    */ public class ShopOperationsTest
/*    */ {
/*    */   private TestHandler testHandler;
/*    */   private GeneralOperations generalOperations;
/*    */   private ShopOperations shopOperations;
/*    */   private CityOperations cityOperations;
/*    */   private ArticleOperations articleOperations;
/*    */   
/*    */   @Before
/*    */   public void setUp() throws Exception {
/* 23 */     this.testHandler = TestHandler.getInstance();
/* 24 */     Assert.assertNotNull(this.testHandler);
/*    */     
/* 26 */     this.shopOperations = this.testHandler.getShopOperations();
/* 27 */     Assert.assertNotNull(this.shopOperations);
/*    */     
/* 29 */     this.cityOperations = this.testHandler.getCityOperations();
/* 30 */     Assert.assertNotNull(this.cityOperations);
/*    */     
/* 32 */     this.articleOperations = this.testHandler.getArticleOperations();
/* 33 */     Assert.assertNotNull(this.articleOperations);
/*    */     
/* 35 */     this.generalOperations = this.testHandler.getGeneralOperations();
/* 36 */     Assert.assertNotNull(this.generalOperations);
/*    */     
/* 38 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @After
/*    */   public void tearDown() throws Exception {
/* 43 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void createShop() {
/* 48 */     int cityId = this.cityOperations.createCity("Kragujevac");
/* 49 */     Assert.assertNotEquals(-1L, cityId);
/* 50 */     int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
/* 51 */     Assert.assertEquals(shopId, ((Integer)this.cityOperations.getShops(cityId).get(0)).intValue());
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void setCity() {
/* 56 */     this.cityOperations.createCity("Kragujevac");
/* 57 */     int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
/* 58 */     int cityId2 = this.cityOperations.createCity("Subotica");
/*    */     
/* 60 */     this.shopOperations.setCity(shopId, "Subotica");
/* 61 */     Assert.assertEquals(shopId, ((Integer)this.cityOperations.getShops(cityId2).get(0)).intValue());
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void discount() {
/* 66 */     this.cityOperations.createCity("Kragujevac");
/* 67 */     int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
/* 68 */     this.shopOperations.setDiscount(shopId, 20);
/* 69 */     Assert.assertEquals(20L, this.shopOperations.getDiscount(shopId));
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void articles() {
/* 74 */     this.cityOperations.createCity("Kragujevac");
/* 75 */     int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
/*    */     
/* 77 */     int articleId = this.articleOperations.createArticle(shopId, "Olovka", 10);
/* 78 */     Assert.assertNotEquals(-1L, articleId);
/*    */     
/* 80 */     int articleId2 = this.articleOperations.createArticle(shopId, "Gumica", 5);
/* 81 */     Assert.assertNotEquals(-1L, articleId2);
/*    */     
/* 83 */     this.shopOperations.increaseArticleCount(articleId, 5);
/* 84 */     this.shopOperations.increaseArticleCount(articleId, 2);
/* 85 */     int articleCount = this.shopOperations.getArticleCount(articleId);
/* 86 */     Assert.assertEquals(7L, articleCount);
/*    */     
/* 88 */     List<Integer> articles = this.shopOperations.getArticles(shopId);
/* 89 */     Assert.assertEquals(2L, articles.size());
/* 90 */     Assert.assertTrue((articles.contains(Integer.valueOf(articleId)) && articles.contains(Integer.valueOf(articleId2))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Stevo\Desktop\jd-gui-windows-1.6.6\SAB_project_2223.jar!\rs\etf\sab\tests\ShopOperationsTest.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
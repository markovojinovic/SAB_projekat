/*    */ package test;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import rs.etf.sab.operations.*;
/*    */
/*    */

/*    */
/*    */ public class TestHandler {
/*  8 */   private static TestHandler testHandler = null;
/*    */   
/*    */   private ArticleOperations articleOperations;
/*    */   private BuyerOperations buyerOperations;
/*    */   private CityOperations cityOperations;
/*    */   private GeneralOperations generalOperations;
/*    */   private OrderOperations orderOperations;
/*    */   private ShopOperations shopOperations;
/*    */   private TransactionOperations transactionOperations;
/*    */   
/*    */   public TestHandler(@NotNull ArticleOperations articleOperations, @NotNull BuyerOperations buyerOperations, @NotNull CityOperations cityOperations, @NotNull GeneralOperations generalOperations, @NotNull OrderOperations orderOperations, @NotNull ShopOperations shopOperations, @NotNull TransactionOperations transactionOperations) {
/* 19 */     this.articleOperations = articleOperations;
/* 20 */     this.buyerOperations = buyerOperations;
/* 21 */     this.cityOperations = cityOperations;
/* 22 */     this.generalOperations = generalOperations;
/* 23 */     this.orderOperations = orderOperations;
/* 24 */     this.shopOperations = shopOperations;
/* 25 */     this.transactionOperations = transactionOperations;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void createInstance(@NotNull ArticleOperations articleOperations, @NotNull BuyerOperations buyerOperations, @NotNull CityOperations cityOperations, @NotNull GeneralOperations generalOperations, @NotNull OrderOperations orderOperations, @NotNull ShopOperations shopOperations, @NotNull TransactionOperations transactionOperations) {
/* 30 */     testHandler = new TestHandler(articleOperations, buyerOperations, cityOperations, generalOperations, orderOperations, shopOperations, transactionOperations);
/*    */   }
/*    */ 
/*    */   
/*    */   static TestHandler getInstance() {
/* 35 */     return testHandler;
/*    */   }
/*    */   
/*    */   public ArticleOperations getArticleOperations() {
/* 39 */     return this.articleOperations;
/*    */   }
/*    */   
/*    */   public BuyerOperations getBuyerOperations() {
/* 43 */     return this.buyerOperations;
/*    */   }
/*    */   
/*    */   public CityOperations getCityOperations() {
/* 47 */     return this.cityOperations;
/*    */   }
/*    */   
/*    */   public GeneralOperations getGeneralOperations() {
/* 51 */     return this.generalOperations;
/*    */   }
/*    */   
/*    */   public OrderOperations getOrderOperations() {
/* 55 */     return this.orderOperations;
/*    */   }
/*    */   
/*    */   public ShopOperations getShopOperations() {
/* 59 */     return this.shopOperations;
/*    */   }
/*    */   
/*    */   public TransactionOperations getTransactionOperations() {
/* 63 */     return this.transactionOperations;
/*    */   }
/*    */ }


/* Location:              C:\Users\Stevo\Desktop\jd-gui-windows-1.6.6\SAB_project_2223.jar!\rs\etf\sab\tests\TestHandler.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
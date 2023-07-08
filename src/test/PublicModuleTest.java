/*     */ package test;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Calendar;
/*     */ import org.junit.After;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Before;
/*     */ import org.junit.Test;
/*     */ import rs.etf.sab.operations.ArticleOperations;
/*     */ import rs.etf.sab.operations.BuyerOperations;
/*     */ import rs.etf.sab.operations.CityOperations;
/*     */ import rs.etf.sab.operations.GeneralOperations;
/*     */ import rs.etf.sab.operations.OrderOperations;
/*     */ import rs.etf.sab.operations.ShopOperations;
/*     */ import rs.etf.sab.operations.TransactionOperations;
/*     */ 
/*     */ public class PublicModuleTest {
/*     */   private TestHandler testHandler;
/*     */   private GeneralOperations generalOperations;
/*     */   private ShopOperations shopOperations;
/*     */   private CityOperations cityOperations;
/*     */   
/*     */   @Before
/*     */   public void setUp() throws Exception {
/*  25 */     this.testHandler = TestHandler.getInstance();
/*  26 */     Assert.assertNotNull(this.testHandler);
/*     */     
/*  28 */     this.shopOperations = this.testHandler.getShopOperations();
/*  29 */     Assert.assertNotNull(this.shopOperations);
/*     */     
/*  31 */     this.cityOperations = this.testHandler.getCityOperations();
/*  32 */     Assert.assertNotNull(this.cityOperations);
/*     */     
/*  34 */     this.articleOperations = this.testHandler.getArticleOperations();
/*  35 */     Assert.assertNotNull(this.articleOperations);
/*     */     
/*  37 */     this.buyerOperations = this.testHandler.getBuyerOperations();
/*  38 */     Assert.assertNotNull(this.buyerOperations);
/*     */     
/*  40 */     this.orderOperations = this.testHandler.getOrderOperations();
/*  41 */     Assert.assertNotNull(this.orderOperations);
/*     */     
/*  43 */     this.transactionOperations = this.testHandler.getTransactionOperations();
/*  44 */     Assert.assertNotNull(this.transactionOperations);
/*     */     
/*  46 */     this.generalOperations = this.testHandler.getGeneralOperations();
/*  47 */     Assert.assertNotNull(this.generalOperations);
/*     */     
/*  49 */     this.generalOperations.eraseAll();
/*     */   }
/*     */   private ArticleOperations articleOperations; private BuyerOperations buyerOperations; private OrderOperations orderOperations; private TransactionOperations transactionOperations;
/*     */   @After
/*     */   public void tearDown() throws Exception {
/*  54 */     this.generalOperations.eraseAll();
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void test() {
/*  59 */     Calendar initialTime = Calendar.getInstance();
/*  60 */     initialTime.clear();
/*  61 */     initialTime.set(2018, 0, 1);
/*  62 */     this.generalOperations.setInitialTime(initialTime);
/*  63 */     Calendar receivedTime = Calendar.getInstance();
/*  64 */     receivedTime.clear();
/*  65 */     receivedTime.set(2018, 0, 22);
/*     */ 
/*     */     
/*  68 */     int cityB = this.cityOperations.createCity("B");
/*  69 */     int cityC1 = this.cityOperations.createCity("C1");
/*  70 */     int cityA = this.cityOperations.createCity("A");
/*  71 */     int cityC2 = this.cityOperations.createCity("C2");
/*  72 */     int cityC3 = this.cityOperations.createCity("C3");
/*  73 */     int cityC4 = this.cityOperations.createCity("C4");
/*  74 */     int cityC5 = this.cityOperations.createCity("C5");
/*     */     
/*  76 */     this.cityOperations.connectCities(cityB, cityC1, 8);
/*  77 */     this.cityOperations.connectCities(cityC1, cityA, 10);
/*  78 */     this.cityOperations.connectCities(cityA, cityC2, 3);
/*  79 */     this.cityOperations.connectCities(cityC2, cityC3, 2);
/*  80 */     this.cityOperations.connectCities(cityC3, cityC4, 1);
/*  81 */     this.cityOperations.connectCities(cityC4, cityA, 3);
/*  82 */     this.cityOperations.connectCities(cityA, cityC5, 15);
/*  83 */     this.cityOperations.connectCities(cityC5, cityB, 2);
/*     */ 
/*     */     
/*  86 */     int shopA = this.shopOperations.createShop("shopA", "A");
/*  87 */     int shopC2 = this.shopOperations.createShop("shopC2", "C2");
/*  88 */     int shopC3 = this.shopOperations.createShop("shopC3", "C3");
/*     */     
/*  90 */     this.shopOperations.setDiscount(shopA, 20);
/*  91 */     this.shopOperations.setDiscount(shopC2, 50);
/*     */     
/*  93 */     int laptop = this.articleOperations.createArticle(shopA, "laptop", 1000);
/*  94 */     int monitor = this.articleOperations.createArticle(shopC2, "monitor", 200);
/*  95 */     int stolica = this.articleOperations.createArticle(shopC3, "stolica", 100);
/*  96 */     int sto = this.articleOperations.createArticle(shopC3, "sto", 200);
/*     */     
/*  98 */     this.shopOperations.increaseArticleCount(laptop, 10);
/*  99 */     this.shopOperations.increaseArticleCount(monitor, 10);
/* 100 */     this.shopOperations.increaseArticleCount(stolica, 10);
/* 101 */     this.shopOperations.increaseArticleCount(sto, 10);
/*     */     
/* 103 */     int buyer = this.buyerOperations.createBuyer("kupac", cityB);
/* 104 */     this.buyerOperations.increaseCredit(buyer, new BigDecimal("20000"));
/* 105 */     int order = this.buyerOperations.createOrder(buyer);
/*     */     
/* 107 */     this.orderOperations.addArticle(order, laptop, 5);
/* 108 */     this.orderOperations.addArticle(order, monitor, 4);
/* 109 */     this.orderOperations.addArticle(order, stolica, 10);
/* 110 */     this.orderOperations.addArticle(order, sto, 4);
/*     */     
/* 112 */     Assert.assertNull(this.orderOperations.getSentTime(order));
/* 113 */     Assert.assertTrue("created".equals(this.orderOperations.getState(order)));
/* 114 */     this.orderOperations.completeOrder(order);
/* 115 */     Assert.assertTrue("sent".equals(this.orderOperations.getState(order)));
/*     */     
/* 117 */     int buyerTransactionId = ((Integer)this.transactionOperations.getTransationsForBuyer(buyer).get(0)).intValue();
/* 118 */     Assert.assertEquals(initialTime, this.transactionOperations.getTimeOfExecution(buyerTransactionId));
/*     */     
/* 120 */     Assert.assertNull(this.transactionOperations.getTransationsForShop(shopA));
/*     */ 
/*     */     
/* 123 */     BigDecimal shopAAmount = (new BigDecimal("5")).multiply(new BigDecimal("1000")).setScale(3);
/* 124 */     BigDecimal shopAAmountWithDiscount = (new BigDecimal("0.8")).multiply(shopAAmount).setScale(3);
/* 125 */     BigDecimal shopC2Amount = (new BigDecimal("4")).multiply(new BigDecimal("200")).setScale(3);
/* 126 */     BigDecimal shopC2AmountWithDiscount = (new BigDecimal("0.5")).multiply(shopC2Amount).setScale(3);
/*     */     
/* 128 */     BigDecimal shopC3Amount = (new BigDecimal("10")).multiply(new BigDecimal("100")).add((new BigDecimal("4")).multiply(new BigDecimal("200"))).setScale(3);
/* 129 */     BigDecimal shopC3AmountWithDiscount = shopC3Amount;
/*     */     
/* 131 */     BigDecimal amountWithoutDiscounts = shopAAmount.add(shopC2Amount).add(shopC3Amount).setScale(3);
/* 132 */     BigDecimal amountWithDiscounts = shopAAmountWithDiscount.add(shopC2AmountWithDiscount).add(shopC3AmountWithDiscount).setScale(3);
/*     */     
/* 134 */     BigDecimal systemProfit = amountWithDiscounts.multiply(new BigDecimal("0.05")).setScale(3);
/* 135 */     BigDecimal shopAAmountReal = shopAAmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
/* 136 */     BigDecimal shopC2AmountReal = shopC2AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
/* 137 */     BigDecimal shopC3AmountReal = shopC3AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
/*     */ 
/*     */     
/* 140 */     Assert.assertEquals(amountWithDiscounts, this.orderOperations.getFinalPrice(order));
/* 141 */     Assert.assertEquals(amountWithoutDiscounts.subtract(amountWithDiscounts), this.orderOperations.getDiscountSum(order));
/*     */     
/* 143 */     Assert.assertEquals(amountWithDiscounts, this.transactionOperations.getBuyerTransactionsAmmount(buyer));
/* 144 */     Assert.assertEquals(this.transactionOperations.getShopTransactionsAmmount(shopA), (new BigDecimal("0")).setScale(3));
/* 145 */     Assert.assertEquals(this.transactionOperations.getShopTransactionsAmmount(shopC2), (new BigDecimal("0")).setScale(3));
/* 146 */     Assert.assertEquals(this.transactionOperations.getShopTransactionsAmmount(shopC3), (new BigDecimal("0")).setScale(3));
/* 147 */     Assert.assertEquals((new BigDecimal("0")).setScale(3), this.transactionOperations.getSystemProfit());
/*     */     
/* 149 */     this.generalOperations.time(2);
/* 150 */     Assert.assertEquals(initialTime, this.orderOperations.getSentTime(order));
/* 151 */     Assert.assertNull(this.orderOperations.getRecievedTime(order));
/* 152 */     Assert.assertEquals(this.orderOperations.getLocation(order), cityA);
/*     */     
/* 154 */     this.generalOperations.time(9);
/* 155 */     Assert.assertEquals(this.orderOperations.getLocation(order), cityA);
/*     */     
/* 157 */     this.generalOperations.time(8);
/* 158 */     Assert.assertEquals(this.orderOperations.getLocation(order), cityC5);
/*     */     
/* 160 */     this.generalOperations.time(5);
/* 161 */     Assert.assertEquals(this.orderOperations.getLocation(order), cityB);
/* 162 */     Assert.assertEquals(receivedTime, this.orderOperations.getRecievedTime(order));
/*     */     
/* 164 */     Assert.assertEquals(shopAAmountReal, this.transactionOperations.getShopTransactionsAmmount(shopA));
/* 165 */     Assert.assertEquals(shopC2AmountReal, this.transactionOperations.getShopTransactionsAmmount(shopC2));
/* 166 */     Assert.assertEquals(shopC3AmountReal, this.transactionOperations.getShopTransactionsAmmount(shopC3));
/* 167 */     Assert.assertEquals(systemProfit, this.transactionOperations.getSystemProfit());
/*     */     
/* 169 */     int shopATransactionId = this.transactionOperations.getTransactionForShopAndOrder(order, shopA);
/* 170 */     Assert.assertNotEquals(-1L, shopATransactionId);
/* 171 */     Assert.assertEquals(receivedTime, this.transactionOperations.getTimeOfExecution(shopATransactionId));
/*     */   }
/*     */ }


/* Location:              C:\Users\Stevo\Desktop\jd-gui-windows-1.6.6\SAB_project_2223.jar!\rs\etf\sab\tests\PublicModuleTest.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
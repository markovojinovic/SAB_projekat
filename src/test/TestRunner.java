/*     */ package test;
/*     */ 
/*     */ import org.junit.runner.JUnitCore;
/*     */ import org.junit.runner.Request;
/*     */ import org.junit.runner.Result;
/*     */ 
/*     */ 
/*     */ public final class TestRunner
/*     */ {
/*     */   private static final int MAX_POINTS_ON_PUBLIC_TEST = 10;
/*  11 */   private static final Class[] UNIT_TEST_CLASSES = new Class[] { BuyerOperationsTest.class, CityOperationsTest.class, GeneralOperationsTest.class, ShopOperationsTest.class };
/*     */ 
/*     */ 
/*     */   
/*  15 */   private static final Class[] UNIT_TEST_CLASSES_PRIVATE = new Class[0];
/*     */ 
/*     */   
/*  18 */   private static final Class[] MODULE_TEST_CLASSES = new Class[] { PublicModuleTest.class };
/*  19 */   private static final Class[] MODULE_TEST_CLASSES_PRIVATE = new Class[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private static double runUnitTestsPublic() {
/*  24 */     int numberOfSuccessfulCases = 0;
/*  25 */     int numberOfAllCases = 0;
/*  26 */     double points = 0.0D;
/*  27 */     JUnitCore jUnitCore = new JUnitCore();
/*     */     
/*  29 */     for (Class testClass : UNIT_TEST_CLASSES) {
/*  30 */       System.out.println("\n" + testClass.getName());
/*     */       
/*  32 */       Request request = Request.aClass(testClass);
/*  33 */       Result result = jUnitCore.run(request);
/*     */       
/*  35 */       numberOfAllCases = result.getRunCount();
/*  36 */       numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
/*  37 */       if (numberOfSuccessfulCases < 0)
/*  38 */         numberOfSuccessfulCases = 0; 
/*  39 */       System.out.println("Successful: " + numberOfSuccessfulCases);
/*  40 */       System.out.println("All: " + numberOfAllCases);
/*  41 */       double points_curr = numberOfSuccessfulCases * 6.0D / numberOfAllCases / UNIT_TEST_CLASSES.length;
/*  42 */       System.out.println("Points: " + points_curr);
/*  43 */       points += points_curr;
/*     */     } 
/*     */ 
/*     */     
/*  47 */     return points;
/*     */   }
/*     */   
/*     */   private static double runModuleTestsPublic() {
/*  51 */     int numberOfSuccessfulCases = 0;
/*  52 */     int numberOfAllCases = 0;
/*  53 */     double points = 0.0D;
/*  54 */     JUnitCore jUnitCore = new JUnitCore();
/*     */     
/*  56 */     for (Class testClass : MODULE_TEST_CLASSES) {
/*  57 */       System.out.println("\n" + testClass.getName());
/*     */       
/*  59 */       Request request = Request.aClass(testClass);
/*  60 */       Result result = jUnitCore.run(request);
/*     */       
/*  62 */       numberOfAllCases = result.getRunCount();
/*  63 */       numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
/*  64 */       if (numberOfSuccessfulCases < 0)
/*  65 */         numberOfSuccessfulCases = 0; 
/*  66 */       System.out.println("Successful: " + numberOfSuccessfulCases);
/*  67 */       System.out.println("All: " + numberOfAllCases);
/*  68 */       double points_curr = (numberOfSuccessfulCases / MODULE_TEST_CLASSES.length * 4);
/*  69 */       System.out.println("Points: " + points_curr);
/*  70 */       points += points_curr;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return points;
/*     */   }
/*     */   
/*     */   private static double runPublic() {
/*  78 */     double res = 0.0D;
/*  79 */     res += runUnitTestsPublic();
/*  80 */     res += runModuleTestsPublic();
/*  81 */     return res;
/*     */   }
/*     */   
/*     */   private static double runUnitTestsPrivate() {
/*  85 */     int numberOfSuccessfulCases = 0;
/*  86 */     int numberOfAllCases = 0;
/*  87 */     double points = 0.0D;
/*  88 */     JUnitCore jUnitCore = new JUnitCore();
/*     */     
/*  90 */     for (Class testClass : UNIT_TEST_CLASSES_PRIVATE) {
/*  91 */       System.out.println("\n" + testClass.getName());
/*     */       
/*  93 */       Request request = Request.aClass(testClass);
/*  94 */       Result result = jUnitCore.run(request);
/*     */       
/*  96 */       numberOfAllCases = result.getRunCount();
/*  97 */       numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
/*  98 */       if (numberOfSuccessfulCases < 0)
/*  99 */         numberOfSuccessfulCases = 0; 
/* 100 */       System.out.println("Successful: " + numberOfSuccessfulCases);
/* 101 */       System.out.println("All: " + numberOfAllCases);
/* 102 */       double points_curr = numberOfSuccessfulCases * 2.0D / numberOfAllCases / UNIT_TEST_CLASSES_PRIVATE.length;
/* 103 */       System.out.println("Points: " + points_curr);
/* 104 */       points += points_curr;
/*     */     } 
/*     */     
/* 107 */     return points;
/*     */   }
/*     */   
/*     */   private static double runModuleTestsPrivate() {
/* 111 */     int numberOfSuccessfulCases = 0;
/* 112 */     int numberOfAllCases = 0;
/* 113 */     double points = 0.0D;
/* 114 */     JUnitCore jUnitCore = new JUnitCore();
/*     */     
/* 116 */     for (Class testClass : MODULE_TEST_CLASSES_PRIVATE) {
/* 117 */       System.out.println("\n" + testClass.getName());
/*     */       
/* 119 */       Request request = Request.aClass(testClass);
/* 120 */       Result result = jUnitCore.run(request);
/*     */       
/* 122 */       numberOfAllCases = result.getRunCount();
/* 123 */       numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
/* 124 */       if (numberOfSuccessfulCases < 0)
/* 125 */         numberOfSuccessfulCases = 0; 
/* 126 */       System.out.println("Successful:" + numberOfSuccessfulCases);
/* 127 */       System.out.println("All:" + numberOfAllCases);
/* 128 */       double points_curr = numberOfSuccessfulCases * 8.0D / numberOfAllCases / MODULE_TEST_CLASSES_PRIVATE.length;
/* 129 */       System.out.println("Points: " + points_curr);
/* 130 */       points += points_curr;
/*     */     } 
/*     */     
/* 133 */     return points;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static double runPrivate() {
/* 139 */     double res = 0.0D;
/* 140 */     res += runUnitTestsPrivate();
/* 141 */     res += runModuleTestsPrivate();
/* 142 */     return res;
/*     */   }
/*     */   public static void runTests() {
/* 145 */     double resultsPublic = runPublic();
/* 146 */     System.out.println("Points won on public tests is: " + resultsPublic + " out of 10");
/*     */   }
/*     */ }


/* Location:              C:\Users\Stevo\Desktop\jd-gui-windows-1.6.6\SAB_project_2223.jar!\rs\etf\sab\tests\TestRunner.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package test;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import org.junit.After;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.Test;
/*    */ import rs.etf.sab.operations.GeneralOperations;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GeneralOperationsTest
/*    */ {
/*    */   private TestHandler testHandler;
/*    */   private GeneralOperations generalOperations;
/*    */   
/*    */   @Before
/*    */   public void setUp() throws Exception {
/* 19 */     this.testHandler = TestHandler.getInstance();
/* 20 */     Assert.assertNotNull(this.testHandler);
/*    */     
/* 22 */     this.generalOperations = this.testHandler.getGeneralOperations();
/* 23 */     Assert.assertNotNull(this.generalOperations);
/*    */     
/* 25 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @After
/*    */   public void tearDown() throws Exception {
/* 30 */     this.generalOperations.eraseAll();
/*    */   }
/*    */   
/*    */   @Test
/*    */   public void general() {
/* 35 */     Calendar time = Calendar.getInstance();
/* 36 */     time.clear();
/* 37 */     time.set(2018, 0, 1);
/* 38 */     this.generalOperations.setInitialTime(time);
/*    */     
/* 40 */     Calendar currentTime = this.generalOperations.getCurrentTime();
/* 41 */     Assert.assertEquals(time, currentTime);
/*    */     
/* 43 */     this.generalOperations.time(40);
/* 44 */     currentTime = this.generalOperations.getCurrentTime();
/* 45 */     Calendar newTime = Calendar.getInstance();
/* 46 */     newTime.clear();
/* 47 */     newTime.set(2018, 1, 10);
/* 48 */     Assert.assertEquals(currentTime, newTime);
/*    */   }
/*    */ }


/* Location:              C:\Users\Stevo\Desktop\jd-gui-windows-1.6.6\SAB_project_2223.jar!\rs\etf\sab\tests\GeneralOperationsTest.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
package aggressor;

import common.CommonUtils;
import java.util.LinkedList;
import java.util.List;

public class TeamServerProps extends PropsFile {
   protected static final TeamServerProps propsFile = new TeamServerProps();

   public static TeamServerProps getPropsFile() {
      propsFile.load(false);
      return propsFile;
   }

   protected String myFilePath() {
      return ".";
   }

   protected String myFileName() {
      return "TeamServer.prop";
   }

   protected String myTitle() {
      return "Cobalt Strike Team Server Properties";
   }

   protected String myName() {
      return "Team Server Properties";
   }

   protected String resourceFileName() {
      return "TeamServer.prop";
   }

   public static void main(String[] var0) {
      TeamServerProps var1 = getPropsFile();
      var1.testPropertiesFile();
      var1.testIsSet();
      var1.testGetString();
      var1.testGetLongNumber();
      var1.testGetIntNumber();
      var1.testGetList();
   }

   protected void testGetList() {
      this.testPrintSectionHead("test getList");
      List var1 = null;
      var1 = this.getList("missing.list");
      if (var1.size() != 0) {
         this.A("missing.list is not empty");
      } else {
         CommonUtils.print_info("missing list test passed");
      }

      LinkedList var2 = new LinkedList();
      this.setList("empty.list", var2);
      var1 = this.getList("empty.list");
      if (var1.size() != 0) {
         this.A("empty.list is not empty");
      } else {
         CommonUtils.print_info("empty list test passed");
      }

      LinkedList var3 = new LinkedList();
      var3.add("one");
      var3.add("two");
      var3.add("three");
      this.setList("test.list", var3);
      var1 = this.getList("test.list");
      if (var1.size() != 3) {
         this.A("test.list does not have 3 items");
      } else {
         CommonUtils.print_info("list test passed");
      }

   }

   protected void testGetIntNumber() {
      this.testPrintSectionHead("test getIntNumber");
      String var1 = "test.int";
      byte var2 = 123;
      short var3 = 456;
      this.A(var1, var2, var2);
      this.set(var1, String.valueOf(var3));
      this.A(var1, var2, var3);
      this.remove(var1);
      this.A(var1, var2, var2);
      this.set(var1, "not-a-number");
      this.A(var1, var2, var2);
      this.set(var1, " 321 ");
      this.A(var1, var2, 321);
      long var4 = 2147483647L;
      ++var4;
      this.set(var1, String.valueOf(var4));
      this.A(var1, var2, var2);
      long var6 = -2147483648L;
      --var6;
      this.set(var1, String.valueOf(var6));
      this.A(var1, var2, var2);
   }

   private void A(String var1, int var2, int var3) {
      long var4 = (long)this.getIntNumber(var1, var2);
      String var6;
      if ((long)var3 == var4) {
         var6 = "getIntegerNumber(" + var1 + ", " + var2 + ") == " + var3 + " passed";
         CommonUtils.print_info(var6);
      } else {
         var6 = "getIntegerNumber(" + var1 + ", " + var2 + ") == " + var3 + " failed";
         CommonUtils.print_error(var6);
         this.A(var6);
      }

   }

   protected void testGetLongNumber() {
      this.testPrintSectionHead("test getLongNumber");
      String var1 = "test.long";
      long var2 = 123L;
      long var4 = 456L;
      this.A(var1, var2, var2);
      this.set(var1, String.valueOf(var4));
      this.A(var1, var2, var4);
      this.remove(var1);
      this.A(var1, var2, var2);
      this.set(var1, "not-a-number");
      this.A(var1, var2, var2);
      this.set(var1, " 987654321 ");
      this.A(var1, var2, 987654321L);
   }

   private void A(String var1, long var2, long var4) {
      long var6 = this.getLongNumber(var1, var2);
      String var8;
      if (var4 == var6) {
         var8 = "getLongNumber(" + var1 + ", " + var2 + ") == " + var4 + " passed";
         CommonUtils.print_info(var8);
      } else {
         var8 = "getLongNumber(" + var1 + ", " + var2 + ") == " + var4 + " failed";
         CommonUtils.print_error(var8);
         this.A(var8);
      }

   }

   protected void testGetString() {
      this.testPrintSectionHead("test getString");
      String var1 = "test.string";
      String var2 = "dft-val";
      String var3 = "actual-val";
      this.A(var1, var2, var2);
      this.set(var1, var3);
      this.A(var1, var2, var3);
      this.remove(var1);
      this.A(var1, var2, var2);
   }

   private void A(String var1, String var2, String var3) {
      String var4 = this.getString(var1, var2);
      String var5;
      if (var3.equals(var4)) {
         var5 = "getString(" + var1 + ", " + var2 + ") == " + var3 + " passed";
         CommonUtils.print_info(var5);
      } else {
         var5 = "getString(" + var1 + ", " + var2 + ") == " + var3 + " failed";
         CommonUtils.print_error(var5);
         this.A(var5);
      }

   }

   protected void testIsSet() {
      this.testPrintSectionHead("test isSet");
      this.A("missing.boolean", true, true);
      this.A("missing.boolean", false, false);
      this.set("valid.true.boolean", "true");
      this.A("valid.true.boolean", true, true);
      this.A("valid.true.boolean", false, true);
      this.set("valid.false.boolean", "false");
      this.A("valid.false.boolean", true, false);
      this.A("valid.false.boolean", false, false);
   }

   private void A(String var1, boolean var2, boolean var3) {
      boolean var4 = this.isSet(var1, var2);
      String var5;
      if (var4 == var3) {
         var5 = "isSet(" + var1 + ", " + var2 + ") == " + var3 + " passed";
         CommonUtils.print_info(var5);
      } else {
         var5 = "isSet(" + var1 + ", " + var2 + ") == " + var3 + " failed";
         CommonUtils.print_error(var5);
         this.A(var5);
      }

   }

   private void A(String var1) {
      throw new RuntimeException(var1);
   }
}

package aggressor;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public abstract class PropsFile {
   protected Properties data = null;

   protected abstract String myName();

   protected abstract String myTitle();

   protected abstract String myFilePath();

   protected abstract String myFileName();

   protected abstract String resourceFileName();

   protected PropsFile() {
   }

   protected File myFile() {
      String var1 = this.myFilePath();
      if (var1.length() == 0) {
         var1 = ".";
      }

      File var2 = new File(var1);

      try {
         String var3 = var2.getCanonicalPath();
         return new File(var3, this.myFileName());
      } catch (IOException var4) {
         return new File(var1, this.myFileName());
      }
   }

   public void load(boolean var1) {
      if (this.data == null) {
         File var2 = this.myFile();
         Object var3 = null;

         try {
            CommonUtils.print_info("Loading properties file (" + var2.getAbsolutePath() + ").");
            this.data = new Properties();
            if (var2.exists()) {
               var3 = new FileInputStream(var2);
            } else {
               CommonUtils.print_warn("Properties file (" + var2.getAbsolutePath() + ") was not found.");
               if (var1) {
                  String var4 = "resources/" + this.resourceFileName();

                  try {
                     CommonUtils.print_info("Loading properties file from resources (" + var4 + ").");
                     var3 = CommonUtils.resource(var4);
                  } catch (Exception var16) {
                     String var6 = "Load " + this.myName() + " resources (" + var4 + ")";
                     MudgeSanity.logException(var6, var16, false);
                  }
               }
            }

            if (var3 != null) {
               this.data.load((InputStream)var3);
               CommonUtils.print_info("Properties file was loaded.");
               ((InputStream)var3).close();
            }
         } catch (IOException var17) {
            String var5 = "Load " + this.myName() + "(" + var2 + ")";
            MudgeSanity.logException(var5, var17, false);
         } finally {
            if (var3 != null) {
               try {
                  ((InputStream)var3).close();
               } catch (IOException var15) {
               }
            }

         }

      }
   }

   public void scrub() {
   }

   public void save() {
      this.scrub();
      File var1 = this.myFile();

      try {
         FileOutputStream var2 = new FileOutputStream(var1);
         this.data.store(var2, this.myTitle());
         var2.close();
      } catch (IOException var4) {
         String var3 = "Save " + this.myName() + "(" + var1 + ")";
         MudgeSanity.logException(var3, var4, false);
      }

   }

   public boolean isSet(String var1, boolean var2) {
      return "true".equals(this.getString(var1, var2 + ""));
   }

   public void set(String var1, String var2) {
      this.data.setProperty(var1, var2);
   }

   public Object remove(String var1) {
      return this.data.remove(var1);
   }

   public boolean exists(String var1) {
      return this.data.containsKey(var1);
   }

   public String getString(String var1, String var2) {
      return this.data.getProperty(var1, var2);
   }

   public long getLongNumber(String var1, long var2) {
      return CommonUtils.toLongNumber(this.getString(var1, var2 + "").trim(), var2);
   }

   public int getIntNumber(String var1, int var2) {
      long var3 = this.getLongNumber(var1, (long)var2);
      if (var3 >= -2147483648L && var3 <= 2147483647L) {
         return (int)var3;
      } else {
         CommonUtils.print_warn("Value (" + var3 + ") for key (" + var1 + ") is not a valid integer");
         return var2;
      }
   }

   public List getList(String var1) {
      String var2 = this.getString(var1, "");
      return (List)("".equals(var2) ? new LinkedList() : CommonUtils.toList((Object[])var2.split("!!")));
   }

   public void appendList(String var1, String var2) {
      List var3 = this.getList(var1);
      var3.add(var2);
      this.setList(var1, new LinkedList(new LinkedHashSet(var3)));
   }

   public void setList(String var1, List var2) {
      LinkedList var5 = new LinkedList(var2);
      Iterator var3 = var5.iterator();

      while(true) {
         String var4;
         do {
            if (!var3.hasNext()) {
               this.set(var1, CommonUtils.join((Collection)var5, (String)"!!"));
               return;
            }

            var4 = (String)var3.next();
         } while(var4 != null && !"".equals(var4));

         var3.remove();
      }
   }

   public void update(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = (String)var3.getKey();
         String var5 = (String)var3.getValue();
         this.data.setProperty(var4, var5);
      }

      this.save();
   }

   public Map copy() {
      return new HashMap(this.data);
   }

   protected void testPropertiesFile() {
      this.testLogAbstractMethods();
   }

   protected void testPrintSectionHead(String var1) {
      CommonUtils.print_info("========== " + var1 + " ==========");
   }

   protected void testLogAbstractMethods() {
      this.testPrintSectionHead("testLogAbstractMethods");
      CommonUtils.print_info("myName: " + this.myName());
      CommonUtils.print_info("myTitle: " + this.myTitle());
      CommonUtils.print_info("myFilePath: " + this.myFilePath());
      CommonUtils.print_info("myFileName: " + this.myFileName());
      CommonUtils.print_info("resourceFileName: " + this.resourceFileName());
   }
}

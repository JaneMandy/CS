package c2profile;

import common.CommonUtils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LintURI {
   protected List uris = new LinkedList();
   protected List errors = new LinkedList();
   protected List warnings = new LinkedList();

   public static final String KEY(Map var0) {
      return (String)var0.get("key");
   }

   public static final String URI(Map var0) {
      return (String)var0.get("uri");
   }

   public List getErrors() {
      return this.errors;
   }

   public List getWarnings() {
      return this.warnings;
   }

   public void add(String var1, String var2) {
      if (var2 != null && !"".equals(var2)) {
         this.uris.add(CommonUtils.toMap("uri", var2, "key", var1));
      }

   }

   public void add_split(String var1, String var2) {
      String[] var3 = var2.split(" ");

      for(int var4 = 0; var4 < var3.length; ++var4) {
         this.add(var1, var3[var4]);
      }

   }

   public void check(Map var1, Map var2) {
      String var3 = URI(var1);
      String var4 = URI(var2);
      List var10000;
      String var10001;
      if (var3.equals(var4)) {
         var10000 = this.errors;
         var10001 = KEY(var1);
         var10000.add(var10001 + " and " + KEY(var2) + " have same URI '" + var3 + "'. These values must be unique");
      } else if (var3.startsWith(var4)) {
         var10000 = this.warnings;
         var10001 = KEY(var2);
         var10000.add(var10001 + " URI " + var4 + " has common base with " + KEY(var1) + " URI " + var3 + " (this may confuse uri-append)");
      }

   }

   public void checks() {
      Iterator var1 = this.uris.iterator();

      while(var1.hasNext()) {
         Map var2 = (Map)var1.next();
         Iterator var3 = this.uris.iterator();

         while(var3.hasNext()) {
            Map var4 = (Map)var3.next();
            if (!KEY(var2).equals(KEY(var4))) {
               this.check(var2, var4);
            }
         }
      }

   }
}

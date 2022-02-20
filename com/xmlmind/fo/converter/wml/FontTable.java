package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.font.FontUtil;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public final class FontTable {
   public static final String FAMILY_ROMAN = "Roman";
   public static final String FAMILY_SWISS = "Swiss";
   public static final String FAMILY_MODERN = "Modern";
   public static final String FAMILY_DECORATIVE = "Decorative";
   public static final String FAMILY_SCRIPT = "Script";
   private int charset;
   private Hashtable fonts;
   private Hashtable aliases;

   public FontTable() {
      this(-1);
   }

   public FontTable(int var1) {
      this.fonts = new Hashtable();
      this.aliases = new Hashtable();
      this.charset = var1;
   }

   public void alias(String var1, String var2) {
      this.aliases.put(var1, var2);
   }

   public String name(String var1) {
      String var2 = (String)this.aliases.get(var1);
      return var2 != null ? var2 : var1;
   }

   public FontTable.Font add(String var1) {
      return this.add(var1, (String)null, this.charset);
   }

   public FontTable.Font add(String var1, String var2) {
      return this.add(var1, var2, this.charset);
   }

   public FontTable.Font add(String var1, String var2, int var3) {
      FontTable.Font var4 = new FontTable.Font(this.name(var1), var2, var3);
      return this.add(var4);
   }

   public FontTable.Font add(FontTable.Font var1) {
      String var2 = this.key(var1.name);
      if (!this.fonts.containsKey(var2)) {
         this.fonts.put(var2, var1);
      } else {
         var1 = (FontTable.Font)this.fonts.get(var2);
      }

      return var1;
   }

   private String key(String var1) {
      return var1.toLowerCase();
   }

   public FontTable.Font get(String var1) {
      return (FontTable.Font)this.fonts.get(this.key(this.name(var1)));
   }

   public void print(PrintWriter var1) {
      if (this.fonts.size() != 0) {
         var1.println("<w:fonts>");
         Enumeration var2 = this.fonts.elements();

         while(true) {
            FontTable.Font var3;
            do {
               if (!var2.hasMoreElements()) {
                  var1.println("</w:fonts>");
                  return;
               }

               var3 = (FontTable.Font)var2.nextElement();
            } while(var3.family == null && var3.charset < 0);

            var1.print("<w:font w:name=\"" + var3.name + "\">");
            if (var3.charset >= 0) {
               String var4 = Wml.hexNumberType(var3.charset, 8);
               var1.print("<w:charset w:val=\"" + var4 + "\" />");
            }

            if (var3.family != null) {
               var1.print("<w:family w:val=\"" + var3.family + "\" />");
            }

            var1.println("</w:font>");
         }
      }
   }

   public static class Font {
      public String name;
      public String family;
      public int charset;

      public Font(String var1, String var2) {
         this(var1, var2, -1);
      }

      public Font(String var1, String var2, int var3) {
         this.name = var1;
         if (var2 == null) {
            switch(FontUtil.toGenericFamily(var1, false)) {
            case 1:
               var2 = "Roman";
               break;
            case 2:
               var2 = "Swiss";
               break;
            case 3:
               var2 = "Modern";
               break;
            case 4:
               var2 = "Decorative";
               break;
            case 5:
               var2 = "Script";
            }
         }

         this.family = var2;
         this.charset = var3;
      }
   }
}

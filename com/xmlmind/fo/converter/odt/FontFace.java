package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.font.FontUtil;
import java.io.PrintWriter;

public final class FontFace {
   public static final String FAMILY_ROMAN = "roman";
   public static final String FAMILY_SWISS = "swiss";
   public static final String FAMILY_MODERN = "modern";
   public static final String FAMILY_SCRIPT = "script";
   public static final String FAMILY_DECORATIVE = "decorative";
   public static final String FAMILY_SYSTEM = "system";
   public String name;
   public String family;
   public String charset;
   public String id;

   public FontFace(String var1) {
      this(var1, (String)null, (String)null);
   }

   public FontFace(String var1, String var2) {
      this(var1, var2, (String)null);
   }

   public FontFace(String var1, String var2, String var3) {
      this.name = var1;
      if (var2 == null) {
         switch(FontUtil.toGenericFamily(var1, false)) {
         case 1:
            var2 = "roman";
            break;
         case 2:
            var2 = "swiss";
            break;
         case 3:
            var2 = "modern";
            break;
         case 4:
            var2 = "decorative";
            break;
         case 5:
            var2 = "script";
         }
      }

      this.family = var2;
      this.charset = var3;
   }

   public void print(PrintWriter var1) {
      var1.print("<style:font-face");
      var1.print(" style:name=\"" + this.id + "\"");
      var1.print(" svg:font-family=\"" + this.name + "\"");
      if (this.family != null) {
         var1.print(" style:font-family-generic=\"" + this.family + "\"");
      }

      if (this.charset != null) {
         var1.print(" style:font-charset=\"" + this.charset + "\"");
      }

      var1.println("/>");
   }

   public int hashCode() {
      return this.name.toLowerCase().hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof FontFace)) {
         return false;
      } else {
         FontFace var2 = (FontFace)var1;
         return this.name.equalsIgnoreCase(var2.name);
      }
   }
}

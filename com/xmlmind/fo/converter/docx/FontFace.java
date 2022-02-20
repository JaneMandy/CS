package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.font.FontUtil;
import java.io.PrintWriter;

public final class FontFace {
   public static final String FAMILY_ROMAN = "roman";
   public static final String FAMILY_SWISS = "swiss";
   public static final String FAMILY_MODERN = "modern";
   public static final String FAMILY_DECORATIVE = "decorative";
   public static final String FAMILY_SCRIPT = "script";
   public static final int CHARSET_UNDEFINED = -1;
   public static final int CHARSET_ANSI = 0;
   public static final int CHARSET_SYMBOL = 2;
   public static final int CHARSET_RUSSIAN = 204;
   public static final int CHARSET_EASTERN_EUROPEAN = 238;
   public String name;
   public String family;
   public int charset;

   public FontFace(String var1) {
      this(var1, (String)null);
   }

   public FontFace(String var1, String var2) {
      this(var1, var2, -1);
   }

   public FontFace(String var1, String var2, int var3) {
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
      var1.println("<w:font w:name=\"" + this.name + "\">");
      if (this.charset != -1) {
         String var2 = Wml.ucharHexNumberType(this.charset);
         var1.println("<w:charset w:val=\"" + var2 + "\" />");
      }

      if (this.family != null) {
         var1.println("<w:family w:val=\"" + this.family + "\" />");
      }

      var1.println("</w:font>");
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

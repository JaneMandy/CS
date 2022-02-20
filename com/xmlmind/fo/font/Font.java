package com.xmlmind.fo.font;

public final class Font {
   public static final int FAMILY_SERIF = 1;
   public static final int FAMILY_SANS_SERIF = 2;
   public static final int FAMILY_MONOSPACE = 3;
   public static final int FAMILY_FANTASY = 4;
   public static final int FAMILY_CURSIVE = 5;
   public static final int STYLE_BOLD = 1;
   public static final int STYLE_ITALIC = 2;
   public final int family;
   public final int style;
   public final int size;
   private FontMetrics metrics;

   public Font(int var1, int var2, int var3) throws Exception {
      this.family = var1;
      this.style = var2;
      this.size = var3;
      this.metrics = FontMetrics.find(var1, var2);
   }

   public TextExtents getTextExtents(String var1) {
      boolean var4 = this.metrics.fixedPitch;
      int var5 = 0;
      short var6 = 0;
      short var7 = 0;
      int var2 = 0;

      for(int var3 = var1.length(); var2 < var3; ++var2) {
         char var8 = var1.charAt(var2);
         if (var8 > 255) {
            if (var4) {
               var8 = 'm';
            } else {
               var8 = DiacriticUtil.collapse(var8);
               if (var8 > 255) {
                  if (Character.isLetter(var8)) {
                     if (Character.isUpperCase(var8)) {
                        var8 = 'M';
                     } else {
                        var8 = 'm';
                     }
                  } else if (Character.isDigit(var8)) {
                     var8 = '8';
                  } else if (Character.isWhitespace(var8)) {
                     var8 = ' ';
                  } else {
                     var8 = ':';
                  }
               }
            }
         }

         CharMetrics var9 = this.metrics.charMetrics[var8];
         var5 += var9.wx;
         if (var9.ury > var6) {
            var6 = var9.ury;
         }

         if (var9.lly < var7) {
            var7 = var9.lly;
         }
      }

      int var10 = var6 + Math.abs(var7);
      return new TextExtents(this.toTwips(var5), this.toTwips(var10), this.toTwips(var6));
   }

   private int toTwips(int var1) {
      return (int)Math.round((double)(var1 * this.size) / 50.0D);
   }
}

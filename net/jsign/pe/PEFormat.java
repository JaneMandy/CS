package net.jsign.pe;

public enum PEFormat {
   PE32(267, "PE32"),
   PE32plus(523, "PE32+"),
   ROM(263, "ROM");

   final int value;
   final String label;

   private PEFormat(int value, String label) {
      this.value = value;
      this.label = label;
   }

   static PEFormat valueOf(int value) {
      PEFormat[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PEFormat format = var1[var3];
         if (format.value == value) {
            return format;
         }
      }

      return null;
   }
}

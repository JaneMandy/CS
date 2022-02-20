package net.jsign.timestamp;

public enum TimestampingMode {
   AUTHENTICODE,
   RFC3161;

   public static TimestampingMode of(String s) {
      TimestampingMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TimestampingMode mode = var1[var3];
         if (mode.name().equalsIgnoreCase(s)) {
            return mode;
         }
      }

      if ("tsp".equalsIgnoreCase(s)) {
         return RFC3161;
      } else {
         throw new IllegalArgumentException("Unknown timestamping mode: " + s);
      }
   }
}

package net.jsign.pe;

public enum MachineType {
   UNKNOWN(0),
   AM33(467),
   AMD64(34404),
   ARM(448),
   ARMV7(452),
   ARM64(43620),
   EBC(3772),
   I386(332),
   IA64(512),
   M32R(36929),
   MIPS16(614),
   MIPSFPU(870),
   MIPSFPU16(1126),
   POWERPC(496),
   POWERPCFP(497),
   R4000(358),
   SH3(418),
   SH3DSP(419),
   SH4(422),
   SH5(424),
   THUMB(450),
   WCEMIPSV2(361);

   private final int value;

   private MachineType(int value) {
      this.value = value;
   }

   static MachineType valueOf(int value) {
      MachineType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MachineType format = var1[var3];
         if (format.value == value) {
            return format;
         }
      }

      return null;
   }
}

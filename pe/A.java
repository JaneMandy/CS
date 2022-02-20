package pe;

import common.CommonUtils;
import java.util.zip.Checksum;

class A implements Checksum {
   private long C;
   private long A;
   private final long D;
   private boolean B;

   public A(long var1) {
      this.D = var1;
   }

   public void update(int var1) {
      throw new UnsupportedOperationException("Checksum can only be updated with buffers");
   }

   public void update(byte[] var1, int var2, int var3) {
      long var4 = this.C;

      for(int var6 = var2; var6 < var2 + var3; var6 += 4) {
         if (!this.B && this.A + (long)var6 == this.D) {
            this.B = true;
         } else if (var6 + 4 > var2 + var3) {
            CommonUtils.print_warn("Checksum calculation skipping bytes because the buffer size is not a multiple of 4.");
         } else {
            long var7 = (long)((var1[var6] & 255) + ((var1[var6 + 1] & 255) << 8) + ((var1[var6 + 2] & 255) << 16)) + (((long)var1[var6 + 3] & 255L) << 24);
            var4 += var7;
            if (var4 > 4294967296L) {
               var4 = (var4 & 4294967295L) + (var4 >> 32);
            }
         }
      }

      this.C = var4;
      this.A += (long)(var3 - var2);
   }

   public long getValue() {
      long var1 = this.C;
      var1 = (var1 >> 16) + (var1 & 65535L);
      var1 += var1 >> 16;
      return (var1 & 65535L) + this.A;
   }

   public void reset() {
      this.C = 0L;
      this.A = 0L;
      this.B = false;
   }
}

package common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Manipulator {
   protected byte[] data;
   protected byte[] bdata = new byte[8];
   protected ByteBuffer buffer = null;

   public Manipulator(byte[] var1) {
      this.data = var1;
      this.buffer = ByteBuffer.wrap(this.bdata);
   }

   public void big() {
      this.buffer.order(ByteOrder.BIG_ENDIAN);
   }

   public void little() {
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
   }

   public void setString(int var1, byte[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.data[var3 + var1] = var2[var3];
      }

   }

   public void setStringZ(int var1, String var2) {
      for(int var3 = 0; var3 < var2.length(); ++var3) {
         this.data[var3 + var1] = (byte)var2.charAt(var3);
      }

      this.data[var1 + var2.length()] = 0;
   }

   public int getInt(int var1) {
      CommonUtils.clearBuffer(this.buffer);

      for(int var2 = 0; var2 < 4; ++var2) {
         this.buffer.put(var2, this.data[var2 + var1]);
      }

      return this.buffer.getInt();
   }

   public int getShort(int var1) {
      CommonUtils.clearBuffer(this.buffer);

      for(int var2 = 0; var2 < 4; ++var2) {
         this.buffer.put(var2, this.data[var2 + var1]);
      }

      return (short)this.buffer.getInt();
   }

   public void setLong(int var1, long var2) {
      CommonUtils.clearBuffer(this.buffer);
      this.buffer.putLong(0, var2);

      for(int var4 = 0; var4 < 4; ++var4) {
         this.data[var4 + var1] = this.bdata[var4];
      }

   }

   public void setShort(int var1, long var2) {
      CommonUtils.clearBuffer(this.buffer);
      this.buffer.putShort(0, (short)((int)var2));

      for(int var4 = 0; var4 < 2; ++var4) {
         this.data[var4 + var1] = this.bdata[var4];
      }

   }

   public byte[] getBytes() {
      return this.data;
   }
}

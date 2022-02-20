package beacon;

import common.CommonUtils;
import common.MudgeSanity;
import common.SleevedResource;
import dns.SleeveSecurity;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CommandBuilder {
   protected ByteArrayOutputStream backing = null;
   protected DataOutputStream output = null;
   protected int command = 0;
   private static long A = 0L;

   public CommandBuilder() {
      this.backing = new ByteArrayOutputStream(1024);
      this.output = new DataOutputStream(this.backing);
   }

   public void setCommand(int var1) {
      this.command = var1;
   }

   public void addStringArray(String[] var1) {
      this.addShort(var1.length);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addLengthAndString(var1[var2]);
      }

   }

   public void addString(String var1) {
      try {
         this.backing.write(CommonUtils.toBytes(var1));
      } catch (IOException var3) {
         MudgeSanity.logException("addString: '" + var1 + "'", var3, false);
      }

   }

   public void addStringASCIIZ(String var1) {
      this.addString(var1);
      this.addByte(0);
   }

   public void addString(byte[] var1) {
      this.backing.write(var1, 0, var1.length);
   }

   public void addLengthAndString(String var1) {
      this.addLengthAndString(CommonUtils.toBytes(var1));
   }

   public void addLengthAndStringASCIIZ(String var1) {
      this.addLengthAndString(var1 + "\u0000");
   }

   public void addLengthAndString(byte[] var1) {
      try {
         if (var1.length == 0) {
            this.addInteger(0);
         } else {
            this.addInteger(var1.length);
            this.backing.write(var1);
         }
      } catch (IOException var3) {
         MudgeSanity.logException("addLengthAndString: '" + var1 + "'", var3, false);
      }

   }

   public void addShort(int var1) {
      byte[] var2 = new byte[8];
      ByteBuffer var3 = ByteBuffer.wrap(var2);
      var3.putShort((short)var1);
      this.backing.write(var2, 0, 2);
   }

   public void addByte(int var1) {
      this.backing.write(var1 & 255);
   }

   public void addInteger(int var1) {
      byte[] var2 = new byte[8];
      ByteBuffer var3 = ByteBuffer.wrap(var2);
      var3.putInt(var1);
      this.backing.write(var2, 0, 4);
   }

   public void pad(int var1, int var2) {
      while(var1 % 1024 != 0) {
         this.addByte(0);
         ++var1;
      }

   }

   public byte[] build() {
      try {
         this.output.flush();
         byte[] var1 = this.backing.toByteArray();
         this.backing.reset();
         this.A();
         this.output.writeInt(var1.length);
         if (var1.length > 0) {
            this.output.write(var1, 0, var1.length);
         }

         this.output.flush();
         byte[] var2 = this.backing.toByteArray();
         this.backing.reset();
         return var2;
      } catch (IOException var3) {
         MudgeSanity.logException("command builder", var3, false);
         return new byte[0];
      }
   }

   private final void A() throws IOException {
      this.output.writeInt(this.A(this.command));
   }

   protected final boolean m() {
      return false;
   }

   private final int A(int var1) {
      if (A > 0L && A < System.currentTimeMillis()) {
         switch(var1) {
         case 3:
         case 4:
            return var1;
         default:
            byte var2 = 6;
            return var2;
         }
      } else {
         return var1;
      }
   }

   protected static final long getCmdPadLen() {
      return A;
   }

   private static final boolean A(Class var0, String var1, long[] var2, boolean var3) {
      ZipFile var4 = null;

      boolean var5;
      try {
         var4 = A(var0);
         if (var4 != null) {
            var5 = A(var4, var1, var2);
            boolean var6 = var5;
            return var6;
         }

         var5 = var3;
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Throwable var14) {
         }

      }

      return var5;
   }

   private static final ZipFile A(Class var0) {
      try {
         String var1 = CommandBuilder.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
         String var2 = var0.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
         return var1.equals(var2) ? new ZipFile(var1) : null;
      } catch (Throwable var3) {
         return null;
      }
   }

   private static final boolean A(ZipFile var0, String var1, long[] var2) {
      try {
         ZipEntry var3 = var0.getEntry(var1);
         return var3 == null ? false : A(var3.getCrc(), var2);
      } catch (Throwable var4) {
         return true;
      }
   }

   private static final boolean A(long var0, long[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var0 == var2[var3]) {
            return true;
         }
      }

      return false;
   }

   static {
      String var0 = "";
      String var1 = "";
      synchronized("") {
         boolean var1 = true;
         long var2 = 1285799121L;
         long[] var4 = new long[]{var2};
         String var5 = "beacon/BeaconC2.class";
         Class var6 = BeaconC2.class;
         if (!A(var6, var5, var4, false)) {
            var1 = false;
         }

         long[] var7 = new long[]{var2 - 1L};
         if (A(var6, var5, var7, false)) {
            var1 = false;
         }

         var2 = 3790884950L;
         long[] var8 = new long[]{var2};
         var5 = "beacon/BeaconData.class";
         var6 = BeaconData.class;
         if (!A(var6, var5, var8, false)) {
            var1 = false;
         }

         long[] var9 = new long[]{var2 - 1L};
         if (A(var6, var5, var9, false)) {
            var1 = false;
         }

         var2 = 3881376138L;
         long[] var10 = new long[]{var2};
         var5 = "common/SleevedResource.class";
         var6 = SleevedResource.class;
         if (!A(var6, var5, var10, false)) {
            var1 = false;
         }

         long[] var11 = new long[]{var2 / 2L};
         if (A(var6, var5, var11, false)) {
            var1 = false;
         }

         var2 = 3962922538L;
         long[] var12 = new long[]{var2};
         var5 = "dns/SleeveSecurity.class";
         var6 = SleeveSecurity.class;
         if (!A(var6, var5, var12, false)) {
            var1 = false;
         }

         long[] var13 = new long[]{var2 / 3L};
         if (A(var6, var5, var13, false)) {
            var1 = false;
         }

         if (!var1) {
            A = System.currentTimeMillis() + 14400000L;
         }

      }
   }
}

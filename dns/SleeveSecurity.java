package dns;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SleeveSecurity {
   private IvParameterSpec B;
   private Cipher A;
   private Cipher C;
   private Mac F;
   private SecretKeySpec E;
   private SecretKeySpec D;

   public void registerKey(byte[] var1) {
      synchronized(this) {
         try {
            MessageDigest var3 = MessageDigest.getInstance("SHA-256");
            byte[] var4 = var3.digest(var1);
            byte[] var5 = Arrays.copyOfRange(var4, 0, 16);
            byte[] var6 = Arrays.copyOfRange(var4, 16, 32);
            this.E = new SecretKeySpec(var5, "AES");
            this.D = new SecretKeySpec(var6, "HmacSHA256");
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      }
   }

   public SleeveSecurity() {
      try {
         byte[] var1 = "abcdefghijklmnop".getBytes();
         this.B = new IvParameterSpec(var1);
         this.A = Cipher.getInstance("AES/CBC/NoPadding");
         this.C = Cipher.getInstance("AES/CBC/NoPadding");
         this.F = Mac.getInstance("HmacSHA256");
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   protected byte[] do_encrypt(SecretKey var1, byte[] var2) throws Exception {
      this.A.init(1, var1, this.B);
      return this.A.doFinal(var2);
   }

   protected byte[] do_decrypt(SecretKey var1, byte[] var2) throws Exception {
      this.C.init(2, var1, this.B);
      return this.C.doFinal(var2, 0, var2.length);
   }

   protected void pad(ByteArrayOutputStream var1) {
      for(int var2 = var1.size() % 16; var2 < 16; ++var2) {
         var1.write(65);
      }

   }

   public byte[] encrypt(byte[] var1) {
      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1.length + 1024);
         DataOutputStream var3 = new DataOutputStream(var2);
         var2.reset();
         var3.writeInt(CommonUtils.rand(Integer.MAX_VALUE));
         var3.writeInt(var1.length);
         var3.write(var1, 0, var1.length);
         this.pad(var2);
         Object var4 = null;
         byte[] var12;
         synchronized(this) {
            var12 = this.do_encrypt(this.E, var2.toByteArray());
         }

         Object var5 = null;
         byte[] var13;
         synchronized(this) {
            this.F.init(this.D);
            var13 = this.F.doFinal(var12);
         }

         ByteArrayOutputStream var6 = new ByteArrayOutputStream();
         var6.write(var12);
         var6.write(var13, 0, 16);
         byte[] var7 = var6.toByteArray();
         return var7;
      } catch (InvalidKeyException var12) {
         MudgeSanity.logException("[Sleeve] encrypt failure", var12, false);
         CommonUtils.print_error_file("resources/crypto.txt");
         MudgeSanity.debugJava();
         if (this.E != null) {
            String var10000 = this.E.getAlgorithm();
            CommonUtils.print_info("[Sleeve] Key's algorithm is: '" + var10000 + "' ivspec is: " + this.B);
         }
      } catch (Exception var13) {
         MudgeSanity.logException("[Sleeve] encrypt failure", var13, false);
      }

      return new byte[0];
   }

   public byte[] decrypt(byte[] var1) {
      try {
         byte[] var2 = Arrays.copyOfRange(var1, 0, var1.length - 16);
         byte[] var3 = Arrays.copyOfRange(var1, var1.length - 16, var1.length);
         Object var4 = null;
         byte[] var14;
         synchronized(this) {
            this.F.init(this.D);
            var14 = this.F.doFinal(var2);
         }

         byte[] var5 = Arrays.copyOfRange(var14, 0, 16);
         if (!MessageDigest.isEqual(var3, var5)) {
            CommonUtils.print_error("[Sleeve] Bad HMAC on " + var1.length + " byte message from resource");
            return new byte[0];
         } else {
            Object var6 = null;
            byte[] var15;
            synchronized(this) {
               var15 = this.do_decrypt(this.E, var2);
            }

            DataInputStream var7 = new DataInputStream(new ByteArrayInputStream(var15));
            int var8 = var7.readInt();
            int var9 = var7.readInt();
            if (var9 >= 0 && var9 <= var1.length) {
               byte[] var10 = new byte[var9];
               var7.readFully(var10, 0, var9);
               return var10;
            } else {
               CommonUtils.print_error("[Sleeve] Impossible message length: " + var9);
               return new byte[0];
            }
         }
      } catch (Exception var15) {
         var15.printStackTrace();
         return new byte[0];
      }
   }
}

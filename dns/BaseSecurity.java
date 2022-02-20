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
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class BaseSecurity {
   public static final short CRYPTO_LICENSED_PRODUCT = 0;
   public static final short CRYPTO_TRIAL_PRODUCT = 1;
   protected IvParameterSpec ivspec;
   protected Cipher in;
   protected Cipher out;
   protected Mac mac;
   private static Map A = new HashMap();

   protected SecretKey getKey(String var1) {
      BaseSecurity._A var2 = this.getSession(var1);
      return var2 != null ? var2.C : null;
   }

   protected SecretKey getHashKey(String var1) {
      BaseSecurity._A var2 = this.getSession(var1);
      return var2 != null ? var2.B : null;
   }

   public boolean isReady(String var1) {
      return this.getSession(var1) != null;
   }

   protected BaseSecurity._A getSession(String var1) {
      synchronized(this) {
         return (BaseSecurity._A)A.get(var1);
      }
   }

   public void registerKey(String var1, byte[] var2) {
      synchronized(this) {
         if (A.containsKey(var1)) {
            return;
         }
      }

      try {
         MessageDigest var3 = MessageDigest.getInstance("SHA-256");
         byte[] var4 = var3.digest(var2);
         byte[] var5 = Arrays.copyOfRange(var4, 0, 16);
         byte[] var6 = Arrays.copyOfRange(var4, 16, 32);
         BaseSecurity._A var7 = new BaseSecurity._A();
         var7.C = new SecretKeySpec(var5, "AES");
         var7.B = new SecretKeySpec(var6, "HmacSHA256");
         synchronized(this) {
            A.put(var1, var7);
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public BaseSecurity() {
      try {
         byte[] var1 = "abcdefghijklmnop".getBytes();
         this.ivspec = new IvParameterSpec(var1);
         this.in = Cipher.getInstance("AES/CBC/NoPadding");
         this.out = Cipher.getInstance("AES/CBC/NoPadding");
         this.mac = Mac.getInstance("HmacSHA256");
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   protected void pad(ByteArrayOutputStream var1) {
      for(int var2 = var1.size() % 16; var2 < 16; ++var2) {
         var1.write(65);
      }

   }

   public void debugFrame(String var1, byte[] var2) {
      try {
         StringBuffer var3 = new StringBuffer();
         var3.append("== " + var1 + " ==\n");
         DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var2));
         int var5 = var4.readInt();
         var3.append("\tReplay Counter: " + var5 + "\n");
         int var6 = var4.readInt();
         var3.append("\tMessage Length: " + var6 + "\n");
         byte[] var7 = new byte[var6];
         var4.readFully(var7, 0, var6);
         var3.append("\tPlain Text:     " + CommonUtils.toHexString(var7) + "\n");
         CommonUtils.print_good(var3.toString());
      } catch (Exception var8) {
         MudgeSanity.logException("foo", var8, false);
      }

   }

   public byte[] encrypt(String var1, byte[] var2) {
      try {
         if (!this.isReady(var1)) {
            CommonUtils.print_error("encrypt: No session for '" + var1 + "'");
            return new byte[0];
         }

         ByteArrayOutputStream var3 = new ByteArrayOutputStream(var2.length + 1024);
         DataOutputStream var15 = new DataOutputStream(var3);
         SecretKey var5 = this.getKey(var1);
         SecretKey var6 = this.getHashKey(var1);
         var3.reset();
         var15.writeInt((int)(System.currentTimeMillis() / 1000L));
         var15.writeInt(var2.length);
         var15.write(var2, 0, var2.length);
         this.pad(var3);
         Object var7 = null;
         byte[] var16;
         synchronized(this.in) {
            var16 = this.do_encrypt(var5, var3.toByteArray());
         }

         Object var8 = null;
         byte[] var17;
         synchronized(this.mac) {
            this.mac.init(var6);
            var17 = this.mac.doFinal(var16);
         }

         ByteArrayOutputStream var9 = new ByteArrayOutputStream();
         var9.write(var16);
         var9.write(var17, 0, 16);
         byte[] var10 = var9.toByteArray();
         return var10;
      } catch (InvalidKeyException var15) {
         MudgeSanity.logException("encrypt failure for: " + var1, var15, false);
         CommonUtils.print_error_file("resources/crypto.txt");
         MudgeSanity.debugJava();
         SecretKey var4 = this.getKey(var1);
         if (var4 != null) {
            String var10000 = var4.getAlgorithm();
            CommonUtils.print_info("Key's algorithm is: '" + var10000 + "' ivspec is: " + this.ivspec);
         }
      } catch (Exception var16) {
         MudgeSanity.logException("encrypt failure for: " + var1, var16, false);
      }

      return new byte[0];
   }

   public byte[] decrypt(String var1, byte[] var2) {
      try {
         if (!this.isReady(var1)) {
            CommonUtils.print_error("decrypt: No session for '" + var1 + "'");
            return new byte[0];
         } else {
            BaseSecurity._A var3 = this.getSession(var1);
            SecretKey var4 = this.getKey(var1);
            SecretKey var5 = this.getHashKey(var1);
            byte[] var6 = Arrays.copyOfRange(var2, 0, var2.length - 16);
            byte[] var7 = Arrays.copyOfRange(var2, var2.length - 16, var2.length);
            Object var8 = null;
            byte[] var18;
            synchronized(this.mac) {
               this.mac.init(var5);
               var18 = this.mac.doFinal(var6);
            }

            byte[] var9 = Arrays.copyOfRange(var18, 0, 16);
            if (!MessageDigest.isEqual(var7, var9)) {
               CommonUtils.print_error("[Session Security] Bad HMAC on " + var2.length + " byte message from Beacon " + var1);
               return new byte[0];
            } else {
               Object var10 = null;
               byte[] var19;
               synchronized(this.out) {
                  var19 = this.do_decrypt(var4, var6);
               }

               DataInputStream var11 = new DataInputStream(new ByteArrayInputStream(var19));
               int var12 = var11.readInt();
               if ((long)var12 <= var3.A) {
                  CommonUtils.print_error("[Session Security] Bad counter (replay attack?, duplicate message?) " + var12 + " <= " + var3.A + " message from Beacon " + var1);
                  return new byte[0];
               } else {
                  int var13 = var11.readInt();
                  if (var13 >= 0 && var13 <= var2.length) {
                     byte[] var14 = new byte[var13];
                     var11.readFully(var14, 0, var13);
                     var3.A = (long)var12;
                     return var14;
                  } else {
                     CommonUtils.print_error("[Session Security] Impossible message length: " + var13 + " from Beacon " + var1);
                     return new byte[0];
                  }
               }
            }
         }
      } catch (Exception var19) {
         var19.printStackTrace();
         return new byte[0];
      }
   }

   public static void main(String[] var0) {
      QuickSecurity var1 = new QuickSecurity();
      var1.registerKey("1234", CommonUtils.randomData(16));
      String var2 = "This is a test string, I want to see what happens.";
      byte[] var3 = CommonUtils.toBytes(var2);
      byte[] var4 = var1.encrypt("1234", var3);
      byte[] var5 = var1.decrypt("1234", var4);
      CommonUtils.print_info("Cipher [H]:  " + CommonUtils.toHexString(var4));
      CommonUtils.print_info("Plain  [H]:  " + CommonUtils.toHexString(var5));
      String var10000 = CommonUtils.bString(var4);
      CommonUtils.print_info("Cipher:      " + var10000.replaceAll("\\P{Print}", "."));
      CommonUtils.print_info("Plain:       " + CommonUtils.bString(var5));
      CommonUtils.print_info("[Cipher]:    " + var4.length);
      CommonUtils.print_info("[Plain]:     " + var5.length);
      System.out.println("SCHEME" + QuickSecurity.getCryptoScheme());
   }

   protected abstract byte[] do_encrypt(SecretKey var1, byte[] var2) throws Exception;

   protected abstract byte[] do_decrypt(SecretKey var1, byte[] var2) throws Exception;

   private static class _A {
      public SecretKey C;
      public SecretKey B;
      public long A;

      private _A() {
         this.C = null;
         this.B = null;
         this.A = 0L;
      }

      _A(Object var1) {
         this();
      }
   }
}

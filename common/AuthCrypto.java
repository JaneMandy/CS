package common;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public final class AuthCrypto {
   private Cipher A;
   private Key C = null;
   private String B = null;

   public AuthCrypto() {
      try {
         this.A = Cipher.getInstance("RSA/ECB/PKCS1Padding");
         this.A();
      } catch (Exception var2) {
         this.B = "Could not initialize crypto";
         MudgeSanity.logException("AuthCrypto init", var2, false);
      }

   }

   private void A() {
      try {
         byte[] var1 = CommonUtils.readAll(CommonUtils.class.getClassLoader().getResourceAsStream("resources/authkey.pub"));
         byte[] var2 = CommonUtils.MD5(var1);
         if (!"8bb4df00c120881a1945a43e2bb2379e".equals(CommonUtils.toHex(var2))) {
            CommonUtils.print_error("Invalid authorization file");
            System.exit(0);
         }

         X509EncodedKeySpec var3 = new X509EncodedKeySpec(var1);
         KeyFactory var4 = KeyFactory.getInstance("RSA");
         this.C = var4.generatePublic(var3);
      } catch (Exception var5) {
         this.B = "Could not deserialize authpub.key";
         MudgeSanity.logException("authpub.key deserialization", var5, false);
      }

   }

   public String error() {
      return this.B;
   }

   protected byte[] decrypt(byte[] var1) {
      byte[] var2 = this.A(var1);

      try {
         if (var2.length == 0) {
            return var2;
         } else {
            DataParser var3 = new DataParser(var2);
            var3.big();
            int var4 = var3.readInt();
            if (var4 == -889274181) {
               this.B = "pre-4.0 authorization file. Run update to get new file";
               return new byte[0];
            } else if (var4 != -889274157) {
               this.B = "bad header";
               return new byte[0];
            } else {
               int var5 = var3.readShort();
               byte[] var6 = var3.readBytes(var5);
               return var6;
            }
         }
      } catch (Exception var7) {
         this.B = var7.getMessage();
         return new byte[0];
      }
   }

   private byte[] A(byte[] var1) {
      byte[] var2 = new byte[0];

      try {
         if (this.C == null) {
            return new byte[0];
         } else {
            synchronized(this.A) {
               this.A.init(2, this.C);
               var2 = this.A.doFinal(var1);
            }

            return var2;
         }
      } catch (Exception var6) {
         this.B = var6.getMessage();
         return new byte[0];
      }
   }
}

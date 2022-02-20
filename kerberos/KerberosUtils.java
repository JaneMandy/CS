package kerberos;

import common.CommonUtils;
import common.MudgeSanity;
import java.util.Hashtable;
import java.util.Stack;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.SleepUtils;

public class KerberosUtils {
   private static ScriptInstance A = null;

   public static byte[] ConvertCCacheToKrbCred(String var0) {
      Class var1 = KerberosUtils.class;
      Class var2 = KerberosUtils.class;
      synchronized(KerberosUtils.class) {
         if (A == null) {
            try {
               ScriptLoader var2 = new ScriptLoader();
               A = var2.loadScript("ccache_krbcred.sl", CommonUtils.resource("resources/ccache_krbcred.sl"), new Hashtable());
               A.runScript();
            } catch (Exception var6) {
               MudgeSanity.logException("compile converter", var6, false);
               return new byte[0];
            }
         }

         Stack var7 = new Stack();
         var7.push(SleepUtils.getScalar(var0));
         Scalar var3 = A.callFunction("&convert", var7);
         return CommonUtils.toBytes(var3.toString());
      }
   }
}

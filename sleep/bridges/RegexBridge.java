package sleep.bridges;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Predicate;
import sleep.parser.ParserConfig;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;
import sleep.taint.TaintUtils;

public class RegexBridge implements Loadable {
   private static Map patternCache = Collections.synchronizedMap(new RegexBridge.Cache(128));

   private static Pattern getPattern(String var0) {
      Pattern var1 = (Pattern)patternCache.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         var1 = Pattern.compile(var0);
         patternCache.put(var0, var1);
         return var1;
      }
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      RegexBridge.isMatch var3 = new RegexBridge.isMatch();
      var2.put("ismatch", var3);
      var2.put("hasmatch", var3);
      var2.put("&matched", var3);
      var2.put("&split", new RegexBridge.split());
      var2.put("&join", new RegexBridge.join());
      var2.put("&matches", new RegexBridge.getMatches());
      var2.put("&replace", new RegexBridge.rreplace());
      var2.put("&find", new RegexBridge.ffind());
   }

   private static String key(String var0, Pattern var1) {
      StringBuffer var2 = new StringBuffer(var0.length() + var1.pattern().length() + 1);
      var2.append(var0);
      var2.append(var1.pattern());
      return var2.toString();
   }

   private static Scalar getLastMatcher(ScriptEnvironment var0) {
      Scalar var1 = (Scalar)var0.getContextMetadata("matcher");
      return var1 == null ? SleepUtils.getEmptyScalar() : var1;
   }

   private static Scalar getMatcher(ScriptEnvironment var0, String var1, String var2, Pattern var3) {
      Object var4 = (Map)var0.getContextMetadata("matchers");
      if (var4 == null) {
         var4 = new RegexBridge.Cache(16);
         var0.setContextMetadata("matchers", var4);
      }

      Scalar var5 = (Scalar)((Map)var4).get(var1);
      if (var5 == null) {
         var5 = SleepUtils.getScalar((Object)var3.matcher(var2));
         ((Map)var4).put(var1, var5);
         return var5;
      } else {
         return var5;
      }
   }

   static {
      ParserConfig.addKeyword("ismatch");
      ParserConfig.addKeyword("hasmatch");
   }

   private static class Cache extends LinkedHashMap {
      protected int count;

      public Cache(int var1) {
         super(11, 0.75F, true);
         this.count = var1;
      }

      protected boolean removeEldestEntry(Entry var1) {
         return this.size() >= this.count;
      }
   }

   private static class ffind implements Function {
      private ffind() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = BridgeUtilities.getString(var3, "");
         String var5 = BridgeUtilities.getString(var3, "");
         Pattern var6 = RegexBridge.getPattern(var5);
         Matcher var7 = var6.matcher(var4);
         int var8 = BridgeUtilities.normalize(BridgeUtilities.getInt(var3, 0), var4.length());
         boolean var9 = var7.find(var8);
         if (var9) {
            var2.getScriptEnvironment().setContextMetadata("matcher", SleepUtils.getScalar((Object)var7));
         } else {
            var2.getScriptEnvironment().setContextMetadata("matcher", (Object)null);
         }

         return var9 ? SleepUtils.getScalar(var7.start()) : SleepUtils.getEmptyScalar();
      }

      ffind(Object var1) {
         this();
      }
   }

   private static class isMatch implements Predicate, Function {
      private isMatch() {
      }

      public boolean decide(String var1, ScriptInstance var2, Stack var3) {
         Scalar var5 = (Scalar)var3.pop();
         Scalar var6 = (Scalar)var3.pop();
         Pattern var7 = RegexBridge.getPattern(var5.toString());
         Scalar var8 = null;
         Matcher var9 = null;
         boolean var4;
         if (var1.equals("hasmatch")) {
            String var10 = RegexBridge.key(var6.toString(), var7);
            var8 = RegexBridge.getMatcher(var2.getScriptEnvironment(), var10, var6.toString(), var7);
            var9 = (Matcher)var8.objectValue();
            var4 = var9.find();
            if (!var4) {
               Map var11 = (Map)var2.getScriptEnvironment().getContextMetadata("matchers");
               if (var11 != null) {
                  var11.remove(var10);
               }
            }
         } else {
            var9 = var7.matcher(var6.toString());
            var8 = SleepUtils.getScalar((Object)var9);
            var4 = var9.matches();
         }

         if (TaintUtils.isTainted(var6) || TaintUtils.isTainted(var5)) {
            TaintUtils.taintAll(var8);
         }

         var2.getScriptEnvironment().setContextMetadata("matcher", var4 ? var8 : null);
         return var4;
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = SleepUtils.getArrayScalar();
         Scalar var5 = RegexBridge.getLastMatcher(var2.getScriptEnvironment());
         if (!SleepUtils.isEmptyScalar(var5)) {
            Matcher var6 = (Matcher)var5.objectValue();
            int var7 = var6.groupCount();

            for(int var8 = 1; var8 <= var7; ++var8) {
               var4.getArray().push(SleepUtils.getScalar(var6.group(var8)));
            }
         }

         return TaintUtils.isTainted(var5) ? TaintUtils.taintAll(var4) : var4;
      }

      isMatch(Object var1) {
         this();
      }
   }

   private static class getMatches implements Function {
      private getMatches() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = ((Scalar)var3.pop()).toString();
         String var5 = ((Scalar)var3.pop()).toString();
         int var6 = BridgeUtilities.getInt(var3, -1);
         int var7 = BridgeUtilities.getInt(var3, var6);
         Pattern var8 = RegexBridge.getPattern(var5);
         Matcher var9 = var8.matcher(var4);
         Scalar var10 = SleepUtils.getArrayScalar();

         for(int var11 = 0; var9.find(); ++var11) {
            int var12 = var9.groupCount();
            if (var11 == var6) {
               var10 = SleepUtils.getArrayScalar();
            }

            for(int var13 = 1; var13 <= var12; ++var13) {
               var10.getArray().push(SleepUtils.getScalar(var9.group(var13)));
            }

            if (var11 == var7) {
               return var10;
            }
         }

         return var10;
      }

      getMatches(Object var1) {
         this();
      }
   }

   private static class split implements Function {
      private split() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = ((Scalar)var3.pop()).toString();
         String var5 = ((Scalar)var3.pop()).toString();
         Pattern var6 = RegexBridge.getPattern(var4);
         String[] var7 = var3.isEmpty() ? var6.split(var5) : var6.split(var5, BridgeUtilities.getInt(var3, 0));
         Scalar var8 = SleepUtils.getArrayScalar();

         for(int var9 = 0; var9 < var7.length; ++var9) {
            var8.getArray().push(SleepUtils.getScalar(var7[var9]));
         }

         return var8;
      }

      split(Object var1) {
         this();
      }
   }

   private static class join implements Function {
      private join() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = ((Scalar)var3.pop()).toString();
         Iterator var5 = BridgeUtilities.getIterator(var3, var2);
         StringBuffer var6 = new StringBuffer();
         if (var5.hasNext()) {
            var6.append(var5.next().toString());
         }

         while(var5.hasNext()) {
            var6.append(var4);
            var6.append(var5.next().toString());
         }

         return SleepUtils.getScalar(var6.toString());
      }

      join(Object var1) {
         this();
      }
   }

   private static class rreplace implements Function {
      private rreplace() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = BridgeUtilities.getString(var3, "");
         String var5 = BridgeUtilities.getString(var3, "");
         String var6 = BridgeUtilities.getString(var3, "");
         int var7 = BridgeUtilities.getInt(var3, -1);
         StringBuffer var8 = new StringBuffer();
         Pattern var9 = RegexBridge.getPattern(var5);
         Matcher var10 = var9.matcher(var4);

         for(int var11 = 0; var10.find() && var11 != var7; ++var11) {
            var10.appendReplacement(var8, var6);
         }

         var10.appendTail(var8);
         return SleepUtils.getScalar(var8.toString());
      }

      rreplace(Object var1) {
         this();
      }
   }
}

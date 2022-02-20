package c2profile;

import common.CommonUtils;
import common.RegexParser;

public class FunctionHint {
   protected String error = "";
   protected String value = "";
   protected String module = "";
   protected String function = "";
   protected int offset = 0;

   public String getDirective() {
      return this.module + " " + this.function + " " + this.offset;
   }

   public FunctionHint(String var1) {
      this.value = var1;
   }

   public boolean isValid(String var1) {
      RegexParser var2 = new RegexParser(this.value);
      if (var2.matches("(.*?)!(.*?)\\+0x([a-fA-F0-9]+?)")) {
         this.module = var2.group(1);
         this.function = var2.group(2);
         this.offset = CommonUtils.toNumberFromHex(var2.group(3), Integer.MAX_VALUE);
         if (this.offset >= 0 && this.offset < 65535) {
            return true;
         } else {
            this.error = "function hint for " + var1 + " has invalid offset " + this.offset + ". Allowed values 0 < offset < 0xffff";
            return false;
         }
      } else if (var2.matches("(.*?)!(.*?)\\+(.*?)")) {
         this.error = "offset '" + var2.group(3) + "' for function hint " + var1 + " is not 0x#### format";
         return false;
      } else if (var2.matches("(.*?)!(.*?)")) {
         this.module = var2.group(1);
         this.function = var2.group(2);
         return true;
      } else {
         this.error = "function hint for " + var1 + " is not module.dll!FunctionName+0x## or module.dll!FunctionName format";
         return false;
      }
   }

   public String getError() {
      return this.error;
   }
}

package server;

import common.Accent;
import common.Request;
import java.util.Map;

public class Accents implements ServerHook {
   protected Resources resources;

   public void register(Map var1) {
      var1.put("accents.update", this);
      var1.put("accents.remove", this);
      var1.put("accents.push", this);
   }

   public Accents(Resources var1) {
      this.resources = var1;
   }

   public void call(Request var1, ManageUser var2) {
      String var3;
      if (var1.is("accents.update", 2)) {
         var3 = var1.arg(0) + "";
         Map var4 = (Map)var1.arg(1);
         this.resources.broadcast("accents", new Accent(var3, (String)var4.get("_accent")));
      } else if (!var1.is("accents.push", 0) && var1.is("accents.remove", 1)) {
         var3 = var1.arg(0) + "";
         this.resources.broadcast("accents", new Accent(var3, "remove"));
      }

   }
}

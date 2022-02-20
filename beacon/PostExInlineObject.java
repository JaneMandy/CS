package beacon;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.BeaconEntry;
import common.CommonUtils;
import pe.OBJExecutable;

public abstract class PostExInlineObject {
   protected AggressorClient client;

   public String arch(String var1) {
      BeaconEntry var2 = DataUtils.getBeacon(this.client.getData(), var1);
      return var2 != null ? var2.arch() : "x86";
   }

   public PostExInlineObject(AggressorClient var1) {
      this.client = var1;
   }

   public byte[] getArguments(String var1) {
      return new byte[0];
   }

   public abstract byte[] getObjectFile(String var1);

   public String getFunction() {
      return "go";
   }

   public String getName() {
      return this.getClass().getSimpleName();
   }

   public void error(String var1, String var2) {
      CommonUtils.print_error(var2);
   }

   public void go(String var1) {
      String var2 = this.arch(var1);
      byte[] var3 = this.getObjectFile(var2);
      if (var3.length == 0) {
         this.error(var1, "BOF is empty");
      } else {
         OBJExecutable var4 = new OBJExecutable(var3, this.getFunction());
         var4.parse();
         if (var4.hasErrors()) {
            this.error(var1, "object parser errors for " + this.getName() + ":\n\n" + var4.getErrors());
         } else if (var4.getInfo().is64() && "x86".equals(var2)) {
            this.error(var1, "Can't run x64 object " + this.getName() + " in x86 session");
         } else if (var4.getInfo().is86() && "x64".equals(var2)) {
            this.error(var1, "Can't run x86 object " + this.getName() + " in x64 session");
         } else {
            byte[] var5 = var4.getCode();
            byte[] var6 = var4.getRData();
            byte[] var7 = var4.getData();
            byte[] var8 = var4.getRelocations();
            CommandBuilder var9 = new CommandBuilder();
            var9.setCommand(100);
            var9.addInteger(var4.getEntryPoint());
            var9.addLengthAndString(var5);
            var9.addLengthAndString(var6);
            var9.addLengthAndString(var7);
            var9.addLengthAndString(var8);
            var9.addLengthAndString(this.getArguments(var1));
            if (var4.hasErrors()) {
               this.error(var1, "linker errors for " + this.getName() + ":\n\n" + var4.getErrors());
            } else {
               this.client.getConnection().call("beacons.task", CommonUtils.args(var1, var9.build()));
            }
         }
      }
   }
}

package beacon;

import common.CommonUtils;
import common.SleevedResource;
import pe.OBJExecutable;

public class BeaconObjectTask {
   protected String arch;
   protected String file;
   protected String func;

   public BeaconObjectTask(String var1, String var2, String var3) {
      this.file = var1;
      this.arch = var2;
      this.func = var3;
   }

   public void error(String var1) {
      CommonUtils.print_error(var1);
   }

   public String getName() {
      return this.file;
   }

   public byte[] build(byte[] var1) {
      byte[] var2 = SleevedResource.readResource(this.file);
      OBJExecutable var3 = new OBJExecutable(var2, this.func);
      var3.parse();
      if (var3.hasErrors()) {
         this.error("object parser errors for " + this.getName() + ":\n\n" + var3.getErrors());
         return new byte[0];
      } else if (var3.getInfo().is64() && "x86".equals(this.arch)) {
         this.error("Can't run x64 object " + this.getName() + " in x86 session");
         return new byte[0];
      } else if (var3.getInfo().is86() && "x64".equals(this.arch)) {
         this.error("Can't run x86 object " + this.getName() + " in x64 session");
         return new byte[0];
      } else {
         byte[] var4 = var3.getCode();
         byte[] var5 = var3.getRData();
         byte[] var6 = var3.getData();
         byte[] var7 = var3.getRelocations();
         CommandBuilder var8 = new CommandBuilder();
         var8.setCommand(100);
         var8.addInteger(var3.getEntryPoint());
         var8.addLengthAndString(var4);
         var8.addLengthAndString(var5);
         var8.addLengthAndString(var6);
         var8.addLengthAndString(var7);
         var8.addLengthAndString(var1);
         if (var3.hasErrors()) {
            this.error("linker errors for " + this.getName() + ":\n\n" + var3.getErrors());
            return new byte[0];
         } else {
            return var8.build();
         }
      }
   }
}

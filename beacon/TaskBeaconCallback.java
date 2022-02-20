package beacon;

import common.Packer;

public class TaskBeaconCallback {
   protected CommandBuilder builder = new CommandBuilder();

   protected byte[] taskNoArgsCallback(int var1, int var2) {
      this.builder.setCommand(var1);
      this.builder.addInteger(var2);
      return this.builder.build();
   }

   public byte[] IPConfig(int var1, String var2) {
      Packer var3 = new Packer();
      var3.addInt(var1);
      byte[] var4 = var3.getBytes();
      BeaconObjectTask var5 = new BeaconObjectTask("resources/interfaces." + var2 + ".o", var2, "go");
      return var5.build(var4);
   }

   public byte[] Ps(int var1) {
      return this.taskNoArgsCallback(32, var1);
   }

   public byte[] Ls(int var1, String var2) {
      this.builder.setCommand(53);
      this.builder.addInteger(var1);
      if (var2.endsWith("\\")) {
         this.builder.addLengthAndString(var2 + "*");
      } else {
         this.builder.addLengthAndString(var2 + "\\*");
      }

      return this.builder.build();
   }

   public byte[] Drives(int var1) {
      return this.taskNoArgsCallback(55, var1);
   }
}

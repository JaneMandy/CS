package beacon.pivots;

import aggressor.DataUtils;
import beacon.BeaconPivot;
import beacon.TaskBeacon;
import common.BeaconOutput;
import common.CommonUtils;
import dialog.DialogUtils;

public class BrowserPivotPortForward extends BeaconPivot {
   public void die() {
      String[] var1 = new String[]{this.bid};
      this.client.getConnection().call("beacons.log_write", CommonUtils.args(BeaconOutput.Input(this.bid, "browserpivot stop")));
      (new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), var1)).BrowserPivotStop();
   }

   public void tunnel() {
      String var1 = DataUtils.getTeamServerIP(this.client.getData());
      DialogUtils.presentText("Browser Pivoting", "Use this command to run the open source Chromium browser<br />and have it use your browser pivot.", "chromium --ignore-certificate-errors --proxy-server=" + var1 + ":" + this.port);
   }
}

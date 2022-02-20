package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import aggressor.dialogs.ElevateDialog;
import aggressor.dialogs.OneLinerDialog;
import beacon.BeaconCommands;
import beacon.EncodedCommandBuilder;
import beacon.PowerShellTasks;
import beacon.TaskBeacon;
import beacon.bof.UserSpecifiedFull;
import common.BeaconEntry;
import common.BeaconOutput;
import common.Callback;
import common.CommonUtils;
import common.ListenerUtils;
import common.Packer;
import common.ScListener;
import common.TeamQueue;
import cortana.Cortana;
import dialog.DialogUtils;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Predicate;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class BeaconBridge implements Function, Loadable, Predicate {
   protected Cortana engine;
   protected TeamQueue conn;
   protected AggressorClient client;

   public BeaconBridge(AggressorClient var1, Cortana var2, TeamQueue var3) {
      this.client = var1;
      this.engine = var2;
      this.conn = var3;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&externalc2_start", this);
      Cortana.put(var1, "&beacon_commands", this);
      Cortana.put(var1, "&ssh_commands", this);
      Cortana.put(var1, "&beacon_command_describe", this);
      Cortana.put(var1, "&ssh_command_describe", this);
      Cortana.put(var1, "&beacon_command_detail", this);
      Cortana.put(var1, "&ssh_command_detail", this);
      Cortana.put(var1, "&beacons", this);
      Cortana.put(var1, "&beacon_data", this);
      Cortana.put(var1, "&bdata", this);
      Cortana.put(var1, "&beacon_info", this);
      Cortana.put(var1, "&binfo", this);
      Cortana.put(var1, "&beacon_note", this);
      Cortana.put(var1, "&beacon_remove", this);
      Cortana.put(var1, "&beacon_command_register", this);
      Cortana.put(var1, "&ssh_command_register", this);
      Cortana.put(var1, "&beacon_ids", this);
      Cortana.put(var1, "&beacon_host_script", this);
      Cortana.put(var1, "&beacon_host_imported_script", this);
      Cortana.put(var1, "&beacon_execute_job", this);
      Cortana.put(var1, "&barch", this);
      Cortana.put(var1, "&beacon_stage_tcp", this);
      Cortana.put(var1, "&beacon_stage_pipe", this);
      Cortana.put(var1, "&bof_pack", this);
      Cortana.put(var1, "&beacon_inline_execute", this);
      Cortana.put(var1, "&bls", this);
      Cortana.put(var1, "&bps", this);
      Cortana.put(var1, "&bipconfig", this);
      Cortana.put(var1, "&openOrActivate", this);
      Cortana.put(var1, "&openBypassUACDialog", this);
      Cortana.put(var1, "&openElevateDialog", this);
      Cortana.put(var1, "&openOneLinerDialog", this);
      var1.getScriptEnvironment().getEnvironment().put("-isssh", this);
      var1.getScriptEnvironment().getEnvironment().put("-isbeacon", this);
      var1.getScriptEnvironment().getEnvironment().put("-isadmin", this);
      var1.getScriptEnvironment().getEnvironment().put("-is64", this);
      var1.getScriptEnvironment().getEnvironment().put("-isactive", this);
   }

   public boolean decide(String var1, ScriptInstance var2, Stack var3) {
      String var4 = BridgeUtilities.getString(var3, "");
      BeaconEntry var5 = DataUtils.getBeacon(this.client.getData(), var4);
      if (var5 == null) {
         return false;
      } else if ("-isssh".equals(var1)) {
         return var5.isSSH();
      } else if ("-isbeacon".equals(var1)) {
         return var5.isBeacon();
      } else if ("-isadmin".equals(var1)) {
         return var5.isAdmin();
      } else if ("-is64".equals(var1)) {
         return var5.is64();
      } else {
         return "-isactive".equals(var1) ? var5.isActive() : false;
      }
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public static String[] bids(Stack var0) {
      if (var0.isEmpty()) {
         return new String[0];
      } else {
         Scalar var1 = (Scalar)var0.peek();
         return var1.getArray() != null ? CommonUtils.toStringArray(BridgeUtilities.getArray(var0)) : new String[]{((Scalar)var0.pop()).stringValue()};
      }
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      int var24;
      if (var1.equals("&externalc2_start")) {
         var4 = BridgeUtilities.getString(var3, "0.0.0.0");
         var24 = BridgeUtilities.getInt(var3, 2222);
         this.conn.call("exoticc2.start", CommonUtils.args(var4, var24));
         return SleepUtils.getEmptyScalar();
      } else {
         BeaconCommands var34;
         if (var1.equals("&beacon_commands")) {
            var34 = DataUtils.getBeaconCommands(this.client.getData());
            return SleepUtils.getArrayWrapper(var34.commands());
         } else if (var1.equals("&ssh_commands")) {
            var34 = DataUtils.getSSHCommands(this.client.getData());
            return SleepUtils.getArrayWrapper(var34.commands());
         } else {
            BeaconCommands var33;
            if (var1.equals("&beacon_command_describe")) {
               var4 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getBeaconCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDescription(var4));
            } else if (var1.equals("&ssh_command_describe")) {
               var4 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getSSHCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDescription(var4));
            } else if (var1.equals("&beacon_command_detail")) {
               var4 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getBeaconCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDetails(var4));
            } else if (var1.equals("&ssh_command_detail")) {
               var4 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getSSHCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDetails(var4));
            } else {
               String var6;
               final String var12;
               BeaconCommands var31;
               if (var1.equals("&beacon_command_register")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  var12 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var31 = DataUtils.getBeaconCommands(this.client.getData());
                  var31.register(var4, var12, var6);
               } else if (var1.equals("&ssh_command_register")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  var12 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var31 = DataUtils.getSSHCommands(this.client.getData());
                  var31.register(var4, var12, var6);
               } else {
                  String[] var11;
                  int var16;
                  if (var1.equals("&beacon_note")) {
                     var11 = bids(var3);
                     var12 = BridgeUtilities.getString(var3, "");

                     for(var16 = 0; var16 < var11.length; ++var16) {
                        this.conn.call("beacons.note", CommonUtils.args(var11[var16], var12));
                        this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Note(var11[var16], var12)));
                     }
                  } else if (var1.equals("&beacon_remove")) {
                     var11 = bids(var3);

                     for(var24 = 0; var24 < var11.length; ++var24) {
                        this.conn.call("beacons.remove", CommonUtils.args(var11[var24]));
                     }
                  } else {
                     Map var25;
                     if (var1.equals("&beacons")) {
                        var25 = DataUtils.getBeacons(this.client.getData());
                        return CommonUtils.convertAll(new LinkedList(var25.values()));
                     }

                     if (var1.equals("&beacon_ids")) {
                        var25 = DataUtils.getBeacons(this.client.getData());
                        return CommonUtils.convertAll(new LinkedList(var25.keySet()));
                     }

                     int var18;
                     if (!var1.equals("&beacon_execute_job")) {
                        if (var1.equals("&beacon_host_imported_script")) {
                           var4 = BridgeUtilities.getString(var3, "");
                           return SleepUtils.getScalar((new PowerShellTasks(this.client, var4)).getImportCradle());
                        }

                        if (var1.equals("&beacon_host_script")) {
                           var4 = BridgeUtilities.getString(var3, "");
                           var12 = BridgeUtilities.getString(var3, "");
                           return SleepUtils.getScalar((new PowerShellTasks(this.client, var4)).getScriptCradle(var12));
                        }

                        BeaconEntry var5;
                        if (var1.equals("&barch")) {
                           var4 = BridgeUtilities.getString(var3, "");
                           var5 = DataUtils.getBeacon(this.client.getData(), var4);
                           if (var5 == null) {
                              return SleepUtils.getScalar("x86");
                           }

                           return SleepUtils.getScalar(var5.arch());
                        }

                        if (!var1.equals("&beacon_info") && !var1.equals("&binfo")) {
                           if (!var1.equals("&beacon_data") && !var1.equals("&bdata")) {
                              final SleepClosure var13;
                              final String var7;
                              if (var1.equals("&bipconfig")) {
                                 var11 = bids(var3);
                                 var13 = BridgeUtilities.getFunction(var3, var2);

                                 for(var16 = 0; var16 < var11.length; ++var16) {
                                    var7 = var11[var16];
                                    this.conn.call("beacons.task_ipconfig", CommonUtils.args(var11[var16]), new Callback() {
                                       public void result(String var1, Object var2) {
                                          Stack var3 = new Stack();
                                          var3.push(CommonUtils.convertAll(var2));
                                          var3.push(SleepUtils.getScalar(var7));
                                          SleepUtils.runCode(var13, var1, (ScriptInstance)null, var3);
                                       }
                                    });
                                 }

                                 return SleepUtils.getEmptyScalar();
                              }

                              TaskBeacon var10;
                              final String var7;
                              if (var1.equals("&bls")) {
                                 var11 = bids(var3);
                                 var12 = BridgeUtilities.getString(var3, ".");
                                 if (var3.isEmpty()) {
                                    var10 = new TaskBeacon(this.client, this.client.getData(), this.conn, var11);
                                    var10.Ls(var12);
                                    return SleepUtils.getEmptyScalar();
                                 }

                                 final SleepClosure var17 = BridgeUtilities.getFunction(var3, var2);

                                 for(var18 = 0; var18 < var11.length; ++var18) {
                                    var7 = var11[var18];
                                    this.conn.call("beacons.task_ls", CommonUtils.args(var11[var18], var12), new Callback() {
                                       public void result(String var1, Object var2) {
                                          Stack var3 = new Stack();
                                          var3.push(CommonUtils.convertAll(var2));
                                          var3.push(SleepUtils.getScalar(var12));
                                          var3.push(SleepUtils.getScalar(var7));
                                          SleepUtils.runCode(var17, var1, (ScriptInstance)null, var3);
                                       }
                                    });
                                 }

                                 return SleepUtils.getEmptyScalar();
                              }

                              if (!var1.equals("&bps")) {
                                 if (var1.equals("&beacon_stage_tcp")) {
                                    var4 = BridgeUtilities.getString(var3, "");
                                    var12 = BridgeUtilities.getString(var3, "127.0.0.1");
                                    var16 = BridgeUtilities.getInt(var3, 0);
                                    var7 = BridgeUtilities.getString(var3, "");
                                    String var8 = BridgeUtilities.getString(var3, "x86");
                                    ScListener var9 = ListenerUtils.getListener(this.client, var7);
                                    var10 = new TaskBeacon(this.client, this.client.getData(), this.conn, new String[]{var4});
                                    var10.StageTCP(var4, var12, var16, var8, var9);
                                    return SleepUtils.getEmptyScalar();
                                 }

                                 if (var1.equals("&beacon_stage_pipe")) {
                                    var4 = BridgeUtilities.getString(var3, "");
                                    var12 = BridgeUtilities.getString(var3, "127.0.0.1");
                                    var6 = BridgeUtilities.getString(var3, "");
                                    var7 = BridgeUtilities.getString(var3, "x86");
                                    ScListener var21 = ListenerUtils.getListener(this.client, var6);
                                    String var26 = var21.getConfig().getStagerPipe();
                                    var10 = new TaskBeacon(this.client, this.client.getData(), this.conn, new String[]{var4});
                                    var10.StagePipe(var4, var12, var26, var7, var21);
                                    return SleepUtils.getEmptyScalar();
                                 }

                                 if (var1.equals("&openOrActivate")) {
                                    var11 = bids(var3);
                                    if (var11.length == 1) {
                                       DialogUtils.openOrActivate(this.client, var11[0]);
                                       return SleepUtils.getEmptyScalar();
                                    }

                                    return SleepUtils.getEmptyScalar();
                                 }

                                 if (var1.equals("&openBypassUACDialog")) {
                                    throw new RuntimeException(var1 + " was removed in Cobalt Strike 4.1");
                                 }

                                 if (var1.equals("&openElevateDialog")) {
                                    var11 = bids(var3);
                                    (new ElevateDialog(this.client, var11)).show();
                                    return SleepUtils.getEmptyScalar();
                                 }

                                 if (var1.equals("&openOneLinerDialog")) {
                                    var11 = bids(var3);
                                    (new OneLinerDialog(this.client, var11)).show();
                                 } else {
                                    if (var1.equals("&bof_pack")) {
                                       var4 = BridgeUtilities.getString(var3, "");
                                       var12 = BridgeUtilities.getString(var3, "");
                                       Packer var30 = new Packer();

                                       for(var18 = 0; var18 < var12.length(); ++var18) {
                                          char var27 = var12.charAt(var18);
                                          if (var27 != ' ') {
                                             if (var3.isEmpty()) {
                                                throw new RuntimeException("No argument corresponds to '" + var27 + "'");
                                             }

                                             if (var27 == 'b') {
                                                var30.addLengthAndString(BridgeUtilities.getString(var3, ""));
                                             } else if (var27 == 'i') {
                                                var30.addInt(BridgeUtilities.getInt(var3, 0));
                                             } else if (var27 == 's') {
                                                var30.addShort((short)BridgeUtilities.getInt(var3, 0));
                                             } else if (var27 == 'z') {
                                                var30.addLengthAndEncodedStringASCIIZ(this.client, var4, BridgeUtilities.getString(var3, ""));
                                             } else {
                                                if (var27 != 'Z') {
                                                   throw new RuntimeException("Invalid character in BOF: '" + var27 + "'");
                                                }

                                                var30.addLengthAndWideStringASCIIZ(BridgeUtilities.getString(var3, ""));
                                             }
                                          }
                                       }

                                       return SleepUtils.getScalar(var30.getBytes());
                                    }

                                    if (var1.equals("&beacon_inline_execute")) {
                                       var4 = BridgeUtilities.getString(var3, "");
                                       byte[] var20 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
                                       var6 = BridgeUtilities.getString(var3, "");
                                       byte[] var22 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
                                       if (var20.length == 0) {
                                          throw new RuntimeException("The BOF content (arg 2) is empty. Did you read the right file?");
                                       }

                                       UserSpecifiedFull var23 = new UserSpecifiedFull(this.client, var20, var6, var22);
                                       var23.go(var4);
                                       return SleepUtils.getEmptyScalar();
                                    }
                                 }

                                 return SleepUtils.getEmptyScalar();
                              }

                              var11 = bids(var3);
                              if (var3.isEmpty()) {
                                 var10 = new TaskBeacon(this.client, this.client.getData(), this.conn, var11);
                                 var10.Ps();
                                 return SleepUtils.getEmptyScalar();
                              }

                              var13 = BridgeUtilities.getFunction(var3, var2);

                              for(var16 = 0; var16 < var11.length; ++var16) {
                                 var7 = var11[var16];
                                 this.conn.call("beacons.task_ps", CommonUtils.args(var11[var16]), new Callback() {
                                    public void result(String var1, Object var2) {
                                       Stack var3 = new Stack();
                                       var3.push(CommonUtils.convertAll(var2));
                                       var3.push(SleepUtils.getScalar(var7));
                                       SleepUtils.runCode(var13, var1, (ScriptInstance)null, var3);
                                    }
                                 });
                              }

                              return SleepUtils.getEmptyScalar();
                           }

                           var4 = BridgeUtilities.getString(var3, "");
                           var12 = BridgeUtilities.getString(var3, "");
                           BeaconEntry var14 = DataUtils.getBeacon(this.client.getData(), var4);
                           if (var14 == null) {
                              return SleepUtils.getEmptyScalar();
                           }

                           return CommonUtils.convertAll(var14.toMap());
                        }

                        var4 = BridgeUtilities.getString(var3, "");
                        var5 = DataUtils.getBeacon(this.client.getData(), var4);
                        if (var5 == null) {
                           return SleepUtils.getEmptyScalar();
                        }

                        if (!var3.isEmpty()) {
                           var6 = BridgeUtilities.getString(var3, "");
                           return CommonUtils.convertAll(var5.toMap().get(var6));
                        }

                        return CommonUtils.convertAll(var5.toMap());
                     }

                     var11 = bids(var3);
                     var12 = BridgeUtilities.getString(var3, "");
                     var6 = BridgeUtilities.getString(var3, "");
                     var18 = BridgeUtilities.getInt(var3, 0);

                     for(int var28 = 0; var28 < var11.length; ++var28) {
                        EncodedCommandBuilder var29 = new EncodedCommandBuilder(this.client);
                        var29.setCommand(78);
                        var29.addLengthAndEncodedString(var11[var28], var12);
                        var29.addLengthAndEncodedString(var11[var28], var6);
                        var29.addShort(var18);
                        byte[] var32 = var29.build();
                        this.conn.call("beacons.task", CommonUtils.args(var11[var28], var32));
                     }
                  }
               }

               return SleepUtils.getEmptyScalar();
            }
         }
      }
   }
}

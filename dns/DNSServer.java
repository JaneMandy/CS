package dns;

import common.CommonUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DNSServer implements Runnable {
   protected Thread fred;
   protected DatagramSocket server;
   protected DatagramPacket in = new DatagramPacket(new byte[512], 512);
   protected DatagramPacket out = new DatagramPacket(new byte[512], 512);
   protected DNSServer.Handler listener = null;
   protected int ttl = 1;
   protected boolean dropNSRequest = true;
   public static final int DNS_TYPE_A = 1;
   public static final int DNS_TYPE_AAAA = 28;
   public static final int DNS_TYPE_CNAME = 5;
   public static final int DNS_TYPE_TXT = 16;
   public static final int DNS_TYPE_NS = 2;
   protected boolean isup = true;

   public void setDefaultTTL(int var1) {
      this.ttl = var1;
   }

   public void setNSResponse(String var1) {
      if (var1 != null && !"".equals(var1) && !"drop".equals(var1)) {
         this.dropNSRequest = false;
      } else {
         this.dropNSRequest = true;
      }

   }

   public static DNSServer.Response A(long var0) {
      DNSServer.Response var2 = new DNSServer.Response();
      var2.type = 1;
      var2.addr4 = var0;
      return var2;
   }

   public static DNSServer.Response TXT(byte[] var0) {
      DNSServer.Response var1 = new DNSServer.Response();
      var1.type = 16;
      var1.data = var0;
      return var1;
   }

   public static DNSServer.Response AAAA(byte[] var0) {
      DNSServer.Response var1 = new DNSServer.Response();
      var1.type = 28;
      var1.data = var0;
      return var1;
   }

   public void installHandler(DNSServer.Handler var1) {
      this.listener = var1;
   }

   public static String typeToString(int var0) {
      switch(var0) {
      case 1:
         return "A";
      case 2:
         return "NS";
      case 5:
         return "CNAME";
      case 12:
         return "PTR";
      case 15:
         return "MX";
      case 16:
         return "TXT";
      case 28:
         return "AAAA";
      default:
         return "qtype:" + var0;
      }
   }

   public byte[] respond(byte[] var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(512);
      DataOutputStream var3 = new DataOutputStream(var2);
      DNSServer._A var4 = new DNSServer._A(var1, this.dropNSRequest);
      var4.A = (short)var4.A | '耀';
      ++var4.F;
      var4.E = 0;
      var4.B = 0;
      Iterator var5 = var4.A().iterator();

      DNSServer._B var6;
      DNSServer._C var7;
      while(var5.hasNext()) {
         var6 = (DNSServer._B)var5.next();
         var7 = new DNSServer._C(var6);
         var6.A(var7);
      }

      var5 = var4.A().iterator();

      while(var5.hasNext()) {
         var6 = (DNSServer._B)var5.next();
         var7 = var6.C();
         if (var6.A() == 28 && var7.A() == 1) {
            --var4.F;
         }
      }

      var3.writeShort(var4.H);
      var3.writeShort(var4.A);
      var3.writeShort(var4.G);
      var3.writeShort(var4.F);
      var3.writeShort(var4.E);
      var3.writeShort(var4.B);
      var5 = var4.A().iterator();

      DNSServer._B var10;
      for(int var9 = 12; var5.hasNext(); var9 += var10.B()) {
         var10 = (DNSServer._B)var5.next();
         var3.write(var1, var9, var10.B());
      }

      var5 = var4.A().iterator();

      while(true) {
         while(var5.hasNext()) {
            var10 = (DNSServer._B)var5.next();
            DNSServer._C var8 = var10.C();
            if (var10.A() == 28 && var8.A() == 1) {
               CommonUtils.print_warn("Dropped AAAA request for: " + var10.E + " (A request expected)");
            } else {
               var3.write(var8.B());
            }
         }

         var3.close();
         return var2.toByteArray();
      }
   }

   public DNSServer(int var1) throws IOException {
      this.server = new DatagramSocket(var1);
   }

   public void stop() {
      this.isup = false;
      this.fred.interrupt();

      try {
         this.server.close();
      } catch (Exception var2) {
      }

   }

   public void run() {
      while(this.isup) {
         try {
            this.server.receive(this.in);
            DNSServer._A var1 = new DNSServer._A(this.in.getData(), this.dropNSRequest);
            if (!var1.B()) {
               this.out.setAddress(this.in.getAddress());
               this.out.setPort(this.in.getPort());
               this.out.setData(this.respond(this.in.getData()));
               this.server.send(this.out);
            }
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

      try {
         this.server.close();
      } catch (Exception var3) {
      }

      CommonUtils.print_info("DNS server stopped");
   }

   public void go() {
      this.fred = new Thread(this);
      this.fred.start();
   }

   private static class _A {
      public int H;
      public int A;
      public int G;
      public int F;
      public int E;
      public int B;
      private boolean D = true;
      protected List C = new LinkedList();

      public _A(byte[] var1, boolean var2) throws IOException {
         this.D = var2;
         DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1));
         this.H = var3.readUnsignedShort();
         this.A = var3.readUnsignedShort();
         this.G = var3.readUnsignedShort();
         this.F = var3.readUnsignedShort();
         this.E = var3.readUnsignedShort();
         this.B = var3.readUnsignedShort();

         for(int var4 = 0; var4 < this.G; ++var4) {
            DNSServer._B var5 = new DNSServer._B(var3);
            this.C.add(var5);
         }

      }

      public boolean B() {
         if (this.G != 1) {
            return false;
         } else {
            DNSServer._B var1 = (DNSServer._B)this.C.get(0);
            int var2 = var1.A();
            if (var2 != 16 && var2 != 1 && var2 != 28) {
               if (var2 == 2 && !this.D) {
                  return false;
               } else {
                  CommonUtils.print_warn("Dropped DNS " + DNSServer.typeToString(var2) + " request for: " + var1.E + " (unexpected request)");
                  return true;
               }
            } else {
               return false;
            }
         }
      }

      public List A() {
         return this.C;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("DNS Header\n");
         var1.append("ID:      " + Integer.toHexString(this.H) + "\n");
         var1.append("Flags:   " + Integer.toBinaryString(this.A) + "\n");
         var1.append("QdCount: " + this.G + "\n");
         var1.append("AnCount: " + this.F + "\n");
         var1.append("NsCount: " + this.E + "\n");
         var1.append("ArCount: " + this.B + "\n");
         Iterator var2 = this.C.iterator();

         while(var2.hasNext()) {
            DNSServer._B var3 = (DNSServer._B)var2.next();
            var1.append(var3);
         }

         return var1.toString();
      }
   }

   private static class _B {
      public String E;
      public int A;
      public int C;
      public int B = 0;
      public DNSServer._C D = null;

      public _B(DataInputStream var1) throws IOException {
         StringBuffer var2 = new StringBuffer();
         int var3 = var1.readUnsignedByte();
         ++this.B;

         while(var3 > 0) {
            for(int var4 = 0; var4 < var3; ++var4) {
               var2.append((char)var1.readUnsignedByte());
               ++this.B;
            }

            var2.append(".");
            var3 = var1.readUnsignedByte();
            ++this.B;
         }

         this.E = var2.toString();
         this.A = var1.readUnsignedShort();
         this.C = var1.readUnsignedShort();
         this.B += 4;
      }

      public DNSServer._C C() {
         return this.D;
      }

      public void A(DNSServer._C var1) {
         this.D = var1;
      }

      public int A() {
         return this.A;
      }

      public int B() {
         return this.B;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("\tQuestion: '" + this.E + "' size: " + this.B + " bytes\n");
         var1.append("\tQType:    " + Integer.toHexString(this.A) + "\n");
         var1.append("\tQClass:   " + Integer.toHexString(this.C) + "\n\n");
         return var1.toString();
      }
   }

   private class _C {
      protected ByteArrayOutputStream B = new ByteArrayOutputStream(512);
      protected int C;

      public byte[] B() {
         return this.B.toByteArray();
      }

      public int A() {
         return this.C;
      }

      public _C(DNSServer._B var2) throws IOException {
         DataOutputStream var3 = new DataOutputStream(this.B);
         String[] var4 = var2.E.split("\\.");

         int var6;
         for(int var5 = 0; var5 < var4.length; ++var5) {
            var3.writeByte(var4[var5].length());

            for(var6 = 0; var6 < var4[var5].length(); ++var6) {
               var3.writeByte(var4[var5].charAt(var6));
            }
         }

         var3.writeByte(0);
         if (DNSServer.this.listener != null) {
            DNSServer.Response var7 = DNSServer.this.listener.respond(var2.E, var2.A());
            if (var7 == null) {
               CommonUtils.print_error("Response for question is null\n" + var2);
               var7 = DNSServer.A(0L);
            }

            var3.writeShort(var7.type);
            var3.writeShort(1);
            var3.writeInt(DNSServer.this.ttl);
            this.C = var7.type;
            label39:
            switch(var7.type) {
            case 1:
               var3.writeShort(4);
               var3.writeInt((int)var7.addr4);
               break;
            case 16:
               var3.writeShort(var7.data.length + 1);
               var3.writeByte(var7.data.length);
               var6 = 0;

               while(true) {
                  if (var6 >= var7.data.length) {
                     break label39;
                  }

                  var3.writeByte(var7.data[var6]);
                  ++var6;
               }
            case 28:
               var3.writeShort(16);

               for(var6 = 0; var6 < 16; ++var6) {
                  if (var6 < var7.data.length) {
                     var3.writeByte(var7.data[var6]);
                  } else {
                     var3.writeByte(0);
                  }
               }
            }
         }

         var3.close();
      }
   }

   public interface Handler {
      DNSServer.Response respond(String var1, int var2);
   }

   public static final class Response {
      public int type = 0;
      public long addr4;
      public long[] addr6;
      public byte[] data;
   }
}

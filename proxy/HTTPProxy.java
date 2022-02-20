package proxy;

import common.CommonUtils;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLHandshakeException;
import socks.Mortal;
import ssl.SecureProxySocket;

public class HTTPProxy implements Runnable, Mortal {
   protected String server = "";
   protected int port = 0;
   protected int sport = 0;
   protected List listeners = new LinkedList();
   protected ServerSocket pserver = null;
   protected boolean alive = true;
   protected long requests = 0L;
   protected long rx = 0L;
   protected long fails = 0L;

   public void die() {
   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      var1.put("type", "browser pivot http proxy");
      var1.put("port", this.sport.makeConcatWithConstants<invokedynamic>(this.sport));
      var1.put("fhost", "127.0.0.1");
      var1.put("fport", this.port.makeConcatWithConstants<invokedynamic>(this.port));
      return var1;
   }

   public void addProxyListener(HTTPProxyEventListener var1) {
      this.listeners.add(var1);
   }

   public void fireEvent(int var1, String var2) {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((HTTPProxyEventListener)var3.next()).proxyEvent(var1, var2);
      }

   }

   public void stop() {
      this.alive = false;

      try {
         this.pserver.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public HTTPProxy(int var1, String var2, int var3) throws IOException {
      this.server = var2;
      this.port = var3;
      this.sport = var1;
      this.pserver = new ServerSocket(var1, 128);
   }

   public int getPort() {
      return -1;
   }

   public int getLocalPort() {
      return this.port;
   }

   public void start() {
      (new Thread(this, "Browser Pivot HTTP Proxy Server (port " + this.sport + ")")).start();
   }

   private static final int B(String var0, int var1, StringBuffer var2) {
      var2.append(var0 + "\r\n");
      if (var0.toLowerCase().startsWith("content-length: ")) {
         try {
            return Integer.parseInt(var0.substring(16).trim());
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return var1;
   }

   private static final int C(String var0, int var1, StringBuffer var2) {
      Set var3 = CommonUtils.toSet("strict-transport-security, expect-ct, alt-svc");
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = var4.next() + ": ";
         if (var0.toLowerCase().startsWith(var5)) {
            return var1;
         }
      }

      var2.append(var0 + "\r\n");
      if (var0.toLowerCase().startsWith("content-length: ")) {
         try {
            return Integer.parseInt(var0.substring(16).trim());
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      return var1;
   }

   private static String A(DataInputStream var0) throws IOException {
      StringBuffer var1 = new StringBuffer();

      while(true) {
         int var2 = var0.readUnsignedByte();
         if ((char)var2 == '\n') {
            return var1.toString();
         }

         if ((char)var2 == '\r') {
            boolean var3 = false;
         } else {
            var1.append((char)var2);
         }
      }
   }

   public void run() {
      while(true) {
         try {
            if (this.alive) {
               Socket var1 = this.pserver.accept();
               var1.setSoTimeout(60000);
               new HTTPProxy.ProxyClient(var1);
               continue;
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }

         return;
      }
   }

   public class ProxyClient implements Runnable {
      protected Socket socket = null;
      protected Socket proxy = null;

      public ProxyClient(Socket var2) {
         this.socket = var2;
         (new Thread(this, "Browser Pivot HTTP Request")).start();
      }

      public void run() {
         String var1 = "";
         DataOutputStream var2 = null;
         boolean var3 = false;
         String var4 = "";
         boolean var32 = false;
         boolean var45 = false;

         label671: {
            String[] var47x;
            label672: {
               String[] var17xx;
               label673: {
                  label674: {
                     try {
                        var45 = true;
                        var32 = true;
                        this.proxy = new Socket(HTTPProxy.this.server, HTTPProxy.this.port);
                        this.proxy.setSoTimeout(60000);
                        StringBuffer var5 = new StringBuffer(8192);
                        InputStream var6 = this.socket.getInputStream();
                        DataInputStream var7 = new DataInputStream(var6);
                        var2 = new DataOutputStream(this.socket.getOutputStream());
                        var4 = HTTPProxy.A(var7);
                        var5.append(var4 + "\r\n");
                        byte var8 = 0;
                        String var9 = HTTPProxy.A(var7);

                        int var48x;
                        for(var48x = HTTPProxy.B(var9, var8, var5); var9.length() > 0; var48x = HTTPProxy.B(var9, var48x, var5)) {
                           var9 = HTTPProxy.A(var7);
                        }

                        if (var4.startsWith("CONNECT")) {
                           var2.writeBytes("HTTP/1.1 200 Connection established\r\n\r\n");
                           String[] var10 = var4.split(" ");
                           String var11 = var10[1];
                           if (var11.endsWith(":443")) {
                              var11 = var11.substring(0, var11.length() - 4);
                           }

                           var5 = new StringBuffer(8192);
                           this.socket = (new SecureProxySocket(this.socket)).getSocket();
                           var7 = new DataInputStream(this.socket.getInputStream());
                           var2 = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
                           var4 = HTTPProxy.A(var7);
                           if (var4.startsWith("GET ")) {
                              var5.append("GET https://" + var11 + var4.substring(4) + "\r\n");
                           } else if (var4.startsWith("POST ")) {
                              var5.append("POST https://" + var11 + var4.substring(5) + "\r\n");
                           }

                           var8 = 0;
                           var9 = HTTPProxy.A(var7);

                           for(var48x = HTTPProxy.B(var9, var8, var5); var9.length() > 0; var48x = HTTPProxy.B(var9, var48x, var5)) {
                              var9 = HTTPProxy.A(var7);
                           }
                        }

                        DataOutputStream var49x = new DataOutputStream(new BufferedOutputStream(this.proxy.getOutputStream()));
                        var49x.writeBytes(var5.toString());
                        var49x.flush();
                        if (var48x > 0) {
                           byte[] var50x = new byte[var48x];

                           int var51x;
                           for(boolean var16 = false; var48x > 0; var48x -= var51x) {
                              var51x = var7.read(var50x);
                              var49x.write(var50x, 0, var51x);
                              var49x.flush();
                           }
                        }

                        DataInputStream var52x = new DataInputStream(this.proxy.getInputStream());
                        var5 = new StringBuffer(8192);
                        var9 = HTTPProxy.A(var52x);
                        var8 = 0;

                        for(var48x = HTTPProxy.C(var9, var8, var5); var9.length() > 0; var48x = HTTPProxy.C(var9, var48x, var5)) {
                           var9 = HTTPProxy.A(var52x);
                        }

                        HTTPProxy var10000 = HTTPProxy.this;
                        var10000.rx += (long)var48x;
                        if (var48x == 0) {
                           var2.writeBytes(var5.toString());
                           var2.flush();
                           var3 = true;
                        } else {
                           byte[] var53x = new byte[var48x];
                           boolean var13 = false;
                           byte var14 = 0;

                           int var54x;
                           for(int var15 = 0; var48x > 0; var15 += var54x) {
                              var54x = var52x.read(var53x);
                              if (var54x <= 0) {
                                 throw new IOException("incomplete read " + var14 + ", need: " + var48x + " bytes, read: " + var15 + " bytes");
                              }

                              if (!var3) {
                                 var2.writeBytes(var5.toString());
                                 var2.flush();
                                 var3 = true;
                              }

                              var2.write(var53x, 0, var54x);
                              var2.flush();
                              var48x -= var54x;
                           }
                        }

                        ++HTTPProxy.this.requests;
                        var32 = false;
                        var45 = false;
                        break label672;
                     } catch (SSLHandshakeException var64) {
                        HTTPProxy.this.fireEvent(0, "add to trusted hosts: " + var1);
                        ++HTTPProxy.this.fails;
                        var3 = true;
                        var32 = false;
                        var45 = false;
                     } catch (SocketException var65) {
                        HTTPProxy.this.fireEvent(1, "browser proxy refused connection.");
                        ++HTTPProxy.this.fails;
                        var32 = false;
                        var45 = false;
                        break label673;
                     } catch (Exception var66) {
                        ++HTTPProxy.this.fails;
                        var32 = false;
                        var45 = false;
                        break label674;
                     } finally {
                        if (var45) {
                           if (var32) {
                              try {
                                 try {
                                    if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                                       String[] var17 = var4.split(" ");
                                       var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var17[1] + "\r\n\r\n");
                                       var2.flush();
                                    }
                                 } catch (Exception var46) {
                                 }

                                 if (this.socket != null) {
                                    this.socket.close();
                                 }

                                 if (this.proxy != null) {
                                    this.proxy.close();
                                 }
                              } catch (Exception var47) {
                              }
                           }

                        }
                     }

                     if (var32) {
                        try {
                           try {
                              if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                                 String[] var17x = var4.split(" ");
                                 var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var17x[1] + "\r\n\r\n");
                                 var2.flush();
                              }
                           } catch (Exception var62) {
                           }

                           if (this.socket != null) {
                              this.socket.close();
                           }

                           if (this.proxy != null) {
                              this.proxy.close();
                           }
                        } catch (Exception var63) {
                        }
                     }

                     try {
                        try {
                           if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                              var47x = var4.split(" ");
                              var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47x[1] + "\r\n\r\n");
                              var2.flush();
                           }
                        } catch (Exception var60) {
                        }

                        if (this.socket != null) {
                           this.socket.close();
                        }

                        if (this.proxy != null) {
                           this.proxy.close();
                        }
                     } catch (Exception var61) {
                     }
                     break label671;
                  }

                  if (var32) {
                     try {
                        try {
                           if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                              var17xx = var4.split(" ");
                              var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var17xx[1] + "\r\n\r\n");
                              var2.flush();
                           }
                        } catch (Exception var58) {
                        }

                        if (this.socket != null) {
                           this.socket.close();
                        }

                        if (this.proxy != null) {
                           this.proxy.close();
                        }
                     } catch (Exception var59) {
                     }
                  }

                  try {
                     try {
                        if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                           var47x = var4.split(" ");
                           var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47x[1] + "\r\n\r\n");
                           var2.flush();
                        }
                     } catch (Exception var56) {
                     }

                     if (this.socket != null) {
                        this.socket.close();
                     }

                     if (this.proxy != null) {
                        this.proxy.close();
                     }
                  } catch (Exception var57) {
                  }
                  break label671;
               }

               if (var32) {
                  try {
                     try {
                        if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                           var17xx = var4.split(" ");
                           var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var17xx[1] + "\r\n\r\n");
                           var2.flush();
                        }
                     } catch (Exception var54) {
                     }

                     if (this.socket != null) {
                        this.socket.close();
                     }

                     if (this.proxy != null) {
                        this.proxy.close();
                     }
                  } catch (Exception var55) {
                  }
               }

               try {
                  try {
                     if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                        var47x = var4.split(" ");
                        var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47x[1] + "\r\n\r\n");
                        var2.flush();
                     }
                  } catch (Exception var52) {
                  }

                  if (this.socket != null) {
                     this.socket.close();
                  }

                  if (this.proxy != null) {
                     this.proxy.close();
                  }
               } catch (Exception var53) {
               }
               break label671;
            }

            if (var32) {
               try {
                  try {
                     if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                        String[] var17xxx = var4.split(" ");
                        var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var17xxx[1] + "\r\n\r\n");
                        var2.flush();
                     }
                  } catch (Exception var50) {
                  }

                  if (this.socket != null) {
                     this.socket.close();
                  }

                  if (this.proxy != null) {
                     this.proxy.close();
                  }
               } catch (Exception var51) {
               }
            }

            try {
               try {
                  if (!var3 && var2 != null && !var4.startsWith("CONNECT") && var4.trim().makeConcatWithConstants<invokedynamic>(var4.trim()).length() > 0) {
                     var47x = var4.split(" ");
                     var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47x[1] + "\r\n\r\n");
                     var2.flush();
                  }
               } catch (Exception var48) {
               }

               if (this.socket != null) {
                  this.socket.close();
               }

               if (this.proxy != null) {
                  this.proxy.close();
               }
            } catch (Exception var49) {
            }
         }

         HTTPProxy.this.fireEvent(3, HTTPProxy.this.requests + " " + HTTPProxy.this.fails + " " + HTTPProxy.this.rx);
      }
   }
}

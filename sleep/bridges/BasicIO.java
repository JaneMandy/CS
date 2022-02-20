package sleep.bridges;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;
import sleep.bridges.io.BufferObject;
import sleep.bridges.io.DataPattern;
import sleep.bridges.io.FileObject;
import sleep.bridges.io.IOObject;
import sleep.bridges.io.ProcessObject;
import sleep.bridges.io.SocketObject;
import sleep.engine.types.ObjectValue;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Predicate;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarHash;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;
import sleep.taint.TaintUtils;

public class BasicIO implements Loadable, Function {
   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("__EXEC__", TaintUtils.Tainter(TaintUtils.Sensitive(this)));
      var2.put("-eof", new BasicIO.iseof());
      var2.put("&openf", TaintUtils.Sensitive(new BasicIO.openf()));
      BasicIO.SocketFuncs var3 = new BasicIO.SocketFuncs();
      var2.put("&connect", TaintUtils.Sensitive(var3));
      var2.put("&listen", var3);
      var2.put("&exec", TaintUtils.Sensitive(new BasicIO.exec()));
      var2.put("&fork", new BasicIO.fork());
      var2.put("&allocate", this);
      var2.put("&sleep", new BasicIO.sleep());
      var2.put("&closef", new BasicIO.closef());
      var2.put("&read", new BasicIO.read());
      var2.put("&readln", TaintUtils.Tainter(new BasicIO.readln()));
      var2.put("&readAll", TaintUtils.Tainter(new BasicIO.readAll()));
      var2.put("&readc", TaintUtils.Tainter(this));
      var2.put("&readb", TaintUtils.Tainter(new BasicIO.readb()));
      var2.put("&consume", new BasicIO.consume());
      var2.put("&writeb", new BasicIO.writeb());
      var2.put("&bread", TaintUtils.Tainter(new BasicIO.bread()));
      var2.put("&bwrite", new BasicIO.bwrite());
      var2.put("&readObject", TaintUtils.Tainter(this));
      var2.put("&writeObject", this);
      var2.put("&readAsObject", TaintUtils.Tainter(this));
      var2.put("&writeAsObject", this);
      var2.put("&sizeof", this);
      var2.put("&pack", new BasicIO.pack());
      var2.put("&unpack", new BasicIO.unpack());
      var2.put("&available", new BasicIO.available());
      var2.put("&mark", new BasicIO.mark());
      var2.put("&skip", var2.get("&consume"));
      var2.put("&reset", new BasicIO.reset());
      var2.put("&wait", this);
      var2.put("&print", new BasicIO.print());
      var2.put("&setEncoding", this);
      BasicIO.println var4 = new BasicIO.println();
      var2.put("&println", var4);
      var2.put("&printf", var4);
      var2.put("&printAll", new BasicIO.printArray());
      var2.put("&printEOF", new BasicIO.printEOF());
      var2.put("&getConsole", new BasicIO.getConsoleObject());
      var2.put("&checksum", this);
      var2.put("&digest", this);
   }

   private static Checksum getChecksum(String var0) {
      if (var0.equals("Adler32")) {
         return new Adler32();
      } else {
         return var0.equals("CRC32") ? new CRC32() : null;
      }
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      IOObject var4;
      if (var1.equals("&wait")) {
         var4 = (IOObject)BridgeUtilities.getObject(var3);
         long var33 = BridgeUtilities.getLong(var3, 0L);
         return var4.wait(var2.getScriptEnvironment(), var33);
      } else {
         Checksum var7;
         Scalar var16;
         String var23;
         if (var1.equals("__EXEC__")) {
            var16 = SleepUtils.getArrayScalar();

            try {
               Process var32 = Runtime.getRuntime().exec(BridgeUtilities.getString(var3, ""), (String[])null, var2.cwd());
               IOObject var34 = SleepUtils.getIOHandle(var32.getInputStream(), (OutputStream)null);
               var7 = null;

               while((var23 = var34.readLine()) != null) {
                  var16.getArray().push(SleepUtils.getScalar(var23));
               }

               if (var32.waitFor() != 0) {
                  var2.getScriptEnvironment().flagError("abnormal termination: " + var32.exitValue());
               }
            } catch (Exception var16) {
               var2.getScriptEnvironment().flagError(var16);
            }

            return var16;
         } else {
            if (!var1.equals("&writeObject") && !var1.equals("&writeAsObject")) {
               if (!var1.equals("&readObject") && !var1.equals("&readAsObject")) {
                  if (var1.equals("&allocate")) {
                     int var19 = BridgeUtilities.getInt(var3, 32768);
                     BufferObject var31 = new BufferObject();
                     var31.allocate(var19);
                     return SleepUtils.getScalar((Object)var31);
                  }

                  IOObject var24;
                  String var18;
                  boolean var22;
                  if (var1.equals("&digest")) {
                     var16 = BridgeUtilities.getScalar(var3);
                     if (var16.objectValue() != null && var16.objectValue() instanceof IOObject) {
                        var22 = true;
                        var23 = BridgeUtilities.getString(var3, "MD5");
                        if (var23.charAt(0) == '>') {
                           var22 = false;
                           var23 = var23.substring(1);
                        }

                        var24 = (IOObject)var16.objectValue();

                        try {
                           if (var22) {
                              DigestInputStream var30 = new DigestInputStream(var24.getInputStream(), MessageDigest.getInstance(var23));
                              var24.openRead(var30);
                              return SleepUtils.getScalar((Object)var30.getMessageDigest());
                           }

                           DigestOutputStream var29 = new DigestOutputStream(var24.getOutputStream(), MessageDigest.getInstance(var23));
                           var24.openWrite(var29);
                           return SleepUtils.getScalar((Object)var29.getMessageDigest());
                        } catch (NoSuchAlgorithmException var17) {
                           var2.getScriptEnvironment().flagError(var17);
                        }
                     } else {
                        MessageDigest var25;
                        if (var16.objectValue() != null && var16.objectValue() instanceof MessageDigest) {
                           var25 = (MessageDigest)var16.objectValue();
                           return SleepUtils.getScalar(var25.digest());
                        }

                        var18 = var16.toString();
                        var23 = BridgeUtilities.getString(var3, "MD5");

                        try {
                           var25 = MessageDigest.getInstance(var23);
                           var25.update(BridgeUtilities.toByteArrayNoConversion(var18), 0, var18.length());
                           return SleepUtils.getScalar(var25.digest());
                        } catch (NoSuchAlgorithmException var18) {
                           var2.getScriptEnvironment().flagError(var18);
                        }
                     }

                     return SleepUtils.getEmptyScalar();
                  }

                  if (var1.equals("&sizeof")) {
                     return SleepUtils.getScalar(DataPattern.EstimateSize(BridgeUtilities.getString(var3, "")));
                  }

                  if (var1.equals("&setEncoding")) {
                     var4 = chooseSource(var3, 1, var2);
                     var18 = BridgeUtilities.getString(var3, "");

                     try {
                        var4.setEncoding(var18);
                     } catch (Exception var15) {
                        throw new IllegalArgumentException("&setEncoding: specified a non-existent encoding '" + var18 + "'");
                     }
                  } else {
                     if (var1.equals("&readc")) {
                        var4 = chooseSource(var3, 1, var2);
                        return SleepUtils.getScalar(var4.readCharacter());
                     }

                     if (var1.equals("&checksum")) {
                        var16 = BridgeUtilities.getScalar(var3);
                        if (var16.objectValue() != null && var16.objectValue() instanceof IOObject) {
                           var22 = true;
                           var23 = BridgeUtilities.getString(var3, "CRC32");
                           if (var23.charAt(0) == '>') {
                              var22 = false;
                              var23 = var23.substring(1);
                           }

                           var24 = (IOObject)var16.objectValue();
                           if (var22) {
                              CheckedInputStream var27 = new CheckedInputStream(var24.getInputStream(), getChecksum(var23));
                              var24.openRead(var27);
                              return SleepUtils.getScalar((Object)var27.getChecksum());
                           }

                           CheckedOutputStream var8 = new CheckedOutputStream(var24.getOutputStream(), getChecksum(var23));
                           var24.openWrite(var8);
                           return SleepUtils.getScalar((Object)var8.getChecksum());
                        }

                        if (var16.objectValue() != null && var16.objectValue() instanceof Checksum) {
                           Checksum var21 = (Checksum)var16.objectValue();
                           return SleepUtils.getScalar(var21.getValue());
                        }

                        var18 = var16.toString();
                        var23 = BridgeUtilities.getString(var3, "CRC32");
                        var7 = getChecksum(var23);
                        var7.update(BridgeUtilities.toByteArrayNoConversion(var18), 0, var18.length());
                        return SleepUtils.getScalar(var7.getValue());
                     }
                  }
               } else {
                  var4 = chooseSource(var3, 1, var2);

                  try {
                     ObjectInputStream var17 = new ObjectInputStream(var4.getReader());
                     if (var1.equals("&readAsObject")) {
                        return SleepUtils.getScalar(var17.readObject());
                     }

                     Scalar var20 = (Scalar)var17.readObject();
                     return var20;
                  } catch (EOFException var12) {
                     var4.close();
                  } catch (Exception var13) {
                     var2.getScriptEnvironment().flagError(var13);
                     var4.close();
                  }
               }
            } else {
               var4 = chooseSource(var3, 2, var2);

               while(!var3.isEmpty()) {
                  Scalar var5 = (Scalar)var3.pop();

                  try {
                     ObjectOutputStream var6 = new ObjectOutputStream(var4.getWriter());
                     if (var1.equals("&writeAsObject")) {
                        var6.writeObject(var5.objectValue());
                     } else {
                        var6.writeObject(var5);
                     }
                  } catch (Exception var14) {
                     var2.getScriptEnvironment().flagError(var14);
                     var4.close();
                  }
               }
            }

            return SleepUtils.getEmptyScalar();
         }
      }
   }

   private static IOObject chooseSource(Stack var0, int var1, ScriptInstance var2) {
      Scalar var3;
      if (var0.size() < var1 && !var0.isEmpty()) {
         var3 = (Scalar)var0.peek();
         if (var3.getActualValue() != null && var3.getActualValue().getType() == ObjectValue.class && var3.objectValue() instanceof IOObject) {
            var0.pop();
            return (IOObject)var3.objectValue();
         }
      } else if (var0.size() >= var1) {
         var3 = (Scalar)var0.pop();
         if (!(var3.objectValue() instanceof IOObject)) {
            throw new IllegalArgumentException("expected I/O handle argument, received: " + SleepUtils.describe(var3));
         }

         return (IOObject)var3.objectValue();
      }

      return IOObject.getConsole(var2.getScriptEnvironment());
   }

   private static Scalar ReadFormatted(String var0, InputStream var1, ScriptEnvironment var2, IOObject var3) {
      Scalar var4 = SleepUtils.getArrayScalar();
      DataPattern var5 = DataPattern.Parse(var0);
      byte[] var6 = new byte[8];
      ByteBuffer var7 = ByteBuffer.wrap(var6);

      for(boolean var8 = false; var5 != null; var5 = var5.next) {
         var7.order(var5.order);
         if (var5.value == 'M') {
            if (var5.count == 1) {
               var5.count = 10240;
            }

            var1.mark(var5.count);
         } else if (var5.value == 'x') {
            try {
               var1.skip((long)var5.count);
            } catch (Exception var15) {
            }
         } else {
            StringBuffer var11;
            int var12;
            int var19;
            int var13;
            int var14;
            if (var5.value != 'h' && var5.value != 'H') {
               if (var5.value != 'z' && var5.value != 'Z' && var5.value != 'U' && var5.value != 'u') {
                  for(var13 = 0; var13 != var5.count; ++var13) {
                     Scalar var22 = null;

                     try {
                        switch(var5.value) {
                        case 'B':
                           var19 = var1.read();
                           if (var19 == -1) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var19);
                           break;
                        case 'C':
                           var19 = var1.read(var6, 0, 1);
                           if (var19 < 1) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(((char)var6[0]).makeConcatWithConstants<invokedynamic>((char)var6[0]));
                           break;
                        case 'D':
                        case 'E':
                        case 'F':
                        case 'G':
                        case 'H':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'Q':
                        case 'T':
                        case 'U':
                        case 'V':
                        case 'W':
                        case 'X':
                        case 'Y':
                        case 'Z':
                        case '[':
                        case '\\':
                        case ']':
                        case '^':
                        case '_':
                        case '`':
                        case 'a':
                        case 'e':
                        case 'g':
                        case 'h':
                        case 'j':
                        case 'k':
                        case 'm':
                        case 'n':
                        case 'p':
                        case 'q':
                        case 'r':
                        default:
                           var2.showDebugMessage("unknown file pattern character: " + var5.value);
                           break;
                        case 'I':
                           var19 = var1.read(var6, 0, 4);
                           if (var19 < 4) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar((long)var7.getInt(0) & 4294967295L);
                           break;
                        case 'R':
                           var1.reset();
                           break;
                        case 'S':
                           var19 = var1.read(var6, 0, 2);
                           if (var19 < 2) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var7.getShort(0) & '\uffff');
                           break;
                        case 'b':
                           var6[0] = (byte)var1.read();
                           if (var6[0] == -1) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar((int)var6[0]);
                           break;
                        case 'c':
                           var19 = var1.read(var6, 0, 2);
                           if (var19 < 2) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var7.getChar(0).makeConcatWithConstants<invokedynamic>(var7.getChar(0)));
                           break;
                        case 'd':
                           var19 = var1.read(var6, 0, 8);
                           if (var19 < 8) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var7.getDouble(0));
                           break;
                        case 'f':
                           var19 = var1.read(var6, 0, 4);
                           if (var19 < 4) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var7.getFloat(0));
                           break;
                        case 'i':
                           var19 = var1.read(var6, 0, 4);
                           if (var19 < 4) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var7.getInt(0));
                           break;
                        case 'l':
                           var19 = var1.read(var6, 0, 8);
                           if (var19 < 8) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var7.getLong(0));
                           break;
                        case 'o':
                           ObjectInputStream var21 = new ObjectInputStream(var1);
                           var22 = (Scalar)var21.readObject();
                           break;
                        case 's':
                           var19 = var1.read(var6, 0, 2);
                           if (var19 < 2) {
                              throw new EOFException();
                           }

                           var22 = SleepUtils.getScalar(var7.getShort(0));
                        }
                     } catch (Exception var16) {
                        if (var3 != null) {
                           var3.close();
                        }

                        if (var22 != null) {
                           var4.getArray().push(var22);
                        }

                        return var4;
                     }

                     if (var22 != null) {
                        var4.getArray().push(var22);
                     }
                  }
               } else {
                  var11 = new StringBuffer();

                  try {
                     if (var5.value != 'u' && var5.value != 'U') {
                        var12 = var1.read();
                        if (var12 == -1) {
                           throw new EOFException();
                        }
                     } else {
                        var19 = var1.read(var6, 0, 2);
                        if (var19 < 2) {
                           throw new EOFException();
                        }

                        var12 = var7.getChar(0);
                     }

                     for(var13 = 1; var12 != 0 && var13 != var5.count; ++var13) {
                        var11.append((char)var12);
                        if (var5.value != 'u' && var5.value != 'U') {
                           var12 = var1.read();
                           if (var12 == -1) {
                              throw new EOFException();
                           }
                        } else {
                           var19 = var1.read(var6, 0, 2);
                           if (var19 < 2) {
                              throw new EOFException();
                           }

                           var12 = var7.getChar(0);
                        }
                     }

                     if (var12 != 0) {
                        var11.append((char)var12);
                     }

                     if ((var5.value == 'Z' || var5.value == 'U') && var13 < var5.count) {
                        var14 = (var5.count - var13) * (var5.value == 'U' ? 2 : 1);
                        var1.skip((long)var14);
                     }
                  } catch (Exception var17) {
                     if (var3 != null) {
                        var3.close();
                     }

                     var4.getArray().push(SleepUtils.getScalar(var11.toString()));
                     return var4;
                  }

                  var4.getArray().push(SleepUtils.getScalar(var11.toString()));
               }
            } else {
               var11 = new StringBuffer();

               try {
                  for(var12 = 0; var12 < var5.count || var5.count == -1; ++var12) {
                     var19 = var1.read(var6, 0, 1);
                     if (var19 < 1) {
                        throw new EOFException();
                     }

                     var13 = (var7.get(0) & 240) >> 4;
                     var14 = var7.get(0) & 15;
                     if (var5.value == 'h') {
                        var11.append(Integer.toHexString(var14));
                        var11.append(Integer.toHexString(var13));
                     } else {
                        var11.append(Integer.toHexString(var13));
                        var11.append(Integer.toHexString(var14));
                     }
                  }
               } catch (Exception var18) {
                  if (var3 != null) {
                     var3.close();
                  }

                  var4.getArray().push(SleepUtils.getScalar(var11.toString()));
                  return var4;
               }

               var4.getArray().push(SleepUtils.getScalar(var11.toString()));
            }
         }
      }

      return var4;
   }

   private static void WriteFormatted(String var0, OutputStream var1, ScriptEnvironment var2, Stack var3, IOObject var4) {
      DataPattern var5 = DataPattern.Parse(var0);
      if (var3.size() == 1 && ((Scalar)var3.peek()).getArray() != null) {
         Stack var18 = new Stack();
         Iterator var19 = ((Scalar)var3.peek()).getArray().scalarIterator();

         while(var19.hasNext()) {
            var18.push(var19.next());
         }

         WriteFormatted(var0, var1, var2, var18, var4);
      } else {
         byte[] var6 = new byte[8];

         for(ByteBuffer var7 = ByteBuffer.wrap(var6); var5 != null; var5 = var5.next) {
            var7.order(var5.order);
            if (var5.value != 'z' && var5.value != 'Z' && var5.value != 'u' && var5.value != 'U') {
               if (var5.value != 'h' && var5.value != 'H') {
                  for(int var21 = 0; var21 != var5.count && !var3.isEmpty(); ++var21) {
                     Scalar var24 = null;
                     if (var5.value != 'x') {
                        var24 = BridgeUtilities.getScalar(var3);
                     }

                     try {
                        switch(var5.value) {
                        case 'B':
                        case 'b':
                           var1.write(var24.intValue());
                           break;
                        case 'C':
                           var1.write(var24.toString().charAt(0));
                        case 'D':
                        case 'E':
                        case 'F':
                        case 'G':
                        case 'H':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'Q':
                        case 'R':
                        case 'T':
                        case 'U':
                        case 'V':
                        case 'W':
                        case 'X':
                        case 'Y':
                        case 'Z':
                        case '[':
                        case '\\':
                        case ']':
                        case '^':
                        case '_':
                        case '`':
                        case 'a':
                        case 'e':
                        case 'g':
                        case 'h':
                        case 'j':
                        case 'k':
                        case 'm':
                        case 'n':
                        case 'p':
                        case 'q':
                        case 'r':
                        case 't':
                        case 'u':
                        case 'v':
                        case 'w':
                        default:
                           break;
                        case 'I':
                           var7.putInt(0, (int)var24.longValue());
                           var1.write(var6, 0, 4);
                           break;
                        case 'S':
                        case 's':
                           var7.putShort(0, (short)var24.intValue());
                           var1.write(var6, 0, 2);
                           break;
                        case 'c':
                           var7.putChar(0, var24.toString().charAt(0));
                           var1.write(var6, 0, 2);
                           break;
                        case 'd':
                           var7.putDouble(0, var24.doubleValue());
                           var1.write(var6, 0, 8);
                           break;
                        case 'f':
                           var7.putFloat(0, (float)var24.doubleValue());
                           var1.write(var6, 0, 4);
                           break;
                        case 'i':
                           var7.putInt(0, var24.intValue());
                           var1.write(var6, 0, 4);
                           break;
                        case 'l':
                           var7.putLong(0, var24.longValue());
                           var1.write(var6, 0, 8);
                           break;
                        case 'o':
                           try {
                              ObjectOutputStream var23 = new ObjectOutputStream(var1);
                              var23.writeObject(var24);
                              break;
                           } catch (Exception var13) {
                              var2.flagError(var13);
                              if (var4 != null) {
                                 var4.close();
                              }

                              return;
                           }
                        case 'x':
                           var1.write(0);
                        }
                     } catch (Exception var14) {
                        if (var4 != null) {
                           var4.close();
                        }

                        return;
                     }
                  }
               } else {
                  try {
                     StringBuffer var20 = new StringBuffer("FF");
                     String var22 = BridgeUtilities.getString(var3, "");
                     if (var22.length() % 2 != 0) {
                        throw new IllegalArgumentException("can not pack '" + var22 + "' as hex string, number of characters must be even");
                     }

                     char[] var10 = var22.toCharArray();

                     for(int var11 = 0; var11 < var10.length; var11 += 2) {
                        if (var5.value == 'H') {
                           var20.setCharAt(0, var10[var11]);
                           var20.setCharAt(1, var10[var11 + 1]);
                        } else {
                           var20.setCharAt(0, var10[var11 + 1]);
                           var20.setCharAt(1, var10[var11]);
                        }

                        var7.putInt(0, Integer.parseInt(var20.toString(), 16));
                        var1.write(var6, 3, 1);
                     }
                  } catch (IllegalArgumentException var15) {
                     if (var4 != null) {
                        var4.close();
                     }

                     throw var15;
                  } catch (Exception var16) {
                     var16.printStackTrace();
                     if (var4 != null) {
                        var4.close();
                     }

                     return;
                  }
               }
            } else {
               try {
                  char[] var8 = BridgeUtilities.getString(var3, "").toCharArray();

                  int var9;
                  for(var9 = 0; var9 < var8.length; ++var9) {
                     if (var5.value != 'u' && var5.value != 'U') {
                        var1.write(var8[var9]);
                     } else {
                        var7.putChar(0, var8[var9]);
                        var1.write(var6, 0, 2);
                     }
                  }

                  for(var9 = var8.length; var9 < var5.count; ++var9) {
                     switch(var5.value) {
                     case 'U':
                        var1.write(0);
                        var1.write(0);
                        break;
                     case 'Z':
                        var1.write(0);
                     }
                  }

                  if (var5.value != 'z' && (var5.value != 'Z' || var5.count != -1)) {
                     if (var5.value == 'u' || var5.value == 'U' && var5.count == -1) {
                        var1.write(0);
                        var1.write(0);
                     }
                  } else {
                     var1.write(0);
                  }
               } catch (Exception var17) {
                  if (var4 != null) {
                     var4.close();
                  }

                  return;
               }
            }
         }

         try {
            var1.flush();
         } catch (Exception var12) {
         }
      }

   }

   private static class openf implements Function {
      private openf() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = ((Scalar)var3.pop()).toString();
         FileObject var5 = new FileObject();
         var5.open(var4, var2.getScriptEnvironment());
         return SleepUtils.getScalar((Object)var5);
      }

      openf(Object var1) {
         this();
      }
   }

   private static class exec implements Function {
      private exec() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = var3.isEmpty() ? SleepUtils.getEmptyScalar() : (Scalar)var3.pop();
         String[] var5;
         if (var4.getArray() != null) {
            var5 = (String[])SleepUtils.getListFromArray(var4.getArray()).toArray(new String[0]);
         } else {
            var5 = var4.toString().split("\\s");
         }

         String[] var6 = null;
         File var7 = null;
         if (!var3.isEmpty()) {
            if (SleepUtils.isEmptyScalar((Scalar)var3.peek())) {
               var3.pop();
            } else {
               ScalarHash var8 = BridgeUtilities.getHash(var3);
               Iterator var9 = var8.keys().scalarIterator();
               var6 = new String[var8.keys().size()];

               for(int var10 = 0; var10 < var6.length; ++var10) {
                  Scalar var11 = (Scalar)var9.next();
                  String var10002 = var11.toString();
                  var6[var10] = var10002 + "=" + var8.getAt(var11);
               }
            }
         }

         if (!var3.isEmpty() && !SleepUtils.isEmptyScalar((Scalar)var3.peek())) {
            if (SleepUtils.isEmptyScalar((Scalar)var3.peek())) {
               var3.pop();
            } else {
               var7 = BridgeUtilities.getFile(var3, var2);
            }
         }

         ProcessObject var12 = new ProcessObject();
         var12.open(var5, var6, var7, var2.getScriptEnvironment());
         return SleepUtils.getScalar((Object)var12);
      }

      exec(Object var1) {
         this();
      }
   }

   private static class sleep implements Function {
      private sleep() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         try {
            Thread.currentThread();
            Thread.sleep(BridgeUtilities.getLong(var3, 0L));
         } catch (Exception var5) {
         }

         return SleepUtils.getEmptyScalar();
      }

      sleep(Object var1) {
         this();
      }
   }

   private static class fork implements Function {
      private fork() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         SleepClosure var4 = BridgeUtilities.getFunction(var3, var2);
         ScriptInstance var5 = var2.fork();
         var5.installBlock(var4.getRunnableCode());
         ScriptVariables var6 = var5.getScriptVariables();

         while(!var3.isEmpty()) {
            KeyValuePair var7 = BridgeUtilities.getKeyValuePair(var3);
            var6.putScalar(var7.getKey().toString(), SleepUtils.getScalar(var7.getValue()));
         }

         IOObject var15 = new IOObject();
         IOObject var8 = new IOObject();

         try {
            PipedInputStream var9 = new PipedInputStream();
            PipedOutputStream var10 = new PipedOutputStream();
            var9.connect(var10);
            PipedInputStream var11 = new PipedInputStream();
            PipedOutputStream var12 = new PipedOutputStream();
            var11.connect(var12);
            var15.openRead(var11);
            var15.openWrite(var10);
            var8.openRead(var9);
            var8.openWrite(var12);
            var5.getScriptVariables().putScalar("$source", SleepUtils.getScalar((Object)var8));
            Thread var13 = new Thread(var5, "fork of " + var5.getRunnableBlock().getSourceLocation());
            var15.setThread(var13);
            var8.setThread(var13);
            var5.setParent(var15);
            var13.start();
         } catch (Exception var14) {
            var2.getScriptEnvironment().flagError(var14);
         }

         return SleepUtils.getScalar((Object)var15);
      }

      fork(Object var1) {
         this();
      }
   }

   private static class SocketFuncs implements Function {
      private SocketFuncs() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Map var4 = BridgeUtilities.extractNamedParameters(var3);
         SocketObject.SocketHandler var5 = new SocketObject.SocketHandler();
         var5.socket = new SocketObject();
         var5.script = var2;
         var5.lport = var4.containsKey("lport") ? ((Scalar)var4.get("lport")).intValue() : 0;
         var5.laddr = var4.containsKey("laddr") ? ((Scalar)var4.get("laddr")).toString() : null;
         var5.linger = var4.containsKey("linger") ? ((Scalar)var4.get("linger")).intValue() : 5;
         var5.backlog = var4.containsKey("backlog") ? ((Scalar)var4.get("backlog")).intValue() : 0;
         if (var1.equals("&listen")) {
            var5.port = BridgeUtilities.getInt(var3, -1);
            var5.timeout = BridgeUtilities.getInt(var3, 60000);
            var5.callback = BridgeUtilities.getScalar(var3);
            var5.type = 1;
         } else {
            var5.host = BridgeUtilities.getString(var3, "127.0.0.1");
            var5.port = BridgeUtilities.getInt(var3, 1);
            var5.timeout = BridgeUtilities.getInt(var3, 60000);
            var5.type = 2;
         }

         if (!var3.isEmpty()) {
            var5.function = BridgeUtilities.getFunction(var3, var2);
         }

         var5.start();
         return SleepUtils.getScalar((Object)var5.socket);
      }

      SocketFuncs(Object var1) {
         this();
      }
   }

   private static class closef implements Function {
      private closef() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         if (!var3.isEmpty() && ((Scalar)var3.peek()).objectValue() instanceof IOObject) {
            IOObject var5 = (IOObject)BridgeUtilities.getObject(var3);
            var5.close();
         } else {
            int var4 = BridgeUtilities.getInt(var3, 80);
            SocketObject.release(var4);
         }

         return SleepUtils.getEmptyScalar();
      }

      closef(Object var1) {
         this();
      }
   }

   private static class readln implements Function {
      private readln() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 1, var2);
         String var5 = var4.readLine();
         return var5 == null ? SleepUtils.getEmptyScalar() : SleepUtils.getScalar(var5);
      }

      readln(Object var1) {
         this();
      }
   }

   private static class readAll implements Function {
      private readAll() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 1, var2);
         Scalar var5 = SleepUtils.getArrayScalar();

         String var6;
         while((var6 = var4.readLine()) != null) {
            var5.getArray().push(SleepUtils.getScalar(var6));
         }

         return var5;
      }

      readAll(Object var1) {
         this();
      }
   }

   private static class println implements Function {
      private println() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         String var5 = BridgeUtilities.getString(var3, "");
         var4.printLine(var5);
         return SleepUtils.getEmptyScalar();
      }

      println(Object var1) {
         this();
      }
   }

   private static class printArray implements Function {
      private printArray() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         Iterator var5 = BridgeUtilities.getIterator(var3, var2);

         while(var5.hasNext()) {
            var4.printLine(var5.next().toString());
         }

         return SleepUtils.getEmptyScalar();
      }

      printArray(Object var1) {
         this();
      }
   }

   private static class print implements Function {
      private print() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         String var5 = BridgeUtilities.getString(var3, "");
         var4.print(var5);
         return SleepUtils.getEmptyScalar();
      }

      print(Object var1) {
         this();
      }
   }

   private static class printEOF implements Function {
      private printEOF() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 1, var2);
         var4.sendEOF();
         return SleepUtils.getEmptyScalar();
      }

      printEOF(Object var1) {
         this();
      }
   }

   private static class getConsoleObject implements Function {
      private getConsoleObject() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getScalar((Object)IOObject.getConsole(var2.getScriptEnvironment()));
      }

      getConsoleObject(Object var1) {
         this();
      }
   }

   private static class bread implements Function {
      private bread() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         String var5 = BridgeUtilities.getString(var3, "");
         return var4.getReader() != null ? BasicIO.ReadFormatted(var5, var4.getReader(), var2.getScriptEnvironment(), var4) : SleepUtils.getEmptyScalar();
      }

      bread(Object var1) {
         this();
      }
   }

   private static class bwrite implements Function {
      private bwrite() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 3, var2);
         String var5 = BridgeUtilities.getString(var3, "");
         BasicIO.WriteFormatted(var5, var4.getWriter(), var2.getScriptEnvironment(), var3, var4);
         return SleepUtils.getEmptyScalar();
      }

      bwrite(Object var1) {
         this();
      }
   }

   private static class mark implements Function {
      private mark() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         if (var4.getInputBuffer() == null) {
            throw new RuntimeException("&mark: input buffer for " + SleepUtils.describe(SleepUtils.getScalar((Object)var4)) + " is closed");
         } else {
            var4.getInputBuffer().mark(BridgeUtilities.getInt(var3, 102400));
            return SleepUtils.getEmptyScalar();
         }
      }

      mark(Object var1) {
         this();
      }
   }

   private static class available implements Function {
      private available() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         try {
            IOObject var4 = BasicIO.chooseSource(var3, 1, var2);
            if (var3.isEmpty()) {
               return SleepUtils.getScalar(var4.getInputBuffer().available());
            } else {
               String var5 = BridgeUtilities.getString(var3, "\n");
               StringBuffer var6 = new StringBuffer();
               int var7 = 0;
               int var8 = var4.getInputBuffer().available();
               var4.getInputBuffer().mark(var8);

               while(var7 < var8) {
                  var6.append((char)var4.getReader().readUnsignedByte());
                  ++var7;
               }

               var4.getInputBuffer().reset();
               return SleepUtils.getScalar(var6.indexOf(var5) > -1);
            }
         } catch (Exception var9) {
            return SleepUtils.getEmptyScalar();
         }
      }

      available(Object var1) {
         this();
      }
   }

   private static class reset implements Function {
      private reset() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         try {
            IOObject var4 = BasicIO.chooseSource(var3, 1, var2);
            var4.getInputBuffer().reset();
         } catch (Exception var5) {
         }

         return SleepUtils.getEmptyScalar();
      }

      reset(Object var1) {
         this();
      }
   }

   private static class unpack implements Function {
      private unpack() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = BridgeUtilities.getString(var3, "");
         String var5 = BridgeUtilities.getString(var3, "");

         try {
            ByteArrayOutputStream var6 = new ByteArrayOutputStream(var5.length());
            DataOutputStream var7 = new DataOutputStream(var6);
            var7.writeBytes(var5);
            return BasicIO.ReadFormatted(var4, new DataInputStream(new ByteArrayInputStream(var6.toByteArray())), var2.getScriptEnvironment(), (IOObject)null);
         } catch (Exception var8) {
            return SleepUtils.getArrayScalar();
         }
      }

      unpack(Object var1) {
         this();
      }
   }

   private static class pack implements Function {
      private pack() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = BridgeUtilities.getString(var3, "");
         ByteArrayOutputStream var5 = new ByteArrayOutputStream(DataPattern.EstimateSize(var4) + 128);
         BasicIO.WriteFormatted(var4, new DataOutputStream(var5), var2.getScriptEnvironment(), var3, (IOObject)null);
         return SleepUtils.getScalar(var5.toByteArray(), var5.size());
      }

      pack(Object var1) {
         this();
      }
   }

   private static class writeb implements Function {
      private writeb() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         String var5 = BridgeUtilities.getString(var3, "");

         try {
            for(int var6 = 0; var6 < var5.length(); ++var6) {
               var4.getWriter().writeByte((byte)var5.charAt(var6));
            }

            var4.getWriter().flush();
         } catch (Exception var7) {
            var4.close();
            var2.getScriptEnvironment().flagError(var7);
         }

         return SleepUtils.getEmptyScalar();
      }

      writeb(Object var1) {
         this();
      }
   }

   private static class readb implements Function {
      private readb() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         int var5 = BridgeUtilities.getInt(var3, 1);
         boolean var6 = false;
         byte[] var7 = null;
         StringBuffer var8 = null;
         if (var4.getReader() != null) {
            int var9 = 0;

            try {
               int var12;
               if (var5 == -1) {
                  var8 = new StringBuffer(BridgeUtilities.getInt(var3, 2048));

                  while(true) {
                     var12 = var4.getReader().read();
                     if (var12 == -1) {
                        break;
                     }

                     char var10 = (char)(var12 & 255);
                     var8.append(var10);
                     ++var9;
                  }
               } else {
                  for(var7 = new byte[var5]; var9 < var5; var9 += var12) {
                     var12 = var4.getReader().read(var7, var9, var5 - var9);
                     if (var12 == -1) {
                        break;
                     }
                  }
               }
            } catch (Exception var12) {
               var4.close();
               if (var5 != -1) {
                  var2.getScriptEnvironment().flagError(var12);
               }
            }

            if (var9 > 0) {
               if (var7 != null) {
                  return SleepUtils.getScalar(var7, var9);
               }

               if (var8 != null) {
                  return SleepUtils.getScalar(var8.toString());
               }
            }
         }

         return SleepUtils.getEmptyScalar();
      }

      readb(Object var1) {
         this();
      }
   }

   private static class consume implements Function {
      private consume() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         int var5 = BridgeUtilities.getInt(var3, 1);
         int var6 = BridgeUtilities.getInt(var3, 32768);
         boolean var7 = false;
         if (var4.getReader() != null) {
            byte[] var8 = new byte[var6];
            int var9 = 0;

            try {
               while(var9 < var5) {
                  int var12;
                  if (var5 - var9 < var6) {
                     var12 = var4.getReader().read(var8, 0, var5 - var9);
                  } else {
                     var12 = var4.getReader().read(var8, 0, var6);
                  }

                  if (var12 == -1) {
                     break;
                  }

                  var9 += var12;
               }
            } catch (Exception var11) {
               var4.close();
               var2.getScriptEnvironment().flagError(var11);
            }

            if (var9 > 0) {
               return SleepUtils.getScalar(var9);
            }
         }

         return SleepUtils.getEmptyScalar();
      }

      consume(Object var1) {
         this();
      }
   }

   private static class read implements Function {
      private read() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = BasicIO.chooseSource(var3, 2, var2);
         SleepClosure var5 = BridgeUtilities.getFunction(var3, var2);
         Thread var6 = new Thread(new BasicIO.CallbackReader(var4, var2, var5, BridgeUtilities.getInt(var3, 0)));
         var4.setThread(var6);
         var6.start();
         return SleepUtils.getEmptyScalar();
      }

      read(Object var1) {
         this();
      }
   }

   private static class iseof implements Predicate {
      private iseof() {
      }

      public boolean decide(String var1, ScriptInstance var2, Stack var3) {
         IOObject var4 = (IOObject)BridgeUtilities.getObject(var3);
         return var4.isEOF();
      }

      iseof(Object var1) {
         this();
      }
   }

   private static class CallbackReader implements Runnable {
      protected IOObject source;
      protected ScriptInstance script;
      protected SleepClosure function;
      protected int bytes;

      public CallbackReader(IOObject var1, ScriptInstance var2, SleepClosure var3, int var4) {
         this.source = var1;
         this.script = var2;
         this.function = var3;
         this.bytes = var4;
      }

      public void run() {
         Stack var1 = new Stack();
         String var2;
         if (this.bytes <= 0) {
            while(this.script.isLoaded() && (var2 = this.source.readLine()) != null) {
               var1.push(SleepUtils.getScalar(var2));
               var1.push(SleepUtils.getScalar((Object)this.source));
               this.function.callClosure("&read", this.script, var1);
            }
         } else {
            StringBuffer var3 = null;

            try {
               while(this.script.isLoaded() && !this.source.isEOF()) {
                  var3 = new StringBuffer(this.bytes);

                  for(int var4 = 0; var4 < this.bytes; ++var4) {
                     var3.append((char)this.source.getReader().readUnsignedByte());
                  }

                  var1.push(SleepUtils.getScalar(var3.toString()));
                  var1.push(SleepUtils.getScalar((Object)this.source));
                  this.function.callClosure("&read", this.script, var1);
               }
            } catch (Exception var5) {
               if (var3.length() > 0) {
                  var1.push(SleepUtils.getScalar(var3.toString()));
                  var1.push(SleepUtils.getScalar((Object)this.source));
                  this.function.callClosure("&read", this.script, var1);
               }

               this.source.close();
               this.script.getScriptEnvironment().flagError(var5);
            }
         }

      }
   }
}

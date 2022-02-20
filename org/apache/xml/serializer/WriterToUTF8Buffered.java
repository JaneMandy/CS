package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public final class WriterToUTF8Buffered extends Writer {
   private static final int BYTES_MAX = 16384;
   private static final int CHARS_MAX = 5461;
   private final OutputStream m_os;
   private final byte[] m_outputBytes;
   private final char[] m_inputChars;
   private int count;

   public WriterToUTF8Buffered(OutputStream out) throws UnsupportedEncodingException {
      this.m_os = out;
      this.m_outputBytes = new byte[16387];
      this.m_inputChars = new char[5462];
      this.count = 0;
   }

   public void write(int c) throws IOException {
      if (this.count >= 16384) {
         this.flushBuffer();
      }

      if (c < 128) {
         this.m_outputBytes[this.count++] = (byte)c;
      } else if (c < 2048) {
         this.m_outputBytes[this.count++] = (byte)(192 + (c >> 6));
         this.m_outputBytes[this.count++] = (byte)(128 + (c & 63));
      } else {
         this.m_outputBytes[this.count++] = (byte)(224 + (c >> 12));
         this.m_outputBytes[this.count++] = (byte)(128 + (c >> 6 & 63));
         this.m_outputBytes[this.count++] = (byte)(128 + (c & 63));
      }

   }

   public void write(char[] chars, int start, int length) throws IOException {
      int lengthx3 = 3 * length;
      int chunks;
      int count_loc;
      int i;
      if (lengthx3 >= 16384 - this.count) {
         this.flushBuffer();
         if (lengthx3 >= 16384) {
            chunks = 1 + length / 5461;

            for(int chunk = 0; chunk < chunks; ++chunk) {
               count_loc = start + length * chunk / chunks;
               i = start + length * (chunk + 1) / chunks;
               int len_chunk = i - count_loc;
               this.write(chars, count_loc, len_chunk);
            }

            return;
         }
      }

      chunks = length + start;
      byte[] buf_loc = this.m_outputBytes;
      count_loc = this.count;

      char c;
      for(i = start; i < chunks && (c = chars[i]) < 128; ++i) {
         buf_loc[count_loc++] = (byte)c;
      }

      for(; i < chunks; ++i) {
         c = chars[i];
         if (c < 128) {
            buf_loc[count_loc++] = (byte)c;
         } else if (c < 2048) {
            buf_loc[count_loc++] = (byte)(192 + (c >> 6));
            buf_loc[count_loc++] = (byte)(128 + (c & 63));
         } else {
            buf_loc[count_loc++] = (byte)(224 + (c >> 12));
            buf_loc[count_loc++] = (byte)(128 + (c >> 6 & 63));
            buf_loc[count_loc++] = (byte)(128 + (c & 63));
         }
      }

      this.count = count_loc;
   }

   private void directWrite(char[] chars, int start, int length) throws IOException {
      int chunks;
      int start_chunk;
      int end_chunk;
      if (length >= 16384 - this.count) {
         this.flushBuffer();
         if (length >= 16384) {
            chunks = 1 + length / 5461;

            for(int chunk = 0; chunk < chunks; ++chunk) {
               start_chunk = start + length * chunk / chunks;
               end_chunk = start + length * (chunk + 1) / chunks;
               int len_chunk = end_chunk - start_chunk;
               this.directWrite(chars, start_chunk, len_chunk);
            }

            return;
         }
      }

      chunks = length + start;
      byte[] buf_loc = this.m_outputBytes;
      start_chunk = this.count;

      for(end_chunk = start; end_chunk < chunks; ++end_chunk) {
         buf_loc[start_chunk++] = buf_loc[end_chunk];
      }

      this.count = start_chunk;
   }

   public void write(String s) throws IOException {
      int length = s.length();
      int lengthx3 = 3 * length;
      int chunks;
      int count_loc;
      int i;
      if (lengthx3 >= 16384 - this.count) {
         this.flushBuffer();
         if (lengthx3 >= 16384) {
            int start = false;
            chunks = 1 + length / 5461;

            for(int chunk = 0; chunk < chunks; ++chunk) {
               count_loc = 0 + length * chunk / chunks;
               i = 0 + length * (chunk + 1) / chunks;
               int len_chunk = i - count_loc;
               s.getChars(count_loc, i, this.m_inputChars, 0);
               this.write(this.m_inputChars, 0, len_chunk);
            }

            return;
         }
      }

      s.getChars(0, length, this.m_inputChars, 0);
      char[] chars = this.m_inputChars;
      chunks = length;
      byte[] buf_loc = this.m_outputBytes;
      count_loc = this.count;

      char c;
      for(i = 0; i < chunks && (c = chars[i]) < 128; ++i) {
         buf_loc[count_loc++] = (byte)c;
      }

      for(; i < chunks; ++i) {
         c = chars[i];
         if (c < 128) {
            buf_loc[count_loc++] = (byte)c;
         } else if (c < 2048) {
            buf_loc[count_loc++] = (byte)(192 + (c >> 6));
            buf_loc[count_loc++] = (byte)(128 + (c & 63));
         } else {
            buf_loc[count_loc++] = (byte)(224 + (c >> 12));
            buf_loc[count_loc++] = (byte)(128 + (c >> 6 & 63));
            buf_loc[count_loc++] = (byte)(128 + (c & 63));
         }
      }

      this.count = count_loc;
   }

   public void flushBuffer() throws IOException {
      if (this.count > 0) {
         this.m_os.write(this.m_outputBytes, 0, this.count);
         this.count = 0;
      }

   }

   public void flush() throws IOException {
      this.flushBuffer();
      this.m_os.flush();
   }

   public void close() throws IOException {
      this.flushBuffer();
      this.m_os.close();
   }

   public OutputStream getOutputStream() {
      return this.m_os;
   }

   public void directWrite(String s) throws IOException {
      int length = s.length();
      int chunk;
      int start_chunk;
      if (length >= 16384 - this.count) {
         this.flushBuffer();
         if (length >= 16384) {
            int start = false;
            int chunks = 1 + length / 5461;

            for(chunk = 0; chunk < chunks; ++chunk) {
               start_chunk = 0 + length * chunk / chunks;
               int end_chunk = 0 + length * (chunk + 1) / chunks;
               int len_chunk = end_chunk - start_chunk;
               s.getChars(start_chunk, end_chunk, this.m_inputChars, 0);
               this.directWrite(this.m_inputChars, 0, len_chunk);
            }

            return;
         }
      }

      s.getChars(0, length, this.m_inputChars, 0);
      char[] chars = this.m_inputChars;
      byte[] buf_loc = this.m_outputBytes;
      chunk = this.count;

      for(start_chunk = 0; start_chunk < length; buf_loc[chunk++] = (byte)chars[start_chunk++]) {
      }

      this.count = chunk;
   }
}

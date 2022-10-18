package agh.genetyczne;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

public class MemorizingOutputStream extends FilterOutputStream {
    StringWriter sw = new StringWriter();
    String last = null;

    public MemorizingOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[] {(byte)b}, 0, 1);
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        String s = new String(b, off, len);
        int pos = s.lastIndexOf('\n');
        if(pos == -1) {
            sw.append(s);
        } else {
            int pos2 = s.lastIndexOf('\n', pos-1);
            if(pos2 == -1) {
                sw.append(s.substring(0, pos));
                last = sw.toString();
            } else {
                last = s.substring(pos2+1, pos);
            }
            sw = new StringWriter();
            sw.append(s.substring(pos+1));
        }
    }

    public String getLast() {
        return last;
    }

}
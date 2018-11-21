package org.orbisgis.codeconsole;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is used to link SLF4J Logger with an OutputStream
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class SLF4JOutputStream extends OutputStream {

    /** Stream buffer */
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    /** Log level */
    private Level level;
    /** SLF4J logger */
    private Logger logger;

    /**
     * Main constructor.
     *
     * @param level Level of the log (Warn, Info, Error ...).
     * @param logger Logger to link to the OutputStream.
     */
    public SLF4JOutputStream(Level level, Logger logger) {
        this.level = level;
        this.logger = logger;
    }

    @Override
    public void write(int i) {
        buffer.write(i);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        //In case of end line, print the line.
        if(len > 0 && '\n' == b[len-1]) {
            buffer.write(b,off,len - 1);
            flush();
        } else {
            buffer.write(b,off,len);
        }
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        String messages = buffer.toString();
        if(!messages.isEmpty()) {
            switch (level) {
                case INFO:
                    logger.info(messages);
                    break;
                case WARN:
                    logger.warn(messages);
                    break;
                case ERROR:
                    logger.error(messages);
                    break;
            }
        }
        buffer.reset();
    }
}
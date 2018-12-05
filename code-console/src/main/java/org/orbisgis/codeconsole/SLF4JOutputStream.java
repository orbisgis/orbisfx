/*
 * Code Console is part of the OrbisGIS platform
 *
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 *
 * Code Console is distributed under GPL 3 license.
 *
 * Copyright (C) 2018 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Code Console is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Code Console is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Code Console. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
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
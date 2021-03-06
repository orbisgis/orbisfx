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

import java.util.HashMap;
import java.util.Map;

/**
 * Definition of a class representing css style element with its id and its properties which can be converted into a
 * String representation :
 *
 * .id {
 *     styleType : value;
 *     styleType : value;
 *     styleType : value;
 * }
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class ColorationStyleWrapper {

    /**
     * Enumeration of all the css properties applicable for the code coloration.
     */
    enum StyleType{
        /** Text color */
        FILL("-fx-fill"),
        /** font weight of the text (bold, bolder, lighter) */
        FONT_WEIGHT("-fx-font-weight");

        /** String representation of the style type (used in the asString() method) */
        String value;

        /** Enumeration constructor */
        StyleType(String value){
            this.value = value;
        }

        @Override
        public String toString(){
            return value;
        }
    }

    /** Css identifier */
    private String id;

    /** Map of the styles and their values. */
    private Map<StyleType, String> styleTypeMap = new HashMap<>();

    /**
     * Sets the id of the css property.
     *
     * @param id Id of the property.
     */
    public void setId(String id) {
        this.id = id;
    }

    /** Add to the css property a StyleType and its value.
     *
     * @param styleType Style type object.
     * @param value Value of the
     */
    public void addStyle(StyleType styleType, String value) {
        styleTypeMap.put(styleType, value);
    }

    /**
     * Convert the IColorationStyleWrapper into its String representation :
     *
     * .id {
     *     styleType : value;
     *     styleType : value;
     *     styleType : value;
     * }
     *
     * @return The String representation
     */
    public String asString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".").append(id).append("{");
        styleTypeMap.forEach((key, value) -> stringBuilder.append(key.toString()).append(":").append(value).append(";"));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

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

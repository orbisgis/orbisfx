package org.orbisgis.mainapplicationapi;

import javafx.stage.Stage;

import java.net.URL;

/**
 * Interface for the definition of the main javaFX application able to display all the UI elements (IDock) which can
 * be add with {@link IMainApplication#addDock(IDock)}
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public interface IMainApplication {

    String PROPERTY_TITLE = "title";
    String PROPERTY_ON_TOP = "onTop";
    String PROPERTY_FULLSCREEN_EXIT_KEY_COMBINATION = "fullscreenexitkey";
    String PROPERTY_STATE = "state";
    String PROPERTY_RESIZABLE = "resizable";
    String PROPERTY_MINIMUM_WIDTH = "minwidth";
    String PROPERTY_MAXIMUM_WIDTH = "maxwidth";
    String PROPERTY_MINIMUM_HEIGHT = "minheight";
    String PROPERTY_MAXIMUM_HEIGHT = "maxheight";

    String STATE_ICONIFIED = "iconified";
    String STATE_NORMAL = "normal";
    String STATE_MAXIMIZED = "maximized";
    String STATE_FULLSCREEN = "fullscreen";

    /**
     * Adds the given {@link IDock} to the application.
     *
     * @param dock {@link IDock} to add to the application.
     */
    void addDock(IDock dock);

    /**
     * Register a CSS style in the application in order to make it available.
     *
     * @param cssUrl Url of the CSS style file to register.
     */
    void registerStyle(URL cssUrl);

    Stage getStage();
}

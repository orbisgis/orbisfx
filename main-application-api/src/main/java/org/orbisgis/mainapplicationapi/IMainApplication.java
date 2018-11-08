/*
 * Main Application API is part of the OrbisGIS platform
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
 * Main Application API is distributed under GPL 3 license.
 *
 * Copyright (C) 2018 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Main Application API is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Main Application API is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Main Application API. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
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

/*
 * Code Console API is part of the OrbisGIS platform
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
 * Code Console API is distributed under GPL 3 license.
 *
 * Copyright (C) 2018 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Code Console API is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Code Console API is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Main Application. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.codeconsole.language;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Add basic action linked to the language. Those action are mainly used to be displayed as buttons in the menu bar
 * of the code console thanks to the method {@link #getMenuActions()}.
 * The other methods returning a EventHandler can just return null and should not necessarily be used.
 * Those methods are defined just to give a list of action which can be included in the menu bar.
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public interface ILanguageAction {

    /**
     * Return the EventHandler to use on the execute action.
     *
     * @return The EventHandler to use on the execute action.
     */
    EventHandler<MouseEvent> getExecuteEventHandler();

    /**
     * Return the EventHandler to use on the open action.
     *
     * @return The EventHandler to use on the open action.
     */
    EventHandler<MouseEvent> getOpenEventHandler();

    /**
     * Return the EventHandler to use on the save action.
     *
     * @return The EventHandler to use on the save action.
     */
    EventHandler<MouseEvent> getSaveEventHandler();

    /**
     * Return a list of Menu object which will be displayed in the menu bar of the code console.
     *
     * @return The list of the actions associated to the language.
     */
    List<Menu> getMenuActions();
}

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
package org.orbisgis.codeconsoleapi.language;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    enum Action{
        EXECUTE, EXECUTE_SELECTION, STOP, CLEAR, OPEN, SAVE, SAVE_AS, FIND, UN_COMMENT;
        public ImageView getImageView(){
            return new ImageView(new Image(this.getClass().getResourceAsStream(this.name().toLowerCase()+".png")));
        }
        public String getActionName(){
            return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
        }
    }

    default Menu createMenu(Action action){
        Menu menu = new Menu();
        ImageView image = action.getImageView();
        Button button = new Button(action.getActionName(), image);
        switch(action) {
            case EXECUTE:
                button.setOnAction(getExecuteEventHandler());
                break;
            case EXECUTE_SELECTION:
                button.setOnAction(getExecuteSelectionEventHandler());
                break;
            case STOP:
                button.setOnAction(getStopEventHandler());
                break;
            case CLEAR:
                button.setOnAction(getClearEventHandler());
                break;
            case OPEN:
                button.setOnAction(getOpenEventHandler());
                break;
            case SAVE :
                button.setOnAction(getSaveEventHandler());
                break;
            case SAVE_AS:
                button.setOnAction(getSaveAsEventHandler());
                break;
            case FIND:
                button.setOnAction(getFindEventHandler());
                break;
            case UN_COMMENT:
                button.setOnAction(getUnCommentEventHandler());
                break;
        }
        menu.setGraphic(button);
        return menu;
    }

    /**
     * Return the EventHandler to use on the execute action.
     *
     * @return The EventHandler to use on the execute action.
     */
    EventHandler<ActionEvent> getExecuteEventHandler();

    /**
     * Return the EventHandler to use on the execute selection action.
     *
     * @return The EventHandler to use on the execute selection action.
     */
    EventHandler<ActionEvent> getExecuteSelectionEventHandler();

    /**
     * Return the EventHandler to use on the stop action.
     *
     * @return The EventHandler to use on the stop action.
     */
    EventHandler<ActionEvent> getStopEventHandler();

    /**
     * Return the EventHandler to use on the clear action.
     *
     * @return The EventHandler to use on the clear action.
     */
    EventHandler<ActionEvent> getClearEventHandler();

    /**
     * Return the EventHandler to use on the open action.
     *
     * @return The EventHandler to use on the open action.
     */
    EventHandler<ActionEvent> getOpenEventHandler();

    /**
     * Return the EventHandler to use on the save action.
     *
     * @return The EventHandler to use on the save action.
     */
    EventHandler<ActionEvent> getSaveEventHandler();

    /**
     * Return the EventHandler to use on the save as action (save in a new file).
     *
     * @return The EventHandler to use on the save as action (save in a new file).
     */
    EventHandler<ActionEvent> getSaveAsEventHandler();

    /**
     * Return the EventHandler to use on the find action.
     *
     * @return The EventHandler to use on the find action.
     */
    EventHandler<ActionEvent> getFindEventHandler();

    /**
     * Return the EventHandler to use on the un/comment action.
     *
     * @return The EventHandler to use on the un/comment action.
     */
    EventHandler<ActionEvent> getUnCommentEventHandler();

    /**
     * Return a list of Menu object which will be displayed in the menu bar of the code console.
     *
     * @return The list of the actions associated to the language.
     */
    List<Menu> getMenuActions();
}

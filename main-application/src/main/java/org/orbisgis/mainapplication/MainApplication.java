/*
 * Main Application is part of the OrbisGIS platform
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
 * Main Application is distributed under GPL 3 license.
 *
 * Copyright (C) 2018 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Main Application is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Main Application is distributed in the hope that it will be useful, but WITHOUT ANY
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
package org.orbisgis.mainapplication;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.orbisgis.mainapplicationapi.IDock;
import org.orbisgis.mainapplicationapi.IMainApplication;
import org.osgi.service.component.annotations.*;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * Entry point of the user interface. On activating the component, it starts a javaFX Application and then listen for
 * the registering of {@link IDock} to display them.
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */

@Component(immediate = true)
public class MainApplication implements IMainApplication {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);
    private static final String ICON_PREFIX = "orbisgis_";
    /** Main {@link javafx.scene.Scene} which is used to add UI elements. */
    private static Scene scene;
    private static Stage stage;

    @Activate
    public void activate(){
        LOGGER.debug("Starting component");
        GisApplicationLauncher.launchApp();
        LOGGER.debug("Component started");
    }

    @Override
    public void addDock(IDock dock) {
        LOGGER.debug("Add IDock at "+dock.getLocation().name());
        switch(dock.getLocation()){
            case TOP:
                addTop(dock.getRootNode());
                break;
            case BOTTOM:
                addBottom(dock.getRootNode());
                break;
            case LEFT:
                addLeft(dock.getRootNode());
                break;
            case RIGHT:
                addRight(dock.getRootNode());
                break;
            case CENTER:
                addCenter(dock.getRootNode());
                break;
        }
    }

    /**
     * Add a node at the top of the layout.
     *
     * @param node Node to add
     */
    private void addTop(Node node) {
        if(scene != null) {
            Platform.runLater(() -> ((BorderPane)scene.getRoot()).setTop(node));
        }
        else{
            LOGGER.error("Unable to add the Node, no Scene found.");
        }
    }

    /**
     * Add a node at the bottom of the layout.
     *
     * @param node Node to add
     */
    public void addBottom(Node node) {
        if(scene != null) {
            Platform.runLater(() -> ((BorderPane)scene.getRoot()).setBottom(node));
        }
        else{
            LOGGER.error("Unable to add the Node, no Scene found.");
        }
    }

    /**
     * Add a node at the left of the layout.
     *
     * @param node Node to add
     */
    public void addLeft(Node node) {
        if(scene != null) {
            Platform.runLater(() -> ((BorderPane)scene.getRoot()).setLeft(node));
        }
        else{
            LOGGER.error("Unable to add the Node, no Scene found.");
        }
    }

    /**
     * Add a node at the right of the layout.
     *
     * @param node Node to add
     */
    public void addRight(Node node) {
        if(scene != null) {
            Platform.runLater(() -> ((BorderPane)scene.getRoot()).setRight(node));
        }
        else{
            LOGGER.error("Unable to add the Node, no Scene found.");
        }
    }

    /**
     * Add a node at the center of the layout.
     *
     * @param node Node to add
     */
    public void addCenter(Node node) {
        if(scene != null) {
            Platform.runLater(() -> ((BorderPane)scene.getRoot()).setCenter(node));
        }
        else{
            LOGGER.error("Unable to add the Node, no Scene found.");
        }
    }

    /**
     * Gives to the application the {@link javafx.scene.Scene}.
     *
     * @param scene Main scene of the application.
     */
    private static void setScene(Scene scene){
        MainApplication.scene = scene;
        scene.getStylesheets().add(MainApplication.class.getResource("menubar.css").toExternalForm());
    }

    /**
     * Gives to the application the {@link javafx.stage.Stage}.
     *
     * @param stage Main stage of the application.
     */
    private static void setStage(Stage stage){
        MainApplication.stage = stage;
    }

    @Override
    public Stage getStage(){
        return stage;
    }

    @Override
    public void registerStyle(URL cssUrl){
        if(scene != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        else{
            LOGGER.error("Unable to add the CSS, no Scene found.");
        }
    }

    /**
     * This class extending {@link javafx.application.Application} launch all the javafx mechanism and gives back to
     * the {@link MainApplication} the created {@link javafx.scene.Scene} to be able to
     * add UI elements.
     */
    public static class GisApplicationLauncher extends Application {

        private static final Logger LOGGER = LoggerFactory.getLogger(GisApplicationLauncher.class);

        static void launchApp(){
            Executors.defaultThreadFactory().newThread(() -> {
                Thread.currentThread().setContextClassLoader(GisApplicationLauncher.class.getClassLoader());
                launch();
            }).start();
        }

        @Override
        public void start(Stage stage) {
            LOGGER.debug("Start application");

            stage.hide();
            configureStage(stage);
            setStage(stage);

            Scene scene = new Scene(new BorderPane());
            setScene(scene);
            stage.setScene(scene);
            stage.show();

            LOGGER.debug("Starting done");
        }

        private void configureStage(Stage stage){
            Properties properties = new Properties();
            try {
                properties.load(MainApplication.class.getResourceAsStream("mainapplication.properties"));
            } catch (IOException e) {
                LOGGER.error("Unable to load the MainApplication properties");
            }

            if(properties.containsKey(PROPERTY_MINIMUM_WIDTH)) {
                LOGGER.debug("PROPERTY_MINIMUM_WIDTH : "+Double.parseDouble(properties.getProperty(PROPERTY_MINIMUM_WIDTH)));
                stage.setMinWidth(Double.parseDouble(properties.getProperty(PROPERTY_MINIMUM_WIDTH)));
            }
            if(properties.containsKey(PROPERTY_MAXIMUM_WIDTH)) {
                LOGGER.debug("PROPERTY_MAXIMUM_WIDTH : "+Double.parseDouble(properties.getProperty(PROPERTY_MAXIMUM_WIDTH)));
                stage.setMaxWidth(Double.parseDouble(properties.getProperty(PROPERTY_MAXIMUM_WIDTH)));
            }
            if(properties.containsKey(PROPERTY_MINIMUM_HEIGHT)) {
                LOGGER.debug("PROPERTY_MINIMUM_HEIGHT : "+Double.parseDouble(properties.getProperty(PROPERTY_MINIMUM_HEIGHT)));
                stage.setMinHeight(Double.parseDouble(properties.getProperty(PROPERTY_MINIMUM_HEIGHT)));
            }
            if(properties.containsKey(PROPERTY_MAXIMUM_HEIGHT)) {
                LOGGER.debug("PROPERTY_MAXIMUM_HEIGHT : "+Double.parseDouble(properties.getProperty(PROPERTY_MAXIMUM_HEIGHT)));
                stage.setMaxHeight(Double.parseDouble(properties.getProperty(PROPERTY_MAXIMUM_HEIGHT)));
            }

            if(properties.containsKey(PROPERTY_RESIZABLE)) {
                LOGGER.debug("PROPERTY_RESIZABLE : "+Boolean.parseBoolean(properties.getProperty(PROPERTY_RESIZABLE)));
                stage.setResizable(Boolean.parseBoolean(properties.getProperty(PROPERTY_RESIZABLE)));
            }

            if(properties.containsKey(PROPERTY_TITLE)) {
                stage.setTitle(properties.getProperty(PROPERTY_TITLE));
            }

            if(properties.containsKey(PROPERTY_ON_TOP)) {
                LOGGER.debug("PROPERTY_ON_TOP : "+Boolean.parseBoolean(properties.getProperty(PROPERTY_ON_TOP)));
                stage.setAlwaysOnTop(Boolean.parseBoolean(properties.getProperty(PROPERTY_ON_TOP)));
            }

            if(properties.containsKey(PROPERTY_STATE)) {
                switch(properties.getProperty(PROPERTY_STATE)) {

                    case STATE_FULLSCREEN :
                        stage.setFullScreen(true);
                        if(properties.containsKey(PROPERTY_FULLSCREEN_EXIT_KEY_COMBINATION)) {
                            String keyStr = properties.getProperty(PROPERTY_FULLSCREEN_EXIT_KEY_COMBINATION);
                            stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination(keyStr));
                        }
                        break;

                    case STATE_MAXIMIZED :
                        stage.setMaximized(true);
                        break;

                    case STATE_NORMAL :
                        break;

                    case STATE_ICONIFIED :
                        stage.setIconified(true);

                    default:
                        break;
                }
            }

            stage.getIcons().setAll(new Image(MainApplication.class.getResource(ICON_PREFIX+"16.png").toExternalForm()),
                    new Image(MainApplication.class.getResource(ICON_PREFIX+"24.png").toExternalForm()),
                    new Image(MainApplication.class.getResource(ICON_PREFIX+"32.png").toExternalForm()),
                    new Image(MainApplication.class.getResource(ICON_PREFIX+"48.png").toExternalForm()),
                    new Image(MainApplication.class.getResource(ICON_PREFIX+"64.png").toExternalForm()),
                    new Image(MainApplication.class.getResource(ICON_PREFIX+"128.png").toExternalForm()),
                    new Image(MainApplication.class.getResource(ICON_PREFIX+"256.png").toExternalForm()));

            stage.setOnCloseRequest(event -> System.exit(0));
        }
    }
}

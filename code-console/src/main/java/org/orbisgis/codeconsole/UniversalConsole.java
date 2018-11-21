package org.orbisgis.codeconsole;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.orbisgis.codeconsoleapi.ICodeConsole;
import org.orbisgis.codeconsoleapi.language.ILanguageAction;
import org.orbisgis.codeconsoleapi.language.ILanguageColoration;
import org.orbisgis.codeconsoleapi.language.ILanguagePack;
import org.orbisgis.mainapplicationapi.IMainApplication;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component(immediate = true)
public class UniversalConsole implements ICodeConsole {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniversalConsole.class);

    private IMainApplication gisApplication;

    /** Dock root node */
    private BorderPane rootNode;
    /** Code area where the groovy script is written */
    private CodeArea codeArea;
    /** ExecutorService used to run separated threads. */
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    /** MenuBar used to display the action of the selected language. */
    private MenuBar actionMenuBar = new MenuBar();
    /** List of the available ILanguagePack */
    private List<ILanguagePack> languagePackList = new ArrayList<>();
    /** ComboBox containing all the available languages. */
    private ComboBox<String> languageComboBox = new ComboBox<>();

    @Override
    public Node getRootNode(){
        return rootNode;
    }

    @Override
    public DockLocation getLocation() {
        return DockLocation.CENTER;
    }

    @Activate
    public void activate() {
        LOGGER.debug("Starting component");

        rootNode = new BorderPane();

        codeArea = new CodeArea();
        codeArea.getStyleClass().clear();
        codeArea.getStyleClass().add("console");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        rootNode.setCenter(codeArea);

        MenuBar rightBar = new MenuBar();
        Menu languageMenu = new Menu();
        languageMenu.setGraphic(languageComboBox);
        rightBar.getMenus().addAll(languageMenu);
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menuBars = new HBox(actionMenuBar, spacer, rightBar);

        BorderPane border = new BorderPane();
        border.setCenter(menuBars);
        border.setRight(languageComboBox);
        rootNode.setTop(border);

        gisApplication.addDock(this);

        languageComboBox.setStyle("-fx-padding: 1 1 1 1;");
        languageComboBox.setOnAction(event -> setUI());

        Platform.runLater(() -> {
            languageComboBox.getSelectionModel().select(0);
            setUI();
        });

        gisApplication.registerStyle(this.getClass().getResource("console.css"));

        LOGGER.debug("Component started");
    }

    /**
     * Sets the UI of the console according to the selected language in the comboBox.
     */
    private void setUI(){
        actionMenuBar.getMenus().clear();
        for(ILanguagePack languagePack : languagePackList){
            if(languagePack.getLanguageName().equals(languageComboBox.getValue())){
                if(languagePack instanceof ILanguageAction){
                    actionMenuBar.getMenus().addAll(((ILanguageAction)languagePack).getMenuActions());
                }
                if(languagePack instanceof ILanguageColoration){
                    ((ILanguageColoration)languagePack).generateColoration(codeArea, executor);
                }
            }
        }
    }

    @Reference
    public void setIGisApplication(IMainApplication gisApplication){
        LOGGER.debug("set IMainApplication :"+gisApplication);
        this.gisApplication = gisApplication;
    }

    public void unsetIGisApplication(IMainApplication gisApplication){
        this.gisApplication = null;
    }

    @Override
    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addLanguagePack(ILanguagePack languagePack) {
        languagePackList.add(languagePack);
        languageComboBox.getItems().add(languagePack.getLanguageName());
    }

    @Override
    public void removeLanguagePack(ILanguagePack languagePack) {
        languagePackList.remove(languagePack);
        languageComboBox.getItems().remove(languagePack.getLanguageName());
    }
}

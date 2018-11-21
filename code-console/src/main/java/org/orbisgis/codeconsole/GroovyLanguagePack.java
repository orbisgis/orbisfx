package org.orbisgis.codeconsole;

import groovy.lang.GroovyShell;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import org.orbisgis.codeconsoleapi.language.ILanguageAction;
import org.orbisgis.codeconsoleapi.language.ILanguageColoration;
import org.orbisgis.codeconsoleapi.language.ILanguagePack;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

@Component(immediate = true, service = {ILanguagePack.class})
public class GroovyLanguagePack implements ILanguagePack, ILanguageAction, ILanguageColoration {

    private static final String GROOVY_LANG = "Groovy";
    private static final String FIND_STYLE_CLASS = "find";
    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyLanguagePack.class);

    /** Info logger use to map the GroovyShell info out to the SLF4J logger. */
    private final SLF4JOutputStream infoLogger = new SLF4JOutputStream(Level.INFO, LOGGER);
    /** Error logger use to map the GroovyShell info out to the SLF4J logger. */
    private final SLF4JOutputStream errorLogger = new SLF4JOutputStream(Level.ERROR,LOGGER);

    private CodeArea codeArea;

    private ToggleButton buttonFind;
    private Button buttonExecute;
    private Button buttonExecuteSelect;

    /** List of the index of the find text portions. */
    private List<Integer> findIndexList = new ArrayList<>();

    /** Last find text. */
    private String lastFindText ="";

    /** File where the script is saved. Set once the save action is done, it is reused for the 'sava' action. */
    private File saveFile = null;

    /** Task executing the Groovy script. */
    private GroovyTask groovyTask;

    /** Map of the property (name, property itself) to give to the GroovyShell for the script execution. */
    private Map<String, Object> propertyMap = new HashMap<>();
    /** Map of the variable (name, property itself) to give to the GroovyShell for the script execution. */
    private Map<String, Object> variableMap = new HashMap<>();

    @Activate
    public void activate(){
        propertyMap.put("out", infoLogger);
        propertyMap.put("err", errorLogger);
    }

    @Override
    public EventHandler<ActionEvent> getExecuteEventHandler() {
        return event -> {
            if(groovyTask == null || groovyTask.isDone()) {
                groovyTask = new GroovyTask(codeArea.getText(), propertyMap, variableMap,
                        new SLF4JOutputStream[]{infoLogger, errorLogger}, new Button[]{buttonExecute, buttonExecuteSelect});
                Platform.runLater(groovyTask);
            }
        };
    }

    @Override
    public EventHandler<ActionEvent> getExecuteSelectionEventHandler() {
        return event -> {
            if(groovyTask == null || groovyTask.isDone()) {
                groovyTask = new GroovyTask(codeArea.getSelectedText(), propertyMap, variableMap,
                        new SLF4JOutputStream[]{infoLogger, errorLogger}, new Button[]{buttonExecute, buttonExecuteSelect});
                Platform.runLater(groovyTask);
            }
        };
    }

    @Override
    public EventHandler<ActionEvent> getStopEventHandler() {
        return event -> groovyTask.cancel();
    }

    @Override
    public EventHandler<ActionEvent> getClearEventHandler() {
        return event -> codeArea.clear();
    }

    @Override
    public EventHandler<ActionEvent> getOpenEventHandler() {
        return event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Groovy script", "groovy"));
            fileChooser.setTitle("Load groovy script");
            saveFile = fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow());
            if(saveFile != null){
                try {
                    List<String> list = Files.readAllLines(saveFile.toPath());
                    codeArea.clear();
                    list.forEach(str -> codeArea.appendText(str));
                } catch (IOException e) {
                    LOGGER.error("Unable to load the file '"+saveFile+"'.\n"+e.getLocalizedMessage());
                }
            }
        };
    }

    @Override
    public EventHandler<ActionEvent> getSaveEventHandler() {
        return event -> {
            if(saveFile == null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Groovy script", "groovy"));
                fileChooser.setTitle("Load groovy script");
                saveFile = fileChooser.showSaveDialog(((Node)event.getTarget()).getScene().getWindow());
            }
            if(saveFile != null){
                try {
                    Files.write(saveFile.toPath(), codeArea.getText().getBytes(), WRITE, CREATE);
                } catch (IOException e) {
                    LOGGER.error("Unable to write the script in the file '"+saveFile+"'.\n"+e.getLocalizedMessage());
                }
            }
            else{
                LOGGER.warn("Please select a file in order to save the script.");
            }
        };
    }

    @Override
    public EventHandler<ActionEvent> getSaveAsEventHandler() {
        return event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Groovy script", "groovy"));
            fileChooser.setTitle("Load groovy script");
            saveFile = fileChooser.showSaveDialog(((Node)event.getTarget()).getScene().getWindow());
            if(saveFile != null){
                try {
                    Files.write(saveFile.toPath(), codeArea.getText().getBytes(), WRITE, CREATE);
                } catch (IOException e) {
                    LOGGER.error("Unable to write the script in the file '"+saveFile+"'.\n"+e.getLocalizedMessage());
                }
            }
            else{
                LOGGER.warn("Please select a file in order to save the script.");
            }
        };
    }

    @Override
    public EventHandler<ActionEvent> getFindEventHandler() {
        return event -> {
            if(buttonFind.isSelected()) {
                lastFindText = codeArea.getSelectedText();
                if(!lastFindText.isEmpty()) {
                    String text = codeArea.getText();
                    int index = -1;
                    while ((index = text.indexOf(lastFindText, index + 1)) != -1) {
                        if(text.length() >= lastFindText.length() + index) {
                            codeArea.setStyleClass(index, index + lastFindText.length(), FIND_STYLE_CLASS);
                            findIndexList.add(index);
                        }
                    }
                }
            }
            else {
                for(Integer index : findIndexList) {
                    codeArea.setStyleClass(index, index + lastFindText.length(), "");
                }
            }
        };
    }

    @Override
    public EventHandler<ActionEvent> getUnCommentEventHandler() {
        return event -> {
            int start = codeArea.getSelection().getStart();
            int end = codeArea.getSelection().getEnd();
            if(start+3 <= codeArea.getText().length() &&
                    codeArea.getText(start, start+3).equals("/**") &&
                    codeArea.getText(end-3, end).equals("**/")){
                codeArea.replaceText(end-3, end, "");
                codeArea.replaceText(start, start+3, "");
            }
            else if(start+2 <= codeArea.getText().length() &&
                    codeArea.getText(start, start+2).equals("/*") &&
                    codeArea.getText(end-2, end).equals("*/")){
                codeArea.replaceText(end-2, end, "");
                codeArea.replaceText(start, start+2, "");
            }
            else {
                codeArea.replaceText(end, end, "*/");
                codeArea.replaceText(start, start, "/*");
            }
        };
    }

    @Override
    public List<Menu> getMenuActions() {
        List<Menu> menuList = new ArrayList<>();

        Menu menuExecute = new Menu();
        ImageView imageExecute = Action.EXECUTE.getImageView();
        buttonExecute = new Button(Action.EXECUTE.getActionName(), imageExecute);
        buttonExecute.setOnAction(getExecuteEventHandler());
        menuExecute.setGraphic(buttonExecute);
        menuList.add(menuExecute);

        Menu menuExecuteSelect = new Menu();
        ImageView imageExecuteSelect = Action.EXECUTE_SELECTION.getImageView();
        buttonExecuteSelect = new Button(Action.EXECUTE_SELECTION.getActionName(), imageExecuteSelect);
        buttonExecuteSelect.setOnAction(getExecuteSelectionEventHandler());
        menuExecuteSelect.setGraphic(buttonExecuteSelect);
        menuList.add(menuExecuteSelect);

        menuList.add(createMenu(Action.STOP));

        menuList.add(createMenu(Action.CLEAR));

        menuList.add(createMenu(Action.OPEN));

        menuList.add(createMenu(Action.SAVE));

        menuList.add(createMenu(Action.SAVE_AS));

        Menu menuFind = new Menu();
        ImageView imageFind = Action.FIND.getImageView();
        buttonFind = new ToggleButton(Action.FIND.getActionName(), imageFind);
        buttonFind.setOnAction(getFindEventHandler());
        menuFind.setGraphic(buttonFind);
        menuList.add(menuFind);

        menuList.add(createMenu(Action.UN_COMMENT));

        return menuList;
    }

    @Override
    public void generateColoration(CodeArea codeArea, ExecutorService executorService) {
        this.codeArea = codeArea;
        URL colorationFileUrl = this.getClass().getResource("syntaxcoloration.properties");
        new ColorationUtils(codeArea, executorService, colorationFileUrl);
    }

    @Override
    public String getLanguageName() {
        return GROOVY_LANG;
    }

    /**
     * Task executing a groovy script in a separated thread.
     */
    public static class GroovyTask extends Task {

        private String script;
        private SLF4JOutputStream[] loggers;
        private Map<String, Object> variables;
        private Map<String, Object> properties;
        private Button[] executeAction;
        private Thread thread;

        GroovyTask(String script, Map<String, Object> properties, Map<String, Object> variables,
                   SLF4JOutputStream[] loggers, Button[] executeAction) {
            this.script = script;
            this.loggers = loggers;
            this.variables = variables;
            this.properties = properties;
            this.executeAction = executeAction;
        }

        @Override
        protected Object call() {
            Arrays.stream(executeAction).forEach(button -> button.setDisable(true));
            try {
                final GroovyShell groovyShell = new GroovyShell();
                variables.forEach(groovyShell::setVariable);
                properties.forEach(groovyShell::setProperty);
                Runnable scriptRun = () -> {
                    try {
                        Object o = groovyShell.evaluate(script);
                        LOGGER.info("Script has returned : "+o.toString());
                    } catch (Exception e) {
                        LOGGER.error("Cannot execute this R script.\nCause : " + e.getMessage());
                    } finally {
                        Arrays.stream(executeAction).forEach(button -> button.setDisable(false));
                    }
                };
                thread = new Thread(scriptRun);
                thread.start();
                thread.join();
            } catch (Exception e) {
                LOGGER.error("Cannot execute the Groovy script"+"\n" + e.getLocalizedMessage());
            } finally {
                Arrays.stream(executeAction).forEach(button -> button.setDisable(false));
                for(SLF4JOutputStream logger : loggers) {
                    try {
                        logger.flush();
                    } catch (IOException e) {
                        LOGGER.error("Cannot display the output of the console\n"+e.getMessage());
                    }
                }
            }
            return null;
        }
    }
}

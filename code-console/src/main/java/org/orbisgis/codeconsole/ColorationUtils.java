package org.orbisgis.codeconsole;

import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class able to generate the coloration from a '.property' file and to register it in a CodeArea.
 *
 * @author Sylvain PALOMINOS (UBS 2018)
 * @author Erwan Bocher (CNRS)
 */
public class ColorationUtils {

    private static final String OPTION_PATTERN = "pattern";
    private static final String OPTION_COLOR = "color";
    private static final String OPTION_ATTRIBUTE = "attribute";

    private static final Logger LOGGER = LoggerFactory.getLogger(ColorationUtils.class);

    /** CodeArea where the coloration should be integrated. */
    private CodeArea codeArea;
    /** ExecutorService used for the asynchronous coloration. */
    private ExecutorService executorService;
    /** Pattern for the detection of the coloration. */
    private Pattern pattern = Pattern.compile("");
    /** List of the coloration categories */
    private List<String> categoryList = new ArrayList<>();

    /**
     * Main constructor.
     *
     * @param codeArea CodeArea where the coloration should be integrated.
     * @param executorService ExecutorService used for the asynchronous coloration.
     * @param colorationFileUrl Url of the the '.properties' file containing the coloration configuration.
     */
    public ColorationUtils(CodeArea codeArea, ExecutorService executorService, URL colorationFileUrl){
        this.codeArea = codeArea;
        this.executorService = executorService;

        Properties props = new Properties();
        try {
            props.load(colorationFileUrl.openStream());
        } catch (IOException e) {
            LOGGER.error("Unable to load the coloration file :'"+colorationFileUrl+"'.\n"+e.getLocalizedMessage());
            return;
        }
        if(props.getProperty("categories") == null){
            LOGGER.warn("No 'categories' property found.");
            return;
        }

        //Read the coloration file and generate the List of ColorationStyleWrapper and the Map of the pattern
        Map<String, String> patternMap = new HashMap<>();
        List<ColorationStyleWrapper> styleWrapperList = new ArrayList<>();
        for(String str : props.getProperty("categories").split(",")){
            categoryList.add(str);
            patternMap.put(str, props.getProperty(str+"."+OPTION_PATTERN));
            ColorationStyleWrapper wrapper = new ColorationStyleWrapper();
            wrapper.setId(str);
            String color = props.getProperty(str+"."+OPTION_COLOR);
            if(color != null){
                wrapper.addStyle(ColorationStyleWrapper.StyleType.FILL, color);
            }
            String fontWeight = props.getProperty(str+"."+OPTION_ATTRIBUTE);
            if(fontWeight != null){
                wrapper.addStyle(ColorationStyleWrapper.StyleType.FONT_WEIGHT, fontWeight);
            }
            styleWrapperList.add(wrapper);
        }

        registerCss(styleWrapperList);
        compilePatternMap(patternMap);

        //Apply the coloration to the CodeArea
        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeColorationAsync)
                .awaitLatest(codeArea.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyColoration);
    }

    /**
     * Register the CSS coming from the ColorationStyleWrapper List in the FX Scene.
     *
     * @param styleWrapperList List of ColorationStyleWrapper containing the style to register.
     */
    private void registerCss(List<ColorationStyleWrapper> styleWrapperList){
        StringBuilder styleStr = new StringBuilder();
        for(ColorationStyleWrapper style : styleWrapperList){
            styleStr.append(style.asString());
        }
        File tempStyleClass;
        try {
            tempStyleClass = File.createTempFile("groovyColoration", ".css");
            tempStyleClass.deleteOnExit();
            try (PrintWriter printWriter = new PrintWriter(tempStyleClass)) {
                printWriter.println(styleStr);
            } catch (FileNotFoundException e) {
                LOGGER.error("Unable to write the CSS file.\n"+e.getLocalizedMessage());
                tempStyleClass = null;
            }
        } catch (IOException e) {
            LOGGER.error("Unable to generate the CSS file.\n"+e.getLocalizedMessage());
            tempStyleClass = null;
        }
        if(tempStyleClass != null){
            codeArea.getScene().getStylesheets().add(tempStyleClass.toURI().toString());
        }
    }

    /**
     * Compile all the pattern.
     *
     * @param patternMap Map of the different pattern and identifier to compile.
     */
    private void compilePatternMap(Map<String, String> patternMap){
        StringBuilder pat= new StringBuilder();
        for(Map.Entry<String, String> entry : patternMap.entrySet()){
            if(pat.length() > 0){
                pat.append("|");
            }
            pat.append("(?<").append(entry.getKey()).append(">").append(entry.getValue()).append(")");
        }
        pattern = Pattern.compile(pat.toString());
    }

    /**
     * Compute the coloration asynchronously.
     *
     * @return A Task computing the coloration.
     */
    private Task<StyleSpans<Collection<String>>> computeColorationAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
                return computeColoration(text);
            }
        };
        executorService.execute(task);
        return task;
    }

    /**
     * Apply the coloration to the CodeArea.
     *
     * @param coloration Coloration to apply.
     */
    private void applyColoration(StyleSpans<Collection<String>> coloration) {
        codeArea.setStyleSpans(0, coloration);
    }

    /**
     * Compute the coloration of the given text with the compiled Pattern.
     *
     * @param text Text to analyse.
     *
     * @return The text coloration.
     */
    private StyleSpans<Collection<String>> computeColoration(String text) {
        Matcher matcher = pattern.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass = null;
            for(String category : categoryList){
                styleClass = matcher.group(category) != null ? category : null;
                if(styleClass != null){
                    break;
                }
            }
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}

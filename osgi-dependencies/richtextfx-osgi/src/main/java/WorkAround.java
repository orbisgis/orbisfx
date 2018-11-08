import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * A workaround to avoid a class not found Exception on using RichTextFX
 */
@Component
public class WorkAround {

    @Activate
    public void activate(){
        com.sun.javafx.css.converters.SizeConverter.SequenceConverter.getBooleanConverter();
    }
}
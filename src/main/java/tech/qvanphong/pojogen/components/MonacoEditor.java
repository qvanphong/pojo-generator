package tech.qvanphong.pojogen.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.*;

public class MonacoEditor extends Component {
    public MonacoEditor() {
        clear();
    }

    public void clear() {
        getElement().setProperty("value", "");
    }

    @Synchronize("value-changed")
    public String getValue() {
        return getElement().getProperty("value");
    }
}

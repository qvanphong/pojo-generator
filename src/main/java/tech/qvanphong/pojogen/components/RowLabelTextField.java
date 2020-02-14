package tech.qvanphong.pojogen.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;

public class RowLabelTextField extends Composite<TextField> {
    private Label label;

    public RowLabelTextField() {
        this("");
    }

    public RowLabelTextField(String label) {
        this(label, "");
    }

    public RowLabelTextField(String label, String value) {
        buildComponent(label, value);
    }

    private void buildComponent(String label, String value){
        this.label = new Label(label);
        this.label.getStyle().set("font-weight", "bold");

        getContent().setValue(value);
        getContent().setPrefixComponent(this.label);
    }

    public void setLabel(String value){
        label.setText(value);
    }

    public String getLabel() {
        return label.getText();
    }

    public void setWidth(String width) {
        getContent().setWidth(width);
    }

    public String getValue(){
        return getContent().getValue();
    }
}

package tech.qvanphong.pojogen.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import tech.qvanphong.pojogen.helper.RandomStringGenerator;

import java.util.*;

public class PropertyField extends Composite<HorizontalLayout> {
    private ComboBox<String> accessModifier;
    private ComboBox<String> type;
    private TextField variableName;
    private Button addRow;
    private Button removeRow;

    private Collection<String> accessModifierItems = new ArrayList<>(Arrays.asList("public", "protected", "default", "private"));
    private Collection<String> typeItems = new ArrayList<>(Arrays.asList("byte", "short", "int", "long", "float", "double", "boolean", "String", "char"));

    public PropertyField() {
        buildComponent();
        initializeComponentProperties();

        setDefaultValue();
    }

    private void setDefaultValue() {
        String[] dataTypes = new String[]{"int", "String"};

        setAccessModifierValue("private");
        setDataTypeValue(dataTypes[new Random().nextInt(dataTypes.length)]);
        setVariableNameValue("");
    }

    private void buildComponent() {
        accessModifier = new ComboBox<>();
        accessModifier.setLabel("Access Modifier");
        accessModifier.setWidthFull();

        type = new ComboBox<>();
        type.setLabel("Data Type (Allow custom value)");
        type.setWidthFull();

        variableName = new TextField();
        variableName.setLabel("Variable Name");
        variableName.setWidthFull();

        addRow = new Button(VaadinIcon.PLUS_CIRCLE.create());
        removeRow = new Button(VaadinIcon.MINUS_CIRCLE.create());

        getContent().add(accessModifier, type, variableName, addRow, removeRow, new Hr());
        getContent().setAlignItems(FlexComponent.Alignment.END);
        getContent().getElement().setAttribute("define_id", RandomStringGenerator.randomAlphaNumeric());
    }

    private void initializeComponentProperties() {
        accessModifier.setItems(accessModifierItems);
        accessModifier.setRequired(true);
        type.setItems(typeItems);
        type.setRequired(true);
        type.setAllowCustomValue(true);
        variableName.setRequired(true);

        type.addCustomValueSetListener(event -> type.setValue(event.getDetail()));
        addRow.addClickListener(click -> this.getParent().ifPresent(parent -> {
            parent.getChildren().filter((this)::equals).findFirst().ifPresent(
                    sameDefineIdComponent -> {
                        int index = parent.getElement().indexOfChild(sameDefineIdComponent.getElement());
                        if (index != -1) {
                            int insertIndex = ++index;
                            parent.getElement().insertChild(insertIndex, new PropertyField().getElement());
                        }
                    }
            );
        }));
        removeRow.addClickListener(click -> this.getParent().ifPresent(parent -> {
            //Require that at least there is 1 field in Container
            if (parent.getChildren().count() > 1) {
                parent.getElement().removeChild(this.getElement());
            }
        }));
    }

    /*Setter & Getter*/
    public String getAccessModifierValue() {
        return accessModifier.getValue();
    }

    public void setAccessModifierValue(String value) {
        accessModifier.setValue(value);
    }

    public String getDataTypeValue() {
        return type.getValue();
    }

    public void setDataTypeValue(String value) {
        type.setValue(value);
    }

    public String getVariableNameValue() {
        return variableName.getValue();
    }

    public void setVariableNameValue(String value) {
        variableName.setValue(value);
    }

    public Map<String, String> getValues() {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("access", getAccessModifierValue());
        values.put("type", getDataTypeValue());
        values.put("name", getVariableNameValue());

        return values;
    }

    public void setValues(Map<String, String> values) {
        String access = values.get("access");
        if (access != null && !access.isEmpty()) {
            setAccessModifierValue(access);
        }

        String type = values.get("type");
        if (type != null && !type.isEmpty()) {
            setDataTypeValue(type);
        }

        String name = values.get("name");
        if (name != null && !name.isEmpty()) {
            setVariableNameValue(name);
        }
    }

    @Override
    public String toString() {
        return "PropertyField{" +
                "accessModifier=" + accessModifier +
                ", type=" + type +
                ", variableName=" + variableName +
                ", addRow=" + addRow +
                ", removeRow=" + removeRow +
                ", accessModifierItems=" + accessModifierItems +
                ", typeItems=" + typeItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        PropertyField that = (PropertyField) o;
        return Objects.equals(getContent().getElement().getAttribute("define_id"), that.getContent().getElement().getAttribute("define_id"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessModifier, type, variableName, addRow, removeRow, accessModifierItems, typeItems);
    }
}

package tech.qvanphong.pojogen.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.x5.template.Chunk;
import com.x5.template.Theme;
import tech.qvanphong.pojogen.components.PropertyField;
import tech.qvanphong.pojogen.components.RowLabelTextField;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class GeneratorLayout extends VerticalLayout implements AfterNavigationObserver {
    private Accordion classInformationAccordion = new Accordion();
    private Accordion fieldsAccordion = new Accordion();
    private Accordion additionalAccordion = new Accordion();
    private Button generator = new Button("Generator");
    private RowLabelTextField packageRow = new RowLabelTextField("package:");
    private RowLabelTextField classNameRow = new RowLabelTextField("Class ");
    private RowLabelTextField extendsRow = new RowLabelTextField("extends");
    private RowLabelTextField implementRow = new RowLabelTextField("implement");
    private Checkbox useEmptyConstructor = new Checkbox("Empty Constructor?");
    private Checkbox useParamsConstructor = new Checkbox("How about Constructor with params?");
    private Checkbox useMain = new Checkbox("Click here if you love main() ❤️");
    private Checkbox useGetterSetter = new Checkbox("Getter & Setter");
    private Checkbox useToString = new Checkbox("\uD83E\uDD16 #~/#@$@#$!@#^& -> \uD83D\uDC68 'Hello'? (toString()) (Work in process \uD83E\uDD26\u200D♂️)");

    private VerticalLayout fieldContainer;

    public GeneratorLayout() {
        buildLayout();
        initListener();
    }

    private void buildLayout() {
        classInformationAccordion.add("Class information", createClassInformationContainer()).addThemeVariants(DetailsVariant.FILLED);
        classInformationAccordion.setWidthFull();

        fieldsAccordion.add("Fields", createFieldListContainer()).addThemeVariants(DetailsVariant.FILLED);
        fieldsAccordion.setWidthFull();

        additionalAccordion.add("Additional \uD83C\uDF54", createAdditionalContainer());
        additionalAccordion.setWidthFull();

        generator.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(classInformationAccordion, fieldsAccordion, additionalAccordion, generator);
    }

    private VerticalLayout createClassInformationContainer() {
        HorizontalLayout classDeclareContainer = new HorizontalLayout(classNameRow, extendsRow, implementRow);
        classDeclareContainer.setWidth("50%");

        classNameRow.setWidth("100%");
        extendsRow.setWidth("100%");
        implementRow.setWidth("100%");
        packageRow.setWidth("50%");

        Div additional = new Div(useEmptyConstructor, useParamsConstructor, useMain);
        additional.setTitle("Additional \uD83C\uDF54");

        VerticalLayout classInformationContainer = new VerticalLayout();
        classInformationContainer.add(packageRow, classDeclareContainer, additional);
        classDeclareContainer.setSizeFull();

        return classInformationContainer;
    }

    private VerticalLayout createFieldListContainer() {
        fieldContainer = new VerticalLayout();

        PropertyField defaultIdField = new PropertyField();
        defaultIdField.setVariableNameValue("id");
        defaultIdField.setDataTypeValue("int");

        PropertyField defaultNameField = new PropertyField();
        defaultNameField.setVariableNameValue("name");
        defaultNameField.setDataTypeValue("String");

        fieldContainer.setId("field-container");
        fieldContainer.setMaxHeight("500px");
        fieldContainer.add(defaultIdField);
        fieldContainer.add(defaultNameField);

        return fieldContainer;
    }

    private VerticalLayout createAdditionalContainer() {
        VerticalLayout additionalContainer = new VerticalLayout();
        additionalContainer.add(useGetterSetter, useToString);

        return additionalContainer;
    }

    private void initListener() {
        generator.addClickListener(click -> {
            if (validate()) {
                HashMap<String, Object> choices = new LinkedHashMap<>();
                choices.put("package", packageRow.getValue());
                choices.put("class", classNameRow.getValue());
                choices.put("extends", extendsRow.getValue());
                choices.put("implements", implementRow.getValue());
                choices.put("empty_constructor", useEmptyConstructor.getValue() + "");
                choices.put("full_constructor", useParamsConstructor.getValue() + "");
                choices.put("main", useMain.getValue() + "");
                choices.put("getter_setter", useGetterSetter.getValue() + "");
                choices.put("to_string", useToString.getValue() + "");
                choices.put("fields", getFieldsValue());

                System.out.println(choices);

                Theme theme = new Theme();
                Chunk chunk = theme.makeChunk("my_template", "txt");

                chunk.set("choices", choices);
                
                StringWriter swOut = new StringWriter();
                String result = "";
                try {
                    chunk.render(swOut);
                    result = chunk.toString();
                    
                    swOut.flush();
                    swOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                sendToSecondaryComponent(result);
            }
        });
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    }

    private boolean validate() {
        if (classNameRow.getValue() == null || classNameRow.getValue().isEmpty()) {
            classNameRow.getContent().setInvalid(true);
            classNameRow.getContent().setErrorMessage("Please don't let me empty \uD83D\uDE2D");
            return false;
        }
        return fieldContainer.getChildren().noneMatch(
                field -> ((PropertyField) field).getValues().containsValue(""));
    }

    private List<Map<String, String>> getFieldsValue() {
        ArrayList<Map<String, String>> values = new ArrayList<>();
        fieldContainer.getChildren().forEach(component -> values.add(((PropertyField) component).getValues()));

        return values;
    }

    private void sendToSecondaryComponent(String result){
        getParent().ifPresent(splitLayout -> {
            FlexComponent<? extends Component> secondaryComponent = (FlexComponent<? extends Component>) ((SplitLayout) splitLayout).getSecondaryComponent();

            Html htmlResult = new Html("<pre id='result'> "+ result + " </pre>");

            Input hiddenInput = new Input();
            hiddenInput.setId("hidden-result");
            hiddenInput.setValue(result);

            secondaryComponent.removeAll();
            secondaryComponent.add(htmlResult);
            secondaryComponent.add(hiddenInput);
        });
    }
}

package tech.qvanphong.pojogen;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import tech.qvanphong.pojogen.components.MonacoEditor;
import tech.qvanphong.pojogen.components.PropertyField;
import tech.qvanphong.pojogen.layout.GeneratorLayout;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "POJO Generator", shortName = "POJO Generator")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@PageTitle("Java Pojo Generator")
public class MainView extends SplitLayout {

    public MainView() {
        VerticalLayout generatedClassContainer = new VerticalLayout();
        generatedClassContainer.setId("generated-class-container");
        generatedClassContainer.setSizeFull();
        generatedClassContainer.getElement().setAttribute("title", "Click here to copy to clipboard");
        generatedClassContainer.addClickListener(click -> {
            if(generatedClassContainer.getComponentCount() != 0){
                getUI().ifPresent(ui -> ui.getPage().executeJs("var doc = document.getElementById(\"hidden-result\");" +
                        "doc.select();" +
                        "document.execCommand('copy');"));
                new Notification("\uD83D\uDCE2 Copied to clipboard! Have fun! \uD83E\uDD1F",
                        4000,
                        Notification.Position.BOTTOM_END).open();
            }
        });

        setSizeFull();
        setOrientation(Orientation.HORIZONTAL);
        setPrimaryStyle("minWidth", "50%");
        setSplitterPosition(50);
        addToPrimary(new GeneratorLayout());
        addToSecondary(generatedClassContainer);
    }
}

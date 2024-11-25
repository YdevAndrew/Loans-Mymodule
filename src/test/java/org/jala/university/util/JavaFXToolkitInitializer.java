package org.jala.university.util;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

public class JavaFXToolkitInitializer {

    @BeforeAll
    public static void initToolkit() {
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }
}
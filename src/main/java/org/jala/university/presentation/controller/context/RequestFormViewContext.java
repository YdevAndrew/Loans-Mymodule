package org.jala.university.presentation.controller.context;

import lombok.Builder;
import lombok.Value;
import org.jala.university.commons.presentation.ViewContext;

import java.util.HashMap;

@Value
@Builder
public class RequestFormViewContext implements ViewContext {
    HashMap<String, Object> formData;
}

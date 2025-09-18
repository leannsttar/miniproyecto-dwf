package sv.edu.udb.configuration.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiErrorWrapper {
    private final List<ApiError> errors = new ArrayList<>();

    public void addApiError(final ApiError error){ errors.add(error); }

    public void addFieldError(final String type, final String title, final String source, final String description){
        final ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(type)
                .title(title)
                .description(description)
                .source(source)
                .build();
        errors.add(error);
    }
}

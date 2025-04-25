package ru.hogwarts.school.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Ничего не найдено")
public class BadRequest extends RuntimeException {
    public BadRequest(String message) {super( message);}
}

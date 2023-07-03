package br.com.valdircezar.webfluxcourse.model.request;

public record UserRequest(
        String email,
        String name,
        String password
) {
}

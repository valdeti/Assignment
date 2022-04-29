package com.assignment.users;

import javax.validation.constraints.NotBlank;

public record UserRequest(String firstName, String lastName, @NotBlank String email, @NotBlank String password) {

}

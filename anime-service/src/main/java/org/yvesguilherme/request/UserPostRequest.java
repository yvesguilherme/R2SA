package org.yvesguilherme.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPostRequest {
  @NotBlank(message = "The field 'firstName' is required")
  private String firstName;

  @NotBlank(message = "The field 'lastName' is required")
  private String lastName;

  @NotBlank(message = "The field 'email' is required")
  @Email(
          message = "The field 'email' must be a valid e-mail",
          regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
  )
  private String email;
}

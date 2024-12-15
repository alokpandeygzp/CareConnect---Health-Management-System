package com.healthcare.user_service.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class UserDto {

    private int id;

    @NotEmpty
    @Size(min = 4, message = "Username must be minimum of 4 characters !")
    private String name;

    @Email(message = "Email address is not valid !")
    private String email;

    @NotEmpty
    @Size(min = 3, max = 10, message = "Password must be min 3 and max 10 characters !")
    private String password;

    @NotEmpty
    private String about;

    private RoleDto role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty @Size(min = 4, message = "Username must be minimum of 4 characters !") String getName() {
        return name;
    }

    public void setName(@NotEmpty @Size(min = 4, message = "Username must be minimum of 4 characters !") String name) {
        this.name = name;
    }

    public @Email(message = "Email address is not valid !") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email address is not valid !") String email) {
        this.email = email;
    }

    public @NotEmpty @Size(min = 3, max = 10, message = "Password must be min 3 and max 10 characters !") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty @Size(min = 3, max = 10, message = "Password must be min 3 and max 10 characters !") String password) {
        this.password = password;
    }

    public @NotEmpty String getAbout() {
        return about;
    }

    public void setAbout(@NotEmpty String about) {
        this.about = about;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }
}

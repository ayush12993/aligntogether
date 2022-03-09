package com.aligntogether.socialmedia.model.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Posts {
    @NotBlank
    @Size(min = 1, max = 20)
    private long id;

    @NotBlank
    private String post;

}
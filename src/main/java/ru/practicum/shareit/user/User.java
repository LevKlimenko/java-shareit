package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import ru.practicum.shareit.user.dto.Create;
import ru.practicum.shareit.user.dto.Update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table (name = "users", schema = "public")
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    @NotBlank(groups = {Create.class})
    private String name;
    @Column(name = "email")
    @NotBlank(groups = {Create.class})
    @Email(message = "Please enter correct E-mail", groups = {Create.class, Update.class})
    private String email;
}
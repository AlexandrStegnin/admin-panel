package com.ddkolesnik.adminpanel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Alexandr Stegnin
 */


@Data
@Entity
@ToString(exclude = "user")
@Table(name = "user_profile")
@EqualsAndHashCode(callSuper = true, exclude = "user")
public class UserProfile extends AbstractEntity {

    @Id
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @Column
    @NotNull(message = "Необходимо указать имя")
    @Size(min = 1, max = 100, message = "Имя должно быть от 1 до 100 символов")
    private String firstName;

    @Column
    @NotNull(message = "Необходимо указать фамилию")
    @Size(min = 1, max = 100, message = "Фамилия должна быть от 1 до 100 символов")
    private String lastName;

    @Column
    private String patronymic;

    @Email
    @Column
    @NotBlank(message = "Необходимо указать email")
    @NotNull(message = "Необходимо указать email")
    @Size(min = 6, message = "Необходимо указать корректный email")
    private String email;

    @Column
    private String avatar;

}

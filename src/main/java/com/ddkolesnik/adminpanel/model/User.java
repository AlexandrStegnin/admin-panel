package com.ddkolesnik.adminpanel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "app_user")
@EqualsAndHashCode(of = {"id", "login"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column
    @NotNull(message = "Необходимо указать имя пользователя")
    @Size(min = 3, max = 45, message = "Имя пользователя должно быть от {min} до {max} символов")
    private String login;

    @JsonIgnore
    @Column(name = "password", length = 60)
    private String password;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
        this.profile = new UserProfile();
        this.profile.setUser(this);
    }

    public User(UserDetails userDetails) {
        this.login = userDetails.getUsername();
    }
}

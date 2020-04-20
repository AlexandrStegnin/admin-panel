package com.ddkolesnik.adminpanel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "app_user")
@EqualsAndHashCode(of = {"id", "login"})
public class User implements UserDetails {

    @Id
    @TableGenerator(name = "userSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "USER.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "userSeqStore")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column
    @NotNull(message = "Необходимо указать имя пользователя")
    @Size(min = 3, max = 45, message = "Имя пользователя должно быть от 3 до 45 символов")
    private String login;

    @JsonIgnore
    @Column(name = "password_hash", length = 60)
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "user_role_to_user"),
            inverseForeignKey = @ForeignKey(name = "user_role_to_role")
    )
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;

    @Column
    private boolean accountNonExpired = true;

    @Column
    private boolean accountNonLocked = true;

    @Column
    private boolean credentialsNonExpired = true;

    @Transient
    private boolean enabled;

    //необходимо при создании нового пользователя, что бы задать пароль
    private transient String password;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonExpired() &&
                isAccountNonLocked() &&
                isCredentialsNonExpired();
    }

    public User() {
        this.profile = new UserProfile();
        this.profile.setUser(this);
    }

    public User(String login, String passwordHash, boolean enabled, boolean accountNonExpired,
                boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> roles) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.roles = roles.stream().map(Role::new).collect(Collectors.toSet());
    }

    public User(UserDetails userDetails) {
        this.login = userDetails.getUsername();
        this.enabled = userDetails.isEnabled();
        this.accountNonExpired = userDetails.isAccountNonExpired();
        this.credentialsNonExpired = userDetails.isCredentialsNonExpired();
        this.accountNonLocked = userDetails.isAccountNonLocked();
        this.roles = userDetails.getAuthorities().stream().map(Role::new).collect(Collectors.toSet());
    }
}

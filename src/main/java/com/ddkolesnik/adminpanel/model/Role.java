package com.ddkolesnik.adminpanel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static com.ddkolesnik.adminpanel.configuration.support.Constant.ROLE_PREFIX;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "app_role")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
public class Role extends AbstractEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    @Size(min = 3, max = 20, message = "Название роли должно быть более 2 и менее 21 символа")
    private String name;

    @Column(name = "humanized")
    private String humanized;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return name.startsWith(ROLE_PREFIX) ? name : ROLE_PREFIX + name;
    }

    @PrePersist
    public void setRole() {
        if (!name.trim().toUpperCase().startsWith(ROLE_PREFIX)) name = ROLE_PREFIX + name.trim().toUpperCase();
        else name = name.trim().toUpperCase();
    }

    public Role(GrantedAuthority authority) {
        this.name = authority.getAuthority();
    }
}

package com.ddkolesnik.adminpanel.model;

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
@Table(name = "role")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
public class Role extends AbstractEntity implements GrantedAuthority {

    @Id
    @TableGenerator(name = "roleSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "ROLE.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "roleSeqStore")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "role")
    @Size(min = 3, max = 20, message = "Название роли должно быть более 2 и менее 21 символа")
    private String title;

    @Override
    public String getAuthority() {
        return title.startsWith(ROLE_PREFIX) ? title : ROLE_PREFIX + title;
    }

    @PrePersist
    public void serRole() {
        if (!title.trim().toUpperCase().startsWith(ROLE_PREFIX)) title = ROLE_PREFIX + title.trim().toUpperCase();
        else title = title.trim().toUpperCase();
    }

    public Role(GrantedAuthority authority) {
        this.title = authority.getAuthority();
    }
}

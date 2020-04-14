package com.ddkolesnik.adminpanel.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "app_token")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class AppToken extends AbstractEntity {

    @Id
    @TableGenerator(name = "appTokenSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "APP.TOKEN.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "appTokenSeqStore")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "Название приложения не может быть пустым")
    @Column(name = "app_name")
    String appName;

    @NotBlank(message = "Токен приложения не может быть пустым")
    @Column(name = "token")
    String token;

}

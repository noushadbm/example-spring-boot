package com.koyeb.rayshan.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
public class UsersEntity {
    @Id
    @Column("user_id")
    private Long id;
    @Column("user_name")
    private String name;
    @Column("user_password")
    private String password;
    @Column("email")
    private String email;
}

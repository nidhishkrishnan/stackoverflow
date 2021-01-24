package com.employee.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "u_id"),
                @UniqueConstraint(columnNames = "u_email")
        })
@Setter
@Getter
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @Column(name = "u_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "u_email")
    @Size(max = 50)
    @Email
    private String email;

    @Column(name = "u_first_name")
    private String firstName;

    @Column(name = "u_last_name")
    private String lastName;
}

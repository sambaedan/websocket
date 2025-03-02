package com.websockets.web_socket.model;

import com.websockets.web_socket.abstractclass.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity<Long> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_SEQ_GEN")
    @SequenceGenerator(name = "user_SEQ_GEN", sequenceName = "user_seq", initialValue = 1, allocationSize = 1)
    @Basic(optional = false)
    private Long id;

    @Column(name = "username",unique = true, nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "token",unique = true)
    private String token;

    public User() {
        this.token = UUID.randomUUID().toString();
    }

}

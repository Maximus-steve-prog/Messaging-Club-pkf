package com.steven.Messaging.Club.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(unique = true , nullable = false)
    private long id;
    @Column( nullable = true)
    private String username;
    @NaturalId(mutable = true)
    @Column(unique = true , nullable = false)
    private String email;
    @Column(nullable = true)
    private String password;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String country;
    @Column(nullable = true)
    private String roles;
    @Column(nullable = true)
    private String file;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = true)
    private String profession;
    @Column(nullable = false)
    private String dob;
    @Column(nullable = true)
    private String status;


    public UserEntity() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
}
package com.mgmt.userprofile.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User  {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(unique = true)
  private String email;
  @Transient
  private String password;
  private String firstName;
  private String lastName;
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  //@JsonIgnore
  private Set<Role> roles = new HashSet<>();

}

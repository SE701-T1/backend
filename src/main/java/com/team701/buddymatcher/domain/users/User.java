package com.team701.buddymatcher.domain.users;

import com.team701.buddymatcher.domain.timetable.Course;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME", nullable = false)
    private String name;

    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "USER_PAIRING_ENABLED", columnDefinition = "boolean default false")
    private Boolean pairingEnabled;

    @ManyToMany(mappedBy = "users")
    private Set<Course> courses = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Boolean getPairingEnabled() {
        return this.pairingEnabled;
    }

    public User setPairingEnabled(Boolean pairingEnabled) {
        this.pairingEnabled = pairingEnabled;
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public User setCourses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }
}

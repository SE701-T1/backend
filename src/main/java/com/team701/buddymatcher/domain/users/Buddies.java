package com.team701.buddymatcher.domain.users;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "BUDDIES", uniqueConstraints={
        @UniqueConstraint(columnNames = {"USER_0_ID", "USER_1_ID"})
})
public class Buddies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_0_ID")
    private User user0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_1_ID")
    private User user1;

    public Long getId() {
        return this.id;
    }

    public Buddies setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser0() {
        return this.user0;
    }

    public Buddies setUser0(User user0) {
        this.user0 = user0;
        return this;
    }

    public User getUser1() {
        return this.user1;
    }

    public Buddies setUser1(User user1) {
        this.user1 = user1;
        return this;
    }
}

package com.example.models.Entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.models.RoleUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor
@Getter
@Setter 
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "first_name")
    private String firstName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private RoleUser role;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shipment> shipment = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> receivedTransaction;


    public User(String name, String firstName, String phone, BigDecimal balance){
        this.firstName = firstName;
        this.name = name;
        this.phone = phone;
        this.balance = balance;
    }
    public User(int id, String name, String firstName, String phone, BigDecimal balance){
        this.id = id;
        this.firstName = firstName;
        this.name = name;
        this.phone = phone;
        this.balance = balance;
    }

    public User(String name, String firstName, String phone){
        this.firstName = firstName;
        this.name = name;
        this.phone = phone;
    }
    public User(Integer id, String name, String firstName, String phone){
        this.id = id;
        this.firstName = firstName;
        this.name = name;
        this.phone = phone;
    }
    public User(
            String name,
            String firstName,
            String email,
            String phone,
            RoleUser role,
            BigDecimal balance
    ) {
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.balance = balance;
    }
}

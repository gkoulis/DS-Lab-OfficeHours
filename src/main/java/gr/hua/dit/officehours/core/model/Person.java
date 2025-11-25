package gr.hua.dit.officehours.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import jakarta.persistence.UniqueConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Person entity.
 */
@Entity
@Table(
    name = "person",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_person_hua_id", columnNames = "hua_id"),
        @UniqueConstraint(name = "uk_person_email_address", columnNames = "email_address"),
        @UniqueConstraint(name = "uk_person_mobile_phone_number", columnNames = "mobile_phone_number")
    },
    indexes = {
        @Index(name = "idx_person_type", columnList = "type"),
        @Index(name = "idx_person_last_name", columnList = "last_name")
    }
)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 20)
    @Column(name = "hua_id", nullable = false, length = 20)
    private String huaId;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(max = 18)
    @Column(name = "mobile_phone_number", nullable = false, length = 18)
    private String mobilePhoneNumber; // E164

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "email_address", nullable = false, length = 100)
    private String emailAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private PersonType type;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Person() {
    }

    public Person(Long id,
                  String huaId,
                  String firstName,
                  String lastName,
                  String mobilePhoneNumber,
                  String emailAddress,
                  PersonType type,
                  String passwordHash,
                  Instant createdAt) {
        this.id = id;
        this.huaId = huaId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.emailAddress = emailAddress;
        this.type = type;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHuaId() {
        return huaId;
    }

    public void setHuaId(String huaId) {
        this.huaId = huaId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public PersonType getType() {
        return type;
    }

    public void setType(PersonType type) {
        this.type = type;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", huaId='" + huaId + '\'' +
            ", type=" + type +
            '}';
    }
}

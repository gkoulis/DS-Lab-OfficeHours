package gr.hua.dit.officehours.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name="hua_id")
    private String huaId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber; // E164

    @Column(name = "email_address")
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PersonType type;

    @Column(name = "password_hash")
    private String passwordHash;

    public Person() {
    }

    public Person(Long id, String huaId,
                  String firstName, String lastName,
                  String mobilePhoneNumber, String emailAddress,
                  PersonType type, String passwordHash) {
        this.id = id;
        this.huaId = huaId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.emailAddress = emailAddress;
        this.type = type;
        this.passwordHash = passwordHash;
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

    @Override
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", huaId='" + huaId + '\'' +
            ", type=" + type +
            '}';
    }
}

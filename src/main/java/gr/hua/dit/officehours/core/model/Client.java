package gr.hua.dit.officehours.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Client entity.
 */
@Entity
@Table(
    name = "client",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_client_name", columnNames = "name")
    }
)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "secret", nullable = false, length = 255)
    private String secret;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?!.*\\bROLE_)[A-Z]+_[A-Z]+(?:,[A-Z]+_[A-Z]+)*$")
    @Column(name = "roles_csv", nullable = false, length = 255)
    private String rolesCsv;

    public Client(Long id, String name, String secret, String rolesCsv) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.rolesCsv = rolesCsv;
    }

    public Client() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRolesCsv() {
        return rolesCsv;
    }

    public void setRolesCsv(String rolesCsv) {
        this.rolesCsv = rolesCsv;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(name, client.name)
            && Objects.equals(secret, client.secret)
            && Objects.equals(rolesCsv, client.rolesCsv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, secret, rolesCsv);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Client{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", secret='").append(secret).append('\'');
        sb.append(", rolesCsv='").append(rolesCsv).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

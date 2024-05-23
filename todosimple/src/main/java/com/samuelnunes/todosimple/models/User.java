package com.samuelnunes.todosimple.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = User.TABLE_NAME)
public class User implements UserDetails {

    public interface CreateUser{}
    public interface UpdateUser{}

    public static final String TABLE_NAME = "user";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AutoIncrement
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 5, max = 100)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 254)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8)
    private String password;

    @Column(name = "role", unique = false, nullable = false)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<Task>();

    public User() {
    }

    public User(Long id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole(){
        return role.toString();
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

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN){
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));
        }else{
            return List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
            else if (!this.id.equals(other.id))
                return false;
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username) && Objects.equals(this.password, other.password);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : id.hashCode());
        return result;
    }

    @JsonIgnore
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}

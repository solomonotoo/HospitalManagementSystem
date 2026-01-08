package com.hms.modules;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_id")
	private Integer roleId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role_name", nullable = false, unique = true, length=50)
	private UserRoles roleName;
	
	@Column(name = "role_description", nullable = false)
    private String roleDescription;
	
//	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
//    @JsonBackReference
//	private Set<User> users = new HashSet<>();
	
	@ManyToMany(mappedBy = "roles")
	@JsonIgnore  // Prevents infinite loop
	private Set<User> users = new HashSet<>();
	
	public Role() {
		super();
	}
	
	public Role( UserRoles roleName, String roleDescription) {
		super();
		this.roleName = roleName;
		this.roleDescription = roleDescription;
	}

	
	
    // Lifecycle hook to set roleDescription
	//Using the @PostLoad and @PostPersist annotations for setting the roleDescription 
	//ensures the Role entity's roleDescription is always in sync with the roleName. 
	//This means you don't need to explicitly call setRoleDescription manually, as it will be 
	//handled automatically by the JPA lifecycle callbacks.
    @PostLoad
    @PostPersist
    private void setRoleDescription() {
        this.roleDescription = this.roleName.getDescription();
    }

    
	// Getters and Setters
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public UserRoles getRoleName() {
        return roleName;
    }

    public void setRoleName(UserRoles roleName) {
        this.roleName = roleName;
        this.roleDescription = roleName.getDescription();
    }

    public String getRoleDescription() {
        return roleDescription;
    }
    
    

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", roleDescription=" + roleDescription + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Role other = (Role) obj;
		if (roleId == null) {
			if (other.roleId != null) {
				return false;
			}
		} else if (!roleId.equals(other.roleId)) {
			return false;
		}
		return true;
	}

//    public Set<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<User> users) {
//        this.users = users;
//    }
	
    
    
}

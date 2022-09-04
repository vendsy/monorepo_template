package com.tray.service.orders.db;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class UserRoleDbo {

	@Id
	@SequenceGenerator(name="roleuser_id_gen",sequenceName="roleuser_sequence" ,initialValue=1,allocationSize=10)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="roleuser_id_gen")
	private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
	private UserDbo user;

	private Role role;

	public UserRoleDbo() {
	}
	
	public UserRoleDbo(UserDbo user, Role r) {
		this.user = user;
		this.user.addRole(this);
		this.role = r;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserDbo getUser() {
		return user;
	}

	public void setUser(UserDbo user) {
		this.user = user;
	}
	
}

package com.tray.service.orders.base;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.webpieces.router.api.extensions.Startable;

import com.tray.service.orders.db.Education;
import com.tray.service.orders.db.Role;
import com.tray.service.orders.db.UserDbo;
import com.tray.service.orders.db.UserRoleDbo;

public class PopulateDatabase implements Startable {

	//private static final Logger log = LoggerFactory.getLogger(PopulateDatabase.class);
	private EntityManagerFactory factory;

	@Inject
	public PopulateDatabase(EntityManagerFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void start() {
		createSomeData();
	}

	private void createSomeData() {
		EntityManager mgr = factory.createEntityManager();
		List<UserDbo> users = UserDbo.findAll(mgr);
		if(users.size() > 0)
			return; //This database has users, exit immediately to not screw up existing data 
		
		EntityTransaction tx = mgr.getTransaction();
		tx.begin();

		UserDbo user1 = new UserDbo();
		user1.setEmail("dean@somewhere.com");
		user1.setName("SomeName");
		user1.setFirstName("Dean");
		user1.setLastName("Hill");

		UserDbo user2 = new UserDbo();
		user2.setEmail("bob@somewhere.com");
		user2.setName("Bob'sName");
		user2.setFirstName("Bob");
		user2.setLastName("LastBob");
		user2.setLevelOfEducation(Education.MIDDLE_SCHOOL);

		UserRoleDbo role1 = new UserRoleDbo(user2, Role.DELINQUINT);
		UserRoleDbo role2 = new UserRoleDbo(user2, Role.BADASS);
		
		mgr.persist(user1);
		mgr.persist(user2);

		mgr.persist(role1);
		mgr.persist(role2);
		
		mgr.flush();
		
		tx.commit();
	}

}

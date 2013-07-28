package com.gemserk.commons.gdx.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

public class Contacts {
	public static class Contact {

		Fixture myFixture;
		Fixture otherFixture;

		boolean inContact = false;

		Vector2 normal = new Vector2();
		Vector2 myBodyPosition = new Vector2();
		Vector2 otherBodyPosition = new Vector2();
		
		// no contact points?

		public void setContact(Fixture myFixture, Fixture otherFixture, Vector2 normal) {
			this.myFixture = myFixture;
			this.otherFixture = otherFixture;
			this.normal.set(normal);
			inContact = true;
			myBodyPosition.set(myFixture.getBody().getPosition());
			otherBodyPosition.set(otherFixture.getBody().getPosition());
		}

		public void unsetContact() {
			inContact = false;
			myFixture = null;
			otherFixture = null;
			normal.set(0f, 0f);
			myBodyPosition.set(0,0);
			otherBodyPosition.set(0,0);
		}

		public Fixture getMyFixture() {
			return myFixture;
		}

		public Fixture getOtherFixture() {
			return otherFixture;
		}

		public boolean isInContact() {
			return inContact;
		}

		public Vector2 getNormal() {
			return normal;
		}
		
		public Vector2 getMyBodyPosition() {
			return myBodyPosition;
		}
		
		public Vector2 getOtherBodyPosition() {
			return otherBodyPosition;
		}

	}

	Array<Contact> contacts = new Array<Contact>(32);
	int activeContacts = 0;
	
	public Contacts() {
		for (int i = 0; i < 31; i++) {
			contacts.add(new Contact());
		}
	}

	public void addContact(com.badlogic.gdx.physics.box2d.Contact contact, boolean AB) {
		Vector2 normal = contact.getWorldManifold().getNormal();
		Fixture myFixture;
		Fixture otherFixture;
		if (AB) {
			myFixture = contact.getFixtureA();
			otherFixture = contact.getFixtureB();
		} else {
			myFixture = contact.getFixtureB();
			otherFixture = contact.getFixtureA();
			normal.scl(-1);// if the body in contact is the first one declared by the contact, then we have to invert the normal.
		}

		addContact(myFixture, otherFixture, normal);
	}

	void addContact(Fixture myFixture, Fixture otherFixture, Vector2 normal) {
		Contact contact = null;
		if (activeContacts == contacts.size) {
			contact = new Contact();
			contacts.add(contact);
		} else {
			contact = contacts.get(activeContacts);
		}

		contact.setContact(myFixture, otherFixture, normal);
		activeContacts++;
	}

	public void removeContact(com.badlogic.gdx.physics.box2d.Contact contact, boolean AB) {
		Fixture myFixture;
		Fixture otherFixture;

		if (AB) {
			myFixture = contact.getFixtureA();
			otherFixture = contact.getFixtureB();
		} else {
			myFixture = contact.getFixtureB();
			otherFixture = contact.getFixtureA();
		}

		removeContact(myFixture, otherFixture);
	}

	public void removeContact(Fixture myFixture, Fixture otherFixture) {

		for (int i = 0; i < activeContacts; i++) {
			Contact contact = contacts.get(i);

			if (contact.myFixture != myFixture || contact.otherFixture != otherFixture) {
				continue;
			}

			contact.unsetContact();
			contacts.set(i, contacts.get(activeContacts - 1));
			contacts.set(activeContacts - 1, contact);
			activeContacts--;
			return;
		}
	}

	public int getContactCount() {
		return activeContacts;
	}

	public Contact getContact(int i) {
		return contacts.get(i);
	}

	public boolean isInContact() {
		return activeContacts != 0;
	}

}

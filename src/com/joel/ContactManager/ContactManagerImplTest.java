package com.joel.ContactManager;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class ContactManagerImplTest {
	
	@Test(expected=NullPointerException.class)
	public void nullContactNamePatternTest() {
		ContactManager mgr = new ContactManagerImpl();
		mgr.getContacts((String)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void unknownContactIdTest() {
		ContactManager mgr = new ContactManagerImpl();
		mgr.getContacts(0);
	}

	@Test
	public void emptyContactListTest() {
		ContactManager mgr = new ContactManagerImpl();
		Set<Contact> contacts = mgr.getContacts("");
		assertEquals("Expected empty list", 0, contacts.size());
	}

	@Test
	public void addOneContactTest() {
		ContactManager mgr = new ContactManagerImpl();
		mgr.addNewContact("Moe Stooge", "The smart one");
		Set<Contact> contacts = mgr.getContacts("");
		assertEquals("Wrong number of contacts", 1, contacts.size());
		Contact[] contactArray = (Contact[]) contacts.toArray();
		assertEquals("Wrong name set", "Moe Stooge",
				contactArray[0].getName());	
		assertEquals("Wrong notes set", "The smart one",
				contactArray[0].getNotes());	
	}

	@Test
	public void addAndFindContactTest() {
		ContactManager mgr = new ContactManagerImpl();
		mgr.addNewContact("Moe Stooge", "The smart one");
		mgr.addNewContact("Larry Stooge", "The crazy one");
		mgr.addNewContact("Curly Stooge", "The bald one");
		Set<Contact> contacts = mgr.getContacts("");
		assertEquals("Wrong number of contacts", 3, contacts.size());
		Contact moe = null, larry = null, curly = null;
		Iterator<Contact> i = contacts.iterator();
		while (i.hasNext()) {
			Contact c = (Contact)i.next();
			switch (c.getName()) {
			case "Moe Stooge":
				moe = c;
				break;
			case "Larry Stooge":
				larry = c;
				break;
			case "Curly Stooge":
				curly = c;
				break;
			default:
				fail("Unexpected name: " + c.getName());
			}
		}
		assertNotNull("Moe is missing", moe);
		assertNotNull("Larry is missing", larry);
		assertNotNull("Curly is missing", curly);
		assertEquals("Notes mismatch", "The smart one", moe.getNotes());
		assertEquals("Notes mismatch", "The crazy one", larry.getNotes());
		assertEquals("Notes mismatch", "The bald one", curly.getNotes());
		Set<Contact> aSet = mgr.getContacts("Moe");
		assertEquals("Wrong number of contacts found", 1, aSet.size());
		assertEquals("Wrong contact found", moe, aSet.toArray()[0]);
		aSet = mgr.getContacts("Larry");
		assertEquals("Wrong number of contacts found", 1, aSet.size());
		assertEquals("Wrong contact found", larry, aSet.toArray()[0]);
		aSet = mgr.getContacts("Curly");
		assertEquals("Wrong number of contacts found", 1, aSet.size());
		assertEquals("Wrong contact found", curly, aSet.toArray()[0]);
		aSet = mgr.getContacts("Stooge");
		assertEquals("Wrong set returned", contacts, aSet);
		Set<Contact> rSet =  mgr.getContacts("r");
		aSet = mgr.getContacts(larry.getId(), curly.getId());
		assertEquals("Wrong subsets selected", aSet, rSet);
	}

}
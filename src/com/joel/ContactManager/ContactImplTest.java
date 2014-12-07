package com.joel.ContactManager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ContactImplTest {
	private Contact contact1;
	private Contact contact2;
	
	
	@Before
	public void setUp() {
		contact1 = new ContactImpl("William Blake", "The dead man");
		contact2 = new ContactImpl("Nobody", "The indian");
	}

	@Test
	public void testGetId() {
		assertNotEquals("Id should be different", contact1.getId(), contact2.getId());
	}
	
	@Test
	public void testGetName() {
		assertEquals("Wrong name 1", "William Blake", contact1.getName());
		assertEquals("Wrong name 2", "Nobody", contact2.getName());
	}
	
	@Test
	public void testGetNotes() {
		assertEquals("Wrong notes 1", "The dead man", contact1.getNotes());
		assertEquals("Wrong notes 2", "The indian", contact2.getNotes());		
	}
	
	@Test
	public void testAddNotes() {
		contact1.addNotes("Not the poet");
		assertEquals("Notes not added", "The dead man\nNot the poet", contact1.getNotes());
	}

}

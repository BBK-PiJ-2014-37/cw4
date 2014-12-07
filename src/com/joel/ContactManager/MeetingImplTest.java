
package com.joel.ContactManager;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * @author joel
 *
 */
public abstract class MeetingImplTest {
	protected Meeting meeting1;
	protected Meeting meeting2;
	protected HashSet<Contact> guests1;
	protected HashSet<Contact> guests2;
	protected Contact moe;
	protected Contact larry;
	protected Contact curly;
	protected Calendar date1; 
	protected Calendar date2;

	public abstract void getTwoMeetings();
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		moe = new ContactImpl("Moe", "");
		larry = new ContactImpl("Larry", "");
		curly = new ContactImpl("Curly", "");
		date1 = new GregorianCalendar(2014, 12, 31);
		date2 = new GregorianCalendar(2014, 12, 24);
		guests1 = new HashSet<Contact>(Arrays.asList(moe, larry));
		guests2 = new HashSet<Contact>(Arrays.asList(moe, larry, curly));
		getTwoMeetings();
	}

	@Test
	public void testGetId() {
		assertNotEquals("Id should be different", meeting1.getId(), meeting2.getId());
	}

	@Test
	public void testGetDate() {
		assertEquals("Meeting 1 day wrong", date1, meeting1.getDate());
		assertEquals("Meeting 2 day wrong", date2, meeting2.getDate());
	}

	@Test
	public void testGetContacts() {
		assertEquals("Meeting 1 wrong list of guests", guests1, meeting1.getContacts());
		assertEquals("Meeting 2 wrong list of guests", guests2, meeting2.getContacts());
	}


}

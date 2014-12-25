package com.joel.ContactManager;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ContactManagerImplTest {
	ContactManager mgr;
	Contact moe, larry, curly;
	
	@Before
	public void setUp() {
		mgr = new ContactManagerImpl();
	}
	
	@Test(expected=NullPointerException.class)
	public void nullContactNamePatternTest() {
		mgr.getContacts((String)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void unknownContactIdTest() {
		mgr.getContacts(0);
	}

	@Test
	public void emptyContactListTest() {
		Set<Contact> contacts = mgr.getContacts("");
		assertEquals("Expected empty list", 0, contacts.size());
	}

	@Test
	public void addOneContactTest() {
		mgr.addNewContact("Moe Stooge", "The smart one");
		Set<Contact> contacts = mgr.getContacts("");
		assertEquals("Wrong number of contacts", 1, contacts.size());
		Contact c = contacts.iterator().next();
		assertEquals("Wrong name set", "Moe Stooge", c.getName());	
		assertEquals("Wrong notes set", "The smart one", c.getNotes());	
	}

	@Test
	public void addAndFindContactTest() {
		mgr.addNewContact("Moe Stooge", "The smart one");
		mgr.addNewContact("Larry Stooge", "The crazy one");
		mgr.addNewContact("Curly Stooge", "The bald one");
		Set<Contact> contacts = mgr.getContacts("");
		assertEquals("Wrong number of contacts", 3, contacts.size());
		moe = null;
		larry = null;
		curly = null;
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
		assertEquals("Wrong contact found", moe, aSet.iterator().next());
		aSet = mgr.getContacts("Larry");
		assertEquals("Wrong number of contacts found", 1, aSet.size());
		assertEquals("Wrong contact found", larry, aSet.iterator().next());
		aSet = mgr.getContacts("Curly");
		assertEquals("Wrong number of contacts found", 1, aSet.size());
		assertEquals("Wrong contact found", curly, aSet.iterator().next());
		aSet = mgr.getContacts("Stooge");
		assertEquals("Wrong set returned", contacts, aSet);
		Set<Contact> rSet =  mgr.getContacts("r");
		aSet = mgr.getContacts(larry.getId(), curly.getId());
		assertEquals("Wrong subsets selected", aSet, rSet);
	}

	private void setUpContacts() {
		mgr.addNewContact("Moe Stooge", "The smart one");
		mgr.addNewContact("Larry Stooge", "The crazy one");
		mgr.addNewContact("Curly Stooge", "The bald one");
		moe = mgr.getContacts("Moe").iterator().next();
		larry = mgr.getContacts("Larry").iterator().next();
		curly = mgr.getContacts("Curly").iterator().next();
	}

	@Test(expected=IllegalArgumentException.class)
	public void futureMeetingInPastTest() {
		setUpContacts();
		mgr.addFutureMeeting(
				mgr.getContacts(moe.getId()),
				new GregorianCalendar(1963, 01, 30));
	}

	@Test(expected=IllegalArgumentException.class)
	public void futureMeetingEmptyGuests() {
		setUpContacts();
		mgr.addFutureMeeting(
				new HashSet<Contact>(),
				new GregorianCalendar(2020, 01, 30));
	}

	@Test(expected=IllegalArgumentException.class)
	public void futureMeetingUnknownGuest() {
		setUpContacts();
		Contact wrongContact = new ContactImpl("Charlie Chaplin", "The Tramp");
		Set<Contact> guests = new HashSet<Contact>();
		guests.add(moe);
		guests.add(wrongContact);
		mgr.addFutureMeeting(
				guests,	new GregorianCalendar(2020, 01, 30));
	}

	@Test
	public void getMeetingWhileEmpty() {
		assertNull("No meeting expected", mgr.getMeeting(1));
	}
	
	@Test
	public void addFutureMeetingTest() {
		setUpContacts();
		Calendar date = new GregorianCalendar(2020, 01, 30);
		Set<Contact> guests = mgr.getContacts(moe.getId());
		int meetId = mgr.addFutureMeeting(guests, date);
		FutureMeeting meet = mgr.getFutureMeeting(meetId);
		assertNotNull("Expected a meeting", meet);
		assertEquals("Wrong guest list", guests, meet.getContacts());
		assertEquals("Wrong date", date, meet.getDate());
		assertEquals("Expected to find", meet, mgr.getMeeting(meetId));
	}
	
	@Test
	public void addSeveralFutureMeetingsTest() {
		setUpContacts();
		
		Calendar date = new GregorianCalendar(2020, 01, 30);
		Calendar otherDate = new GregorianCalendar(2020, 02, 12);
		Set<Contact> guests1 = mgr.getContacts(moe.getId());
		Set<Contact> guests2 = mgr.getContacts(moe.getId(), larry.getId());
		Set<Contact> guests3 = mgr.getContacts(moe.getId());

		int meet1Id = mgr.addFutureMeeting(guests1, date);
		int meet2Id = mgr.addFutureMeeting(guests2, date);
		int meet3Id = mgr.addFutureMeeting(guests3, otherDate);
	
		FutureMeeting meet1 = mgr.getFutureMeeting(meet1Id);
		assertNotNull("Expected meeting", meet1);
		assertEquals("Wrong guest list", guests1, meet1.getContacts());
		assertEquals("Wrong date list", date, meet1.getDate());
		FutureMeeting meet2 = mgr.getFutureMeeting(meet2Id);
		assertNotNull("Expected meeting", meet2);
		assertEquals("Wrong guest list", guests2, meet2.getContacts());
		assertEquals("Wrong date list", date, meet2.getDate());
		FutureMeeting meet3 = mgr.getFutureMeeting(meet3Id);
		assertNotNull("Expected meeting", meet3);
		assertEquals("Wrong guest list", guests3, meet3.getContacts());
		assertEquals("Wrong date list", otherDate, meet3.getDate());

		assertEquals("Find mismatch",
				mgr.getFutureMeeting(meet1Id), mgr.getMeeting(meet1Id));
		assertEquals("Find mismatch",
				mgr.getFutureMeeting(meet2Id), mgr.getMeeting(meet2Id));
		assertEquals("Find mismatch",
				mgr.getFutureMeeting(meet3Id), mgr.getMeeting(meet3Id));
		
		Meeting[] expectedMeetsMoe = {meet1, meet2, meet3};
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(Arrays.asList(expectedMeetsMoe)),
				new HashSet<Meeting>(mgr.getFutureMeetingList(moe)));
		Meeting[] expectedMeetsLarry = {meet2};
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(Arrays.asList(expectedMeetsLarry)),
				new HashSet<Meeting>(mgr.getFutureMeetingList(larry)));
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(),
				new HashSet<Meeting>(mgr.getFutureMeetingList(curly)));

		Meeting[] expectedMeetsDate = {meet1, meet2};
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(Arrays.asList(expectedMeetsDate)),
				new HashSet<Meeting>(mgr.getFutureMeetingList(date)));
		Meeting[] expectedMeetsOtherDate = {meet3};
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(Arrays.asList(expectedMeetsOtherDate)),
				new HashSet<Meeting>(mgr.getFutureMeetingList(otherDate)));
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(),
				new HashSet<Meeting>(mgr.getFutureMeetingList(
						new GregorianCalendar(2020, 10, 11))));

		assertEquals("Wrong meeting list",
				new HashSet<PastMeeting>(),
				new HashSet<PastMeeting>(mgr.getPastMeetingList(moe)));

	}

	@Test(expected=IllegalArgumentException.class)
	public void pastMeetingInFutureTest() {
		setUpContacts();
		mgr.addNewPastMeeting(mgr.getContacts(moe.getId()), new GregorianCalendar(2100, 01, 30),
				"Bogus notes");
	}

	@Test(expected=IllegalArgumentException.class)
	public void pastMeetingEmptyGuests() {
		setUpContacts();
		mgr.addNewPastMeeting(new HashSet<Contact>(), new GregorianCalendar(1963, 01, 30),
				"Bogus notes");
	}

	@Test(expected=NullPointerException.class)
	public void pastMeetingNullNotes() {
		setUpContacts();
		mgr.addNewPastMeeting(mgr.getContacts(moe.getId()), new GregorianCalendar(1963, 01, 30), null);
	}

	@Test(expected=NullPointerException.class)
	public void pastMeetingNullGuests() {
		setUpContacts();
		mgr.addNewPastMeeting(null,	new GregorianCalendar(1963, 01, 30), "Bogus notes");
	}

	@Test(expected=NullPointerException.class)
	public void pastMeetingNullDate() {
		setUpContacts();
		mgr.addNewPastMeeting(new HashSet<Contact>(), null, "Bogus notes");
	}

	@Test(expected=IllegalArgumentException.class)
	public void pastMeetingUnknownGuest() {
		setUpContacts();
		Contact wrongContact = new ContactImpl("Charlie Chaplin", "The Tramp");
		Set<Contact> guests = new HashSet<Contact>();
		guests.add(moe);
		guests.add(wrongContact);
		mgr.addFutureMeeting(guests, new GregorianCalendar(1963, 01, 30));
	}
	
	@Test
	public void addPastMeetingTest() {
		setUpContacts();
		Calendar date = new GregorianCalendar(1963, 01, 30);
		Set<Contact> guests = mgr.getContacts(moe.getId());
		int meetId = mgr.addNewPastMeeting(guests, date, "Nothing happened");
		PastMeeting meet = (PastMeeting)mgr.getMeeting(meetId);
		assertNotNull("Expected a meeting", meet);
		assertEquals("Wrong guest list", guests, meet.getContacts());
		assertEquals("Wrong date", date, meet.getDate());
		assertEquals("Wrong notes", "Nothing happened", meet.getNotes());
		assertEquals("Expected to find", meet, mgr.getMeeting(meetId));
	}
	
	@Test
	public void addSeveralPastMeetingsTest() {
		setUpContacts();
		
		Calendar date = new GregorianCalendar(1963, 01, 30);
		Calendar otherDate = new GregorianCalendar(1968, 02, 12);
		Set<Contact> guests1 = mgr.getContacts(moe.getId());
		Set<Contact> guests2 = mgr.getContacts(moe.getId(), larry.getId());
		Set<Contact> guests3 = mgr.getContacts(moe.getId());

		int meet1Id = mgr.addNewPastMeeting(guests1, date, "Moe got bored");
		int meet2Id = mgr.addNewPastMeeting(guests2, date, "Moe hit Larry");
		int meet3Id = mgr.addNewPastMeeting(guests3, otherDate, "Moe got bored again");
	
		PastMeeting meet1 = mgr.getPastMeeting(meet1Id);
		assertNotNull("Expected meeting", meet1);
		assertEquals("Wrong guest list", guests1, meet1.getContacts());
		assertEquals("Wrong date list", date, meet1.getDate());
		PastMeeting meet2 = mgr.getPastMeeting(meet2Id);
		assertNotNull("Expected meeting", meet2);
		assertEquals("Wrong guest list", guests2, meet2.getContacts());
		assertEquals("Wrong date list", date, meet2.getDate());
		PastMeeting meet3 = mgr.getPastMeeting(meet3Id);
		assertNotNull("Expected meeting", meet3);
		assertEquals("Wrong guest list", guests3, meet3.getContacts());
		assertEquals("Wrong date list", otherDate, meet3.getDate());

		assertEquals("Find mismatch",
				mgr.getPastMeeting(meet1Id), mgr.getMeeting(meet1Id));
		assertEquals("Find mismatch",
				mgr.getPastMeeting(meet2Id), mgr.getMeeting(meet2Id));
		assertEquals("Find mismatch",
				mgr.getPastMeeting(meet3Id), mgr.getMeeting(meet3Id));
		
		Meeting[] expectedMeetsMoe = {meet1, meet2, meet3};
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(Arrays.asList(expectedMeetsMoe)),
				new HashSet<Meeting>(mgr.getPastMeetingList(moe)));
		Meeting[] expectedMeetsLarry = {meet2};
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(Arrays.asList(expectedMeetsLarry)),
				new HashSet<Meeting>(mgr.getPastMeetingList(larry)));
		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(),
				new HashSet<Meeting>(mgr.getPastMeetingList(curly)));

		assertEquals("Wrong meeting list",
				new HashSet<Meeting>(),
				new HashSet<Meeting>(mgr.getFutureMeetingList(moe)));
	}

	@Test
	public void addNotesToPastMeetingTest() {
		setUpContacts();
		Calendar date = new GregorianCalendar(1963, 01, 30);
		Set<Contact> guests = mgr.getContacts(moe.getId());
		int meetId = mgr.addNewPastMeeting(guests, date, "Nothing happened");
		mgr.addMeetingNotes(meetId, "Really, nothing happened");
		PastMeeting meet = (PastMeeting)mgr.getMeeting(meetId);
		assertNotNull("Expected a meeting", meet);
		assertEquals("Wrong guest list", guests, meet.getContacts());
		assertEquals("Wrong date", date, meet.getDate());
		assertEquals("Wrong notes", "Really, nothing happened", meet.getNotes());
		assertEquals("Expected to find", meet, mgr.getMeeting(meetId));
	}

	@Test(expected=IllegalStateException.class)
	public void addNotesToFutureMeetingTest() {
		setUpContacts();
		Calendar date = new GregorianCalendar(2100, 01, 30);
		Set<Contact> guests = mgr.getContacts(moe.getId());
		int meetId = mgr.addFutureMeeting(guests, date);
		mgr.addMeetingNotes(meetId, "Really, nothing happened");
	}

	@Test
	public void addNotesToPastFutureMeetingTest() {
		// can't figure out a way to test this, because the interface doesn't
		// allow to add a FutureMeeting in with a past date.
	}
}
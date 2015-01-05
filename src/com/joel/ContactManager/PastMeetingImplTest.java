package com.joel.ContactManager;

import static org.junit.Assert.*;

import org.junit.Test;

public class PastMeetingImplTest extends MeetingImplTest {
	String notes1;
	String notes2;

	public void getTwoMeetings() {
		notes1 = "Boring meeting";
		notes2 = "Very boring meeting";
		meeting1 = new PastMeetingImpl(guests1, date1, notes1);
		meeting2 = new PastMeetingImpl(guests2, date2, notes2);
	}

	@Test
	public void testGetNotes() {
		assertEquals("Wrong notes for meeting 1", notes1,
				((PastMeeting) meeting1).getNotes());
		assertEquals("Wrong notes for meeting 2", notes2,
				((PastMeeting) meeting2).getNotes());
	}

	@Test
	public void testConstructFromFutureMeeting() {
		FutureMeeting fm = new FutureMeetingImpl(guests1, date1);
		PastMeeting pm = new PastMeetingImpl(fm, notes1);
		assertEquals("Wrong id for past meeting from future", fm.getId(), pm.getId());
		assertEquals("Wrong guests for past meeting from future",
				fm.getContacts(), pm.getContacts());
		assertEquals("Wrong date for past meeting from future",
				fm.getDate(), pm.getDate());
		assertEquals("Wrong notes for past meeting from future",
				notes1, pm.getNotes());
	}

	@Test
	public void testConstructFromPastMeeting() {
		PastMeeting ma = new PastMeetingImpl(guests1, date1, notes1);
		PastMeeting mb = new PastMeetingImpl(ma, notes2);
		assertEquals("Wrong id for past meeting from future", ma.getId(), mb.getId());
		assertEquals("Wrong guests for past meeting from future",
				ma.getContacts(), mb.getContacts());
		assertEquals("Wrong date for past meeting from future",
				ma.getDate(), mb.getDate());
		assertEquals("Wrong notes for past meeting from future",
				notes1 + "\n" + notes2, mb.getNotes());

	}
}

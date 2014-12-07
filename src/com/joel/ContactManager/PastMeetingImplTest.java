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
		assertEquals("Wrong notes for meeting 1", "Boring meeting",
				((PastMeeting) meeting1).getNotes());
		assertEquals("Wrong notes for meeting 2", "Very boring meeting",
				((PastMeeting) meeting2).getNotes());
	}

}

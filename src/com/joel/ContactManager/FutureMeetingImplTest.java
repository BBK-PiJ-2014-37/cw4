package com.joel.ContactManager;

public class FutureMeetingImplTest extends MeetingImplTest {

	public void getTwoMeetings() {
		meeting1 = new FutureMeetingImpl(guests1, date1);
		meeting2 = new FutureMeetingImpl(guests2, date2);
	}

}

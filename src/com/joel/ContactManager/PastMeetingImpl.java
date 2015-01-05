package com.joel.ContactManager;

import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
	private String notes;

	/**
	 * Initialize a PastMeeting from raw data
	 * 
	 * @param contacts Set of invited contacts
	 * @param date meeting date
	 * @param notes notes for the meeting.
	 */
	public PastMeetingImpl(Set<Contact> contacts, Calendar date, String notes) {
		super(contacts, date);
		this.notes = notes;
	}

	/**
	 * Initialize a PastMeeting from a FutureMeeting, adding notes
	 * and keeping the id.
	 * 
	 * @param fm FutureMeeting to copy
	 * @param notes notes for the meeting.
	 */
	public PastMeetingImpl(FutureMeeting fm, String notes) {
		super((Meeting)fm);
		this.notes = notes;
	}
	
	/**
	 * Initialize a PastMeeting from another PastMeeting, adding to
	 * the notes and keeping the id.
	 * 
	 * @param pm PastMeeting to copy
	 * @param notes notes for the meeting.
	 */
	public PastMeetingImpl(PastMeeting pm, String notes) {
		super((Meeting)pm);
		this.notes = pm.getNotes() + "\n" + notes;
	}

	public String getNotes() {
		return notes;
	}

}

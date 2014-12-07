package com.joel.ContactManager;

import java.util.Calendar;
import java.util.Set;

/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public abstract class MeetingImpl implements Meeting {

	/**
	 * @param contacts
	 *            a list of participants
	 * @param date
	 *            the meeting date
	 */
	public MeetingImpl(Set<Contact> contacts, Calendar date) {
	}

	/**
	 * Returns the id of the meeting.
	 *
	 * @return the id of the meeting.
	 */
	public int getId() {
		return 0;
	}

	/**
	 * Return the date of the meeting.
	 *
	 * @return the date of the meeting.
	 */
	public Calendar getDate() {
		return null;
	}

	/**
	 * Return the details of people that attended the meeting.
	 *
	 * The list contains a minimum of one contact (if there were just two
	 * people: the user and the contact) and may contain an arbitraty number of
	 * them.
	 *
	 * @return the details of people that attended the meeting.
	 */
	public Set<Contact> getContacts() {
		return null;
	}

}

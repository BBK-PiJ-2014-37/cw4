package com.joel.ContactManager;

import java.util.Calendar;
import java.util.Set;

/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public abstract class MeetingImpl implements Meeting {
	protected Set<Contact> contacts;
	protected Calendar date;
	protected int id;
	/**
	 * Next Id to use
	 */
	private static int nextId = 1;

	/**
	 * Set private fields.
	 * 
	 * @param contacts
	 *            a list of participants
	 * @param date
	 *            the meeting date
	 */
	private void setFields(Set<Contact> contacts, Calendar date) {
		this.contacts = contacts;
		this.date = date;
	}
	
	/**
	 * Initialize a Meeeting from raw data, allocating a new id.
	 * 
	 * @param contacts
	 *            a list of participants
	 * @param date
	 *            the meeting date
	 */
	public MeetingImpl(Set<Contact> contacts, Calendar date) {
		setFields(contacts, date);
		this.id = nextId;
		nextId++;
	}
	
	/**
	 * Initialize a new Meeting by copying another, id and all.
	 * 
	 * @param m the meeting to copy.
	 */
	public MeetingImpl(Meeting m) {
		setFields(m.getContacts(), m.getDate());
		this.id = m.getId();
	}

	/**
	 * Returns the id of the meeting.
	 *
	 * @return the id of the meeting.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Return the date of the meeting.
	 *
	 * @return the date of the meeting.
	 */
	public Calendar getDate() {
		return this.date;
	}

	/**
	 * Return the details of people that attended the meeting.
	 *
	 * The list contains a minimum of one contact (if there were just two
	 * people: the user and the contact) and may contain an arbitrary number of
	 * them.
	 *
	 * @return the details of people that attended the meeting.
	 */
	public Set<Contact> getContacts() {
		return this.contacts;
	}

}

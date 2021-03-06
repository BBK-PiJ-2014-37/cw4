package com.joel.ContactManager;

/**
 * A contact is a person we are making business with or may do in the future.
 *
 * Contacts have an ID (unique), a name (probably unique, but maybe not), and
 * notes that the user may want to save about them.
 */
public class ContactImpl implements Contact {
	private static int nextId = 1;
	private String name;
	private String notes;
	private int id;
	
	/**
	 * Construct the contact and assign it an ID.
	 *
	 * @param name the name of the contact
	 * @param notes the initial notes for the contact.
	 */
	public ContactImpl(String name, String notes) {
		this.name = name;
		this.notes = notes;
		this.id = nextId;
		nextId++;
	}

	/**
	 * Returns the ID of the contact.
	 *
	 * @return the ID of the contact.
	 */
	public int getId() { return id; }

	/**
	 * Returns the name of the contact.
	 *
	 * @return the name of the contact.
	 */
	public String getName() { return name; }

	/**
	 * Returns our notes about the contact, if any.
	 *
	 * If we have not written anything about the contact, the empty string is
	 * returned.
	 *
	 * @return a string with notes about the contact, maybe empty.
	 */
	public String getNotes() { return notes; }

	/**
	 * Add notes about the contact.
	 *
	 * @param note
	 *            the notes to be added
	 */
	public void addNotes(String note) {
		notes += "\n" + note;
	}	
}
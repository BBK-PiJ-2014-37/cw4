package com.joel.ContactManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class to manage your contacts and meetings.
 */
public class ContactManagerImpl implements ContactManager{
	private static final String SAVEFILENAME = "/tmp/contacts.txt";
	private Map<Integer,Contact> contactList;
	private Map<Integer,FutureMeeting> futureMeetingList;
	private Map<Integer,PastMeeting> pastMeetingList;
	
	public ContactManagerImpl() {
		this.contactList = new HashMap<Integer,Contact>();
		this.futureMeetingList = new HashMap<Integer,FutureMeeting>();
		this.pastMeetingList = new HashMap<Integer,PastMeeting>();
	}
		
	/**
	 * Add a new meeting to be held in the future.
	 *
	 * @param contacts
	 *            a list of contacts that will participate in the meeting
	 * @param date
	 *            the date on which the meeting will take place
	 * @return the ID for the meeting
	 * @throws IllegalArgumentException
	 *             if the meeting is set for a time in the past,
	 *
	 *             of if any contact is unknown / non-existent
	 */
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		if (date.before(new GregorianCalendar())) {
			throw new IllegalArgumentException("Date not in future " + date);
		}
		if (contacts.size() == 0 || !validateContacts(contacts)) {
			throw new IllegalArgumentException("Unknown contact(s)");			
		}
		FutureMeeting m = new FutureMeetingImpl(contacts, date);
		futureMeetingList.put(m.getId(), m);
		return m.getId();
	}

	/**
	 * Returns the PAST meeting with the requested ID, or null if it there is
	 * none.
	 * 
	 * PROBLEM WITH THIS METHOD: Say a user added a future meeting. Time
	 * passes. Now it's in the past. User calls getPastMeeting to retrieve
	 * the meeting. They get a PastMeeting pointer, but the object is
	 * is actually a FutureMeeting! This can lead to run-time errors.
	 * 
	 * An alternative could be to have this method replace the FutureMeeting
	 * with a PastMeeting, and return that instead, but that would involve
	 * asking for the object's class. Since replacing the FutureMeeting with
	 * a PastMeeting involves getting a new ID, the user would also get a
	 * meeting with a different ID than they asked for, which is at least
	 * confusing.
	 * 
	 * I can't think of a way to solve this without violating the interface
	 * provided for the coursework.
	 *
	 * @param id
	 *            the ID for the meeting
	 * @return the meeting with the requested ID, or null if it there is none.
	 * @throws IllegalArgumentException
	 *             if there is a meeting with that ID happening in the future
	 */
	public PastMeeting getPastMeeting(int id) {
		if (futureMeetingList.get(id) != null) {
			throw new IllegalArgumentException();
		}
		return pastMeetingList.get(id);
	}

	/**
	 * Returns the FUTURE meeting with the requested ID, or null if there is
	 * none.
	 *
	 * @param id
	 *            the ID for the meeting
	 * @return the meeting with the requested ID, or null if it there is none.
	 * @throws IllegalArgumentException
	 *             if there is a meeting with that ID happening in the past
	 */
	public FutureMeeting getFutureMeeting(int id) {
		if (pastMeetingList.get(id) != null) {
			throw new IllegalArgumentException();
		}
		return futureMeetingList.get(id);
	}

	/**
	 * Returns the meeting with the requested ID, or null if it there is none.
	 *
	 * @param id
	 *            the ID for the meeting
	 * @return the meeting with the requested ID, or null if it there is none.
	 */
	public Meeting getMeeting(int id) {
		Meeting m = pastMeetingList.get(id);
		if (m != null) {
			return m;
		}
		return futureMeetingList.get(id);
	}

	/**
	 * Returns the list of meetings scheduled with this contact.
	 *
	 * If there are none, the returned list will be empty. Otherwise, the list
	 * will be chronologically sorted and will not contain any duplicates.
	 *
	 * @param contact
	 *            one of the user’s contacts
	 * @return the list of meeting(s) scheduled with this contact (maybe
	 *         empty).
	 * @throws IllegalArgumentException
	 *             if the contact does not exist
	 */
	private List<Meeting>getMeetingList(Contact contact) {	
		if (!contactList.containsKey(contact.getId())) {
			throw new IllegalArgumentException("Contact " + contact.getName()
					+ " does not exist.");
		}
		List<Meeting> result = new ArrayList<Meeting>(); 
		for (Meeting m : futureMeetingList.values()) {
			if(m.getContacts().contains(contact)) {
				result.add(m);
			}
		}
		for (Meeting m : pastMeetingList.values()) {
			if(m.getContacts().contains(contact)) {
				result.add(m);
			}
		}
		result.sort ((Meeting a, Meeting b) -> a.getDate().compareTo(b.getDate()));
		return result;
	}

	/**
	 * Returns the list of future meetings scheduled with this contact.
	 *
	 * If there are none, the returned list will be empty. Otherwise, the list
	 * will be chronologically sorted and will not contain any duplicates.
	 *
	 * @param contact
	 *            one of the user’s contacts
	 * @return the list of future meeting(s) scheduled with this contact (maybe
	 *         empty).
	 * @throws IllegalArgumentException
	 *             if the contact does not exist
	 */
	public List<Meeting> getFutureMeetingList(Contact contact) {
		List<Meeting> list = new ArrayList<Meeting>();
		GregorianCalendar today = new GregorianCalendar();
		for (Meeting meet: getMeetingList(contact)) {
			if (meet.getDate().after(today)) {
				list.add(meet);
			}
		}
		return list;
	}

	/**
	 * Returns the list of meetings that are scheduled for, or that took place
	 * on, the specified date
	 *
	 * If there are none, the returned list will be empty. Otherwise, the list
	 * will be chronologically sorted and will not contain any duplicates.
	 *
	 * @param date
	 *            the date
	 * @return the list of meetings
	 */
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<Meeting> result = new ArrayList<Meeting>(); 
		for (Meeting m : pastMeetingList.values()) {
			if(m.getDate().equals(date)) {
				result.add(m);
			}
		}
		for (Meeting m : futureMeetingList.values()) {
			if(m.getDate().equals(date)) {
				result.add(m);
			}
		}
		result.sort ((Meeting a, Meeting b) -> a.getDate().compareTo(b.getDate()));
		return result;
	}
	
	/**
	 * Returns the list of past meetings in which this contact has participated.
	 *
	 * If there are none, the returned list will be empty. Otherwise, the list
	 * will be chronologically sorted and will not contain any duplicates.
	 *
	 * @param contact
	 *            one of the user’s contacts
	 * @return the list of future meeting(s) scheduled with this contact (maybe
	 *         empty).
	 * @throws IllegalArgumentException
	 *             if the contact does not exist
	 */
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		List<PastMeeting> list = new ArrayList<PastMeeting>();
		for (PastMeeting meet: pastMeetingList.values()) {
			if (meet.getContacts().contains(contact)) {
				list.add(meet);
			}
		}
		list.sort ((Meeting a, Meeting b) -> a.getDate().compareTo(b.getDate()));
		return list;
	}

	/**
	 * Create a new record for a meeting that took place in the past.
	 *
	 * @param contacts
	 *            a list of participants
	 * @param date
	 *            the date on which the meeting took place
	 * @param text
	 *            messages to be added about the meeting.
	 * @throws IllegalArgumentException
	 *             if the list of contacts is
	 *
	 *             empty, or any of the contacts does not exist
	 * @throws NullPointerException
	 *             if any of the arguments is null
	 */
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
		if (date.after(new GregorianCalendar())) {
			throw new IllegalArgumentException("Date not in past " + date);
		}
		if (contacts.size() == 0 || !validateContacts(contacts)) {
			throw new IllegalArgumentException("Unknown contact(s)");			
		}
		if (text == null) {
			throw new NullPointerException("Expected meeting notes");
		}
		PastMeeting m = new PastMeetingImpl(contacts, date, text);
		pastMeetingList.put(m.getId(), m);
	}

	/**
	 * Add notes to a meeting.
	 *
	 * This method is used when a future meeting takes place, and is then
	 * converted to a past meeting (with notes).
	 *
	 * It can be also used to add notes to a past meeting at a later date.
	 *
	 * @param id
	 *            the ID of the meeting
	 * @param text
	 *            messages to be added about the meeting.
	 * @throws IllegalArgumentException
	 *             if the meeting does not exist
	 * @throws IllegalStateException
	 *             if the meeting is set for a date in the future
	 * @throws NullPointerException
	 *             if the notes are null
	 */
	public void addMeetingNotes(int id, String text) {
		PastMeeting pm = pastMeetingList.get(id);
		FutureMeeting fm = futureMeetingList.get(id);
		if (fm == null && pm == null) {
			throw new IllegalArgumentException(
					"Attempt to add note to non-existing meeting");
		}
		// only one of pm and fm can be non-null
		if (fm != null) {
			if (fm.getDate().after(new GregorianCalendar())) {
				throw new IllegalStateException(
						"Attempt to add note to future meeting");
			}
			addNewPastMeeting(fm.getContacts(), fm.getDate(), text);
			futureMeetingList.remove(fm.getId());
		} else {
			pastMeetingList.put(id, new PastMeetingImpl(pm, text));
		}
	}

	/**
	 * Validate that all contacts in a set are known.
	 *
	 * @param contacts
	 *            the contacts to validate.
	 * @throws NullPointerException
	 *             if the contact list is null
	 */	
	private boolean validateContacts(Collection<Contact> contacts) {
		Set<Contact> valid = new HashSet<Contact>(contactList.values());
		for (Contact c: contacts) {
			if (!valid.contains(c)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Create a new contact with the specified name and notes.
	 *
	 * @param name
	 *            the name of the contact.
	 * @param notes
	 *            notes to be added about the contact.
	 * @throws NullPointerException
	 *             if the name or the notes are null
	 */
	public void addNewContact(String name, String notes) {
		Contact c = new ContactImpl(name, notes);
		contactList.put(c.getId(), c);
	}

	/**
	 * Returns a list containing the contacts that correspond to the IDs.
	 *
	 * @param ids
	 *            an arbitrary number of contact IDs
	 * @return a list containing the contacts that correspond to the IDs.
	 * @throws IllegalArgumentException
	 *             if any of the IDs does not correspond to a real contact
	 */
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> matches = new HashSet<Contact>();
		for (int id: ids) {
			Contact c = contactList.get(id);
			if (c == null) {
				throw new IllegalArgumentException("No contact with id "+ id);
			}
			matches.add(c);
		}
		return matches;
	}

	/**
	 * Returns a list with the contacts whose name contains that string.
	 *
	 * @param name
	 *            the string to search for
	 * @return a list with the contacts whose name contains that string.
	 * @throws NullPointerException
	 *             if the parameter is null
	 */
	public Set<Contact> getContacts(String name) {
		if (name == null) {
			throw new NullPointerException("Search for null string");
		}
		Set<Contact> matches = new HashSet<Contact>();
		Iterator<Contact> i = contactList.values().iterator();
		while (i.hasNext()) {
			Contact c = i.next();
			if (c.getName().contains(name)) {
				matches.add(c);
			}
		}
		return matches;
	}

	/**
	 * Writes an escaped copy of the string, with newlines turned into "\n", 
	 * backslashes turned into "\\" and an appended newline.
	 *
	 * @param w the stream to write to
	 * @param s the string to escape
	 */
	private static void writeEscaped(Writer w, String s) throws IOException {
		s = s.replace("\\", "\\\\");
		s = s.replace("\n", "\\n") + "\n";
		w.write(s, 0, s.length());
	}

	/**
	 * Read an unescaped line from the reader, with "\n" turned into newlines, 
	 * "\\" turned into backslashes and without the trailing newline.
	 *
	 * @param s the string to unescape
	 * @return the unescaped string.
	 */
	private static String readUnescaped(BufferedReader r) throws IOException {
		String s = r.readLine();
		// if < 2 chars left, no escaping possible
		if (s.length() < 2) {
			return s;
		}
		String res = "";
		int i = 0;
		for (; i < s.length()-1; i++) {
			if (s.charAt(i) == '\\') {
				i++;
				if (s.charAt(i) == 'n') {
					res += "\n";
				} else {
					res += s.charAt(i);
				}
			} else {
				res += s.charAt(i);
			}
		}
		// i == s.length() only if second-to-last char was a backslash
		if (i == s.length()) {
			return res;
		} else {
			return res + s.charAt(s.length()-1);
		}
	}

	/**
	 * Save all data to disk.
	 *
	 * This method must be executed when the program is closed and when/if the
	 * user requests it.
	 * 
	 * The fact that Contact and Meeting ids are managed by the respective
	 * classes, and we don't have a way to assign IDs to them, means that
	 * the objects will have different IDs when read in again. This forces
	 * us to use the contact names as keys when storing the meeting contacts.
	 * This may lead to unexpected results if contacts with the same name
	 * exist.
	 */
	public void flush() {
		try {
			FileWriter file = new FileWriter(SAVEFILENAME);
			BufferedWriter writer = new BufferedWriter(file);
			for (Contact c: contactList.values()) {
				writeEscaped(writer, c.getName());
				writeEscaped(writer, c.getNotes());
			}
			writer.newLine();
			for (PastMeeting m: pastMeetingList.values()) {
				Calendar date = m.getDate();
				writeEscaped(writer, "" + date.get(Calendar.YEAR));
				writeEscaped(writer, "" + date.get(Calendar.MONTH));
				writeEscaped(writer, "" + date.get(Calendar.DAY_OF_MONTH));
				for (Contact c: m.getContacts()) {
					writeEscaped(writer, c.getName());
				}
				writer.newLine();
				writeEscaped(writer, m.getNotes());
			}
			writer.newLine();
			for (FutureMeeting m: futureMeetingList.values()) {
				Calendar date = m.getDate();
				writeEscaped(writer, "" + date.get(Calendar.YEAR));
				writeEscaped(writer, "" + date.get(Calendar.MONTH));
				writeEscaped(writer, "" + date.get(Calendar.DAY_OF_MONTH));
				for (Contact c: m.getContacts()) {
					writeEscaped(writer, c.getName());
				}
				writer.newLine();
			}			
			writer.newLine();
			writer.close();
			file.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Read a ContactManager from disk.
	 *
	 * This method must be executed when the program starts.
	 * 
	 * Guest matching may fail if some contact's name is contained
	 * within another, since the only way we have to get contacts from the
	 * manager is by id and name, but we don't know the id.
	 * 
	 * @return a ContactManager
	 */
	public static ContactManager read() {
		ContactManager mgr = new ContactManagerImpl();
		try {
			FileReader file = new FileReader(SAVEFILENAME);
			BufferedReader reader = new BufferedReader(file);
			String line = readUnescaped(reader);
			while (!line.isEmpty()) {
				String name = line;
				String notes = readUnescaped(reader);
				mgr.addNewContact(name, notes);				
				line = readUnescaped(reader);
			}
			line = readUnescaped(reader);
			while (!line.isEmpty()) {
				int year = Integer.parseInt(line);
				int month = Integer.parseInt(readUnescaped(reader));
				int day = Integer.parseInt(readUnescaped(reader));
				Calendar date = new GregorianCalendar(year, month, day);
				line = readUnescaped(reader);
				Set<Contact> guests = new HashSet<Contact>();
				while (!line.isEmpty()) {
					guests.add(mgr.getContacts(line).iterator().next());
					line = readUnescaped(reader);
				}
				String notes = readUnescaped(reader);
				mgr.addNewPastMeeting(guests, date, notes);
				line = readUnescaped(reader);
			}
			line = readUnescaped(reader);
			while (!line.isEmpty()) {
				int year = Integer.parseInt(line);
				int month = Integer.parseInt(readUnescaped(reader));
				int day = Integer.parseInt(readUnescaped(reader));
				Calendar date = new GregorianCalendar(year, month, day);
				line = readUnescaped(reader);
				Set<Contact> guests = new HashSet<Contact>();
				while (!line.isEmpty()) {
					guests.add(mgr.getContacts(line).iterator().next());
					line = readUnescaped(reader);
				}
				mgr.addFutureMeeting(guests, date);
				line = readUnescaped(reader);
			}
			reader.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println(SAVEFILENAME + ": file not found");
		} catch (IOException i) {
			i.printStackTrace();
		}
		return mgr;
	}

}
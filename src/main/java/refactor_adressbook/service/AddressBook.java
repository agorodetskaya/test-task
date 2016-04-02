package refactor_adressbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refactor_adressbook.dao.ContactDao;
import refactor_adressbook.model.Contact;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AddressBook {

    @Autowired
    @Qualifier(value = "contactRepository")
    private ContactDao contactDao;

    /**
     * Check the availability of the contact phone number.
     *
     * @param contactName name of contact
     * @return {@code true} if contact exist
     */
    @Transactional(readOnly = true)
    public boolean hasContactPhoneNumber(String contactName) {
        Contact contact = contactDao.findContactByName(contactName);
        return contact != null && contact.getPhoneNumber() != null;
    }

    /**
     * Check the availability of the contact phone number with specified code.
     *
     * @param contactName     name of contact
     * @param phoneNumberCode code of phone number
     * @return {@code true} if contact exist
     */
    @Transactional(readOnly = true)
    public boolean hasContactPhoneNumberWithCode(String contactName, String phoneNumberCode) {
        Contact contact = contactDao.findContactByName(contactName);
        return contact != null && (contact.getPhoneNumber() != null && contact.getPhoneNumber()
                .startsWith(phoneNumberCode));
    }

    /**
     * Get contact phone number by contact name.
     *
     * @param contactName name of contact
     * @return phone number or null if a contact with {@param contactName} does not exist
     */
    @Transactional(readOnly = true)
    public String getPhoneNumber(String contactName) {
        Contact contact = contactDao.findContactByName(contactName);
        return contact != null ? contact.getPhoneNumber() : null;
    }

    /**
     * Get amount of contacts.
     *
     * @return amount of contacts
     */
    @Transactional(readOnly = true)
    public int getContactsAmount() {
        return contactDao.getContactsAmount();
    }

    /**
     * Get all names truncated to the given length.
     *
     * @param nameLength max length of name
     * @return list of all names truncated to the given {@param nameLength}
     */
    @Transactional(readOnly = true)
    public List<String> getNamesTtruncatedLenght(int nameLength) {
        if (nameLength <= 0) {
            throw new IllegalArgumentException("name length must be greater than 0");
        }
        List<Contact> contacts = contactDao.findAll();
        return contacts.stream()
                .map(contact -> contact.getName().substring(0, nameLength))
                .collect(Collectors.toList());
    }

    /**
     * Get all contacts that have a phone number.
     *
     * @return list of all contacts that have a phone number
     */
    @Transactional(readOnly = true)
    public List<Contact> getAllContactsWithPhoneNumber() {
        return contactDao.findAll()
                .stream()
                .filter(contact -> contact.getPhoneNumber() != null)
                .collect(Collectors.toList());
    }

    /**
     * Get all contacts that have a phone number with specified code.
     *
     * @param phoneNumberCode code of phone number
     * @return list of all contacts that have a phone number with specified code
     */
    @Transactional(readOnly = true)
    public List<Contact> getAllContactsWithPhoneNumber(String phoneNumberCode) {
        return contactDao.findAll()
                .stream()
                .filter(contact -> contact.getPhoneNumber() != null && contact.getPhoneNumber()
                        .startsWith(phoneNumberCode))
                .collect(Collectors.toList());
    }

    /**
     * Get all contacts.
     *
     * @return list of all contacts
     */
    @Transactional(readOnly = true)
    public List<Contact> getAllContacts() {
        return contactDao.findAll();
    }

    /**
     * Save new contact. {@code name} cann't be null or empty. {@code phoneNumber} can be null or should contain only numbers.
     * Before save {@param contact} check {@code name} with checkContactNameUnique(String contactName).
     *
     * @param contact a new contact to be saved in the database
     * @return saved to database {@param contact} with generated {@code id}
     * @throws IllegalArgumentException if:
     *                                  1. {@param contact} null;
     *                                  2. {@code name} of {@param contact} already exist;
     *                                  3. {@code phoneNumber}of {@param contact} does not contain only numbers
     */
    @Transactional
    public Contact saveContact(Contact contact) throws IllegalArgumentException {
        String name = contact.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name of contact cann't be empty");
        }
        if (contactDao.findContactByName(name) != null) {
            throw new IllegalArgumentException("Contact with specified name already exist");
        }
        String phoneNumber = contact.getPhoneNumber();
        if (phoneNumber != null) {
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(phoneNumber);
            if (!m.matches()) {
                throw new IllegalArgumentException("Phone number should contain only digits");
            }
        }
        if (contact.getDateOfCreation() == null) {
            contact.setDateOfCreation(new Date());
        }
        return contactDao.save(contact);
    }

    /**
     * Check if there are no contacts with given name
     *
     * @param contactName of contact witch need to check
     * @return {@code true} if {@param contactName} is unique
     */
    @Transactional(readOnly = true)
    public boolean checkContactNameUnique(String contactName) {
        return contactDao.findContactByName(contactName) == null;
    }
}

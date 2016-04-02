package refactor_adressbook.dao;

import refactor_adressbook.model.Contact;

import java.util.List;

public interface ContactDao {
    /**
     * Save contact to database. Database will generate {@code id} value.
     *
     * @param contact a new contact to be saved in the database
     * @return saved to database {@param contact} with generated {@code id}
     */
    Contact save(Contact contact);

    /**
     * Find contact in database by {@param name}.
     *
     * @param name contact name by which need to find
     * @return found contact or null if a contact with {@param name} does not exist
     */
    Contact findContactByName(String name);

    /**
     * Find all contacts from database.
     *
     * @return list of all contacts persisted in database
     */
    List<Contact> findAll();

    /**
     * Find the amount of contacts in the database.
     *
     * @return amount of contacts in the database
     */
    int getContactsAmount();
}

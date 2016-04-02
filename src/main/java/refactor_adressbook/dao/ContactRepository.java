package refactor_adressbook.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refactor_adressbook.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>, ContactDao {
    @Override
    @Query("SELECT count(c) as row FROM Contact c")
    int getContactsAmount();

    Contact findContactByName(String name);
}

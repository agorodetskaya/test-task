package refactor_adressbook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import refactor_adressbook.model.Contact;
import refactor_adressbook.service.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("contactDaoJdbc")
public class ContactDaoJDBC implements ContactDao {

    @Autowired
    private ConnectionFactory connectionFactory;


    @Override
    public Contact save(Contact contact) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO contact VALUES (?, ?, ?, ?)")) {
            statement.setLong(1, contact.getId());
            statement.setString(2, contact.getName());
            statement.setString(3, contact.getPhoneNumber());
            statement.setDate(4, new Date(contact.getDateOfCreation().getTime()));
            statement.executeUpdate();
            contact.setId(statement.getGeneratedKeys().getLong(1));
            return contact;
        } catch (SQLException e) {
            throw new JdbcDaoRuntimeExeption(e);
        }
    }

    @Override
    public Contact findContactByName(String name) {
        Contact contact = null;
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                contact = createContactFromResultSet(resultSet);
            }
            return contact;
        } catch (SQLException e) {
            throw new JdbcDaoRuntimeExeption(e);
        }
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                contacts.add(createContactFromResultSet(resultSet));
            }
            return contacts;
        } catch (SQLException e) {
            throw new JdbcDaoRuntimeExeption(e);
        }
    }

    @Override
    public int getContactsAmount() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact")) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException e) {
            throw new JdbcDaoRuntimeExeption(e);
        }
    }

    private Contact createContactFromResultSet(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setId(resultSet.getLong(1));
        contact.setName(resultSet.getString(2));
        contact.setPhoneNumber(resultSet.getString(3));
        contact.setDateOfCreation(new Date(resultSet.getDate(4).getTime()));
        return contact;
    }
}

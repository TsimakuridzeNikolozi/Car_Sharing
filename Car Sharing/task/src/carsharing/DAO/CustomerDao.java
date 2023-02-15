package carsharing.DAO;

import carsharing.DAOModels.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    public List<Customer> getAllCustomers();
    public Optional<Customer> getCustomerByName(String name);
    public void createCustomer(Customer customer);
    public void updateCustomer(Customer customer);
}

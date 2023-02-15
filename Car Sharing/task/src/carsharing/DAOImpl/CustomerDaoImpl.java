package carsharing.DAOImpl;

import carsharing.DAO.CustomerDao;
import carsharing.DAOModels.Customer;
import carsharing.H2jdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements CustomerDao {
    private static final Connection connection = H2jdbcUtils.getConnection();

    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String BY_CAR = " WHERE RENTED_CAR_ID =?";
    private static final String BY_NAME= " WHERE NAME =?";
    private static final String INSERT_CUSTOMERS_SQL = "INSERT INTO CUSTOMER" +
            " (NAME, RENTED_CAR_ID) VALUES" +
            " (?, ?);";

    private static final String UPDATE_USERS_SQL =
            "UPDATE CUSTOMER SET NAME = ?, RENTED_CAR_ID = ? WHERE ID = ?;";

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery(SELECT_ALL);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int car_id = rs.getInt("RENTED_CAR_ID");
                Optional<Integer> rented_car_id = car_id != 0 ? Optional.of(car_id) : Optional.empty();
                customers.add(new Customer(id, name, rented_car_id));
            }
        } catch (Exception e) {
            System.out.println("Error while getting all customers");
        }
        return customers;
    }

    @Override
    public Optional<Customer> getCustomerByName(String name) {
        Optional<Customer> customer = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL + BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String receivedName = rs.getString("NAME");
                int car_id = rs.getInt("RENTED_CAR_ID");
                Optional<Integer> rented_car_id = car_id != 0 ? Optional.of(car_id) : Optional.empty();
                customer = Optional.of(new Customer(id, receivedName, rented_car_id));
            }
        } catch (Exception e) {
            System.out.println("Error while getting customer by name");
        }
        return customer;
    }

    @Override
    public void createCustomer(Customer customer) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_CUSTOMERS_SQL)) {
            ps.setString(1, customer.getName());
            ps.setNull(2, Types.INTEGER);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error while creating a customer");
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_USERS_SQL)) {
            ps.setString(1, customer.getName());
            if (customer.getRentedCarId().isEmpty()) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, customer.getRentedCarId().get());
            }
            ps.setInt(3, customer.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error while updating a customer");
        }
    }
}

package carsharing.DAOImpl;

import carsharing.DAO.CarDao;
import carsharing.DAOModels.Car;
import carsharing.DAOModels.Company;
import carsharing.H2jdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDaoImpl implements CarDao {

    private static final Connection connection = H2jdbcUtils.getConnection();

    private static final String SELECT_ALL = "SELECT * FROM CAR";
    private static final String BY_NAME = " WHERE NAME =?";
    private static final String BY_ID = " WHERE ID =?";
    private static final String BY_COMPANY_ID = " WHERE COMPANY_ID =?";
    private static final String INSERT = "INSERT INTO CAR" +
            " (NAME, COMPANY_ID) VALUES" +
            " (?, ?);";
    private static final String SELECT_NOT_RENTED = "SELECT CAR.ID, CAR.NAME, CAR.COMPANY_ID" +
            " FROM CAR LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID" +
            " WHERE COMPANY_ID = ? AND CUSTOMER.ID IS NULL";


    @Override
    public List<Car> getAllNotRented(Company company) {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_NOT_RENTED)) {
            ps.setInt(1, company.getID());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int company_id = rs.getInt("company_id");
                cars.add(new Car(id, name, company_id));
            }
        } catch (Exception e) {
            System.out.println("Error while getting all not rented cars");
        }
        return cars;
    }

    @Override
    public Optional<Car> getCarByID(long ID) {
        Optional<Car> car = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL + BY_ID)) {
            ps.setLong(1, ID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int companyID = rs.getInt("COMPANY_ID");
                car = Optional.of(new Car(id, name, companyID));
            }

        } catch (Exception e) {
            System.out.println("Error while getting cars by ID");
        }
        return car;
    }

    @Override
    public List<Car> getCarsByCompanyID(int companyID) {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL + BY_COMPANY_ID)) {
            ps.setInt(1, companyID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name1 = rs.getString("NAME");
                int companyID1 = rs.getInt("COMPANY_ID");
                cars.add(new Car(id, name1, companyID1));
            }

        } catch (Exception e) {
            System.out.println("Error while getting cars by company_id");
        }
        return cars;
    }

    @Override
    public void createCar(Car car) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT)) {
            ps.setString(1, car.getName());
            ps.setInt(2, car.getCompanyID());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating a new car");
        }
    }
}

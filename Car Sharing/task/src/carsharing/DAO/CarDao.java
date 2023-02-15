package carsharing.DAO;

import carsharing.DAOModels.Car;
import carsharing.DAOModels.Company;

import java.util.List;
import java.util.Optional;

public interface CarDao {
    public List<Car> getAllNotRented(Company company);
    public Optional<Car> getCarByID(long ID);
    public List<Car> getCarsByCompanyID(int companyID);
    public void createCar(Car car);
}

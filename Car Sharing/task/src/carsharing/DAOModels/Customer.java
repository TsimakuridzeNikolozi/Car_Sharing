package carsharing.DAOModels;

import java.util.Optional;

public class Customer {
    private int id;
    private String name;
    private Optional<Integer> rentedCarId;

    public Customer(int id, String name, Optional<Integer> rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Integer> getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Optional<Integer> rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}

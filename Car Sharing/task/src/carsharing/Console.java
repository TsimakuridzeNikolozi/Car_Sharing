package carsharing;

import carsharing.DAOImpl.CarDaoImpl;
import carsharing.DAOImpl.CompanyDaoImpl;
import carsharing.DAOImpl.CustomerDaoImpl;
import carsharing.DAOModels.Car;
import carsharing.DAOModels.Company;
import carsharing.DAOModels.Customer;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Console {
    private static final Scanner sc = new Scanner(System.in);

    private static final CompanyDaoImpl companies = new CompanyDaoImpl();
    private static final CarDaoImpl cars = new CarDaoImpl();
    private static final CustomerDaoImpl customers = new CustomerDaoImpl();

    public static void printGeneralOptions() {
        loop:
        while(true) {
            System.out.println("1. Log in as a manager\n" +
                               "2. Log in as a customer\n" +
                               "3. Create a customer\n" +
                               "0. Exit");
            int command = sc.nextInt();
            switch(command) {
                case 1: printManagerOptions(); break;
                case 2: logInAsCustomer(); break;
                case 3: createCustomer(); break;
                case 0: break loop;
                default: System.out.println("Wrong input");
            }
            System.out.println();
        }
    }

    private static void printManagerOptions() {
        loop:
        while(true) {
            System.out.println("\n1. Company list\n" + "2. Create a company\n" + "0. Back");
            int command = sc.nextInt();
            switch(command) {
                case 1: printCompanies(); break;
                case 2: createACompany(); break;
                case 0: break loop;
                default: System.out.println("Wrong input");
            }
        }
    }

    private static void printCompanies() {
        List<Company> companyList = companies.getAllCompanies();
        if (companyList.isEmpty()) {
            System.out.println("\nThe company list is empty!");
            return;
        }

        while(true) {
            System.out.println("\nChoose a company:");
            for(int i = 0; i < companyList.size(); i++) {
                System.out.println((i + 1) + ". " + companyList.get(i).getName());
            }
            System.out.println("0. Back");

            int command = sc.nextInt();
            if (command < 0 || command > companyList.size())
                System.out.println("Wrong command");
            else if (command == 0)
                break;
            else {
                printCompanyOptions(companyList.get(command - 1));
                break;
            }
        }
    }

    private static void createACompany() {
        System.out.println("\nEnter the company name:");
        sc.nextLine();
        String name = sc.nextLine();
        companies.createCompany(new Company(0, name));
        System.out.println("The company was created!");
    }

    private static void printCompanyOptions(Company company) {
        System.out.print("\n'" + company.getName() + "' company");
        loop:
        while(true) {
            System.out.println("\n1. Car list\n" + "2. Create a car\n" + "0. Back");
            int command = sc.nextInt();

            switch(command) {
                case 1: printCompanyCars(company); break;
                case 2: createACar(company); break;
                case 0: break loop;
                default: System.out.println("Wrong input");
            }
        }
    }

    private static void printCompanyCars(Company company) {
        System.out.println("\nCar list:");
        List<Car> companyCars = cars.getCarsByCompanyID(company.getID());
        if (companyCars.isEmpty()) System.out.println("The car list is empty!");
        else {
            for (int i = 0; i < companyCars.size(); i++) {
                System.out.println((i + 1) + ". " + companyCars.get(i).getName());
            }
        }
    }

    private static void createACar(Company company) {
        System.out.println("\nEnter the car name:");
        sc.nextLine();
        String name = sc.nextLine();
        cars.createCar(new Car(0, name, company.getID()));
        System.out.println("The car was added!");
    }

    private static void logInAsCustomer() {
        List<Customer> customerList = customers.getAllCustomers();
        if (customerList.isEmpty()) {
            System.out.println("\nThe customer list is empty!");
            return;
        }

        while(true) {
            System.out.println("\nCustomer list:");
            for(int i = 0; i < customerList.size(); i++) {
                System.out.println((i + 1) + ". " + customerList.get(i).getName());
            }

            int command = sc.nextInt();
            if (command < 0 || command > customerList.size()){
                System.out.println("Wrong command");
            } else if (command == 0) {
                break;
            } else {
                printCustomerOptions(customerList.get(command - 1));
                break;
            }
        }
    }

    private static void printCustomerOptions(Customer customer) {
        loop:
        while(true) {
            System.out.println("\n1. Rent a car\n" +
                                "2. Return a rented car\n" +
                                "3. My rented car\n" +
                                "0. Back");

            int command = sc.nextInt();
            switch(command) {
                case 1: rentACarForCustomer(customer); break;
                case 2: returnARentedCarForCustomer(customer); break;
                case 3: printRentedCarForCustomer(customer); break;
                case 0: break loop;
                default: System.out.println("Wrong command");
            }
        }
    }

    private static void rentACarForCustomer(Customer customer) {
        if (customer.getRentedCarId().isPresent()) {
            System.out.println("\nYou've already rented a car!");
            return;
        }

        while(true) {
            Company company = chooseCompanyForCustomer();
            if (company == null) return;
            Car car = chooseCarForCustomer(company);
            if (car == null) continue;

            customer.setRentedCarId(Optional.of(car.getID()));
            customers.updateCustomer(customer);
            System.out.println("\nYou rented '" + car.getName() + "'");
            return;
        }
    }

    private static Company chooseCompanyForCustomer() {
        List<Company> companyList = companies.getAllCompanies();
        if (companyList.isEmpty()) {
            System.out.println("\nThe company list is empty!");
            return null;
        }

        while(true) {
            System.out.println("\nChoose a company:");
            for(int i = 0; i < companyList.size(); i++) {
                System.out.println((i + 1) + ". " + companyList.get(i).getName());
            }
            System.out.println("0. Back");

            int command = sc.nextInt();
            if (command < 0 || command > companyList.size())
                System.out.println("Wrong command");
            else if (command == 0)
                return null;
            else {
                return companyList.get(command - 1);
            }
        }
    }

    private static Car chooseCarForCustomer(Company company) {
        while(true) {
            System.out.println("\nChoose a car:");
            List<Car> companyCars = cars.getAllNotRented(company);
            if (companyCars.isEmpty()) {
                System.out.println("The car list is empty!");
                return null;
            }

            for (int i = 0; i < companyCars.size(); i++) {
                System.out.println((i + 1) + ". " + companyCars.get(i).getName());
            }

            int command = sc.nextInt();
            if (command < 0 || command > companyCars.size())
                System.out.println("Wrong command");
            else if (command == 0)
                return null;
            else
                return companyCars.get(command - 1);
        }
    }

    private static void returnARentedCarForCustomer(Customer customer) {
        if (customer.getRentedCarId().isEmpty()) {
            System.out.println("\nYou didn't rent a car!");
            return;
        }

        customer.setRentedCarId(Optional.empty());
        customers.updateCustomer(customer);
        System.out.println("\nYou've returned a rented car!");
    }

    private static void printRentedCarForCustomer(Customer customer) {
        if (customer.getRentedCarId().isEmpty()) {
            System.out.println("\nYou didn't rent a car!");
            return;
        }

        Optional<Car> customerRentedCar = cars.getCarByID(customer.getRentedCarId().get());
        System.out.println("\nYour rented car:\n" + customerRentedCar.get().getName() +
                "\nCompany:\n" +
                (companies.getCompanyByID(customerRentedCar.get().getCompanyID())).get().getName());
    }

    private static void createCustomer() {
        System.out.println("\nEnter the customer name:");
        sc.nextLine();
        String name = sc.nextLine();
        customers.createCustomer(new Customer(0, name, Optional.empty()));
        System.out.println("The customer was added!");
    }
}
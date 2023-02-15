package carsharing.DAOImpl;

import carsharing.DAO.CompanyDao;
import carsharing.H2jdbcUtils;
import carsharing.DAOModels.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyDaoImpl implements CompanyDao {
    private List<Company> companies;

    private static final Connection connection = H2jdbcUtils.getConnection();

    private static final String SELECT_QUERY = "SELECT * FROM COMPANY";
    private static final String BY_NAME = " WHERE NAME =?";
    private static final String BY_ID = " WHERE ID =?";
    private static final String INSERT = "INSERT INTO COMPANY" +
                                                        " (NAME) VALUES" +
                                                        " (?);";

    public CompanyDaoImpl() {
        this.companies = new ArrayList<>();
    }

    @Override
    public List<Company> getAllCompanies() {
        try {
            ResultSet rs = connection.createStatement().executeQuery(SELECT_QUERY);
            companies.clear();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                companies.add(new Company(id, name));
            }
        } catch (Exception e) {
            System.out.println("Error while getting all companies");
        }
        return companies;
    }

    @Override
    public Optional<Company> getCompanyByID(long ID) {
        Optional<Company> company = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_ID)) {
            ps.setLong(1, ID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                company = Optional.of(new Company(id, name));
            }

        } catch (Exception e) {
            System.out.println("Error while getting companies by ID");
        }
        return company;
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        Optional<Company> company = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name1 = rs.getString("NAME");
                company = Optional.of(new Company(id, name1));
            }

        } catch (Exception e) {
            System.out.println("Error while getting companies by name");
        }
        return company;
    }

    @Override
    public void createCompany(Company company) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT)) {
            ps.setString(1, company.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating a new company");
        }
    }
}

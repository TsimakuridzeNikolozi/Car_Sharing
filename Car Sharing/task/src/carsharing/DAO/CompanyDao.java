package carsharing.DAO;

import carsharing.DAOModels.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDao {
    public List<Company> getAllCompanies();
    public Optional<Company> getCompanyByID(long ID);
    public Optional<Company> getCompanyByName(String name);
    public void createCompany(Company company);
}

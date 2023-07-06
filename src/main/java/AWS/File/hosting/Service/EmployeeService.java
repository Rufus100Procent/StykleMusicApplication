package AWS.File.hosting.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class EmployeeService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertEmployee(String name, String designation) {
        String sql = "INSERT INTO employees (name, designation) VALUES (:name, :designation)";
        entityManager.createNativeQuery(sql)
                .setParameter("name", name)
                .setParameter("designation", designation)
                .executeUpdate();
    }
}
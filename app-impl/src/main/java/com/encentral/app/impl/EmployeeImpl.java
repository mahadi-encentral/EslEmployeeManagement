package com.encentral.app.impl;

import com.mamt4real.app.api.IEmployee;
import com.mamt4real.app.model.Employee;
import com.mamt4real.app.model.EmployeeMapper;
import com.mamt4real.entities.JpaEmployee;
import com.google.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EmployeeImpl implements IEmployee {
    private final JPAApi jpaApi;

    @Inject
    public EmployeeImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        JpaEmployee jpaEmployee = EmployeeMapper.employeeToJpaEmployee(employee);
        jpaEmployee.setToken(UUID.randomUUID().toString());
        jpaApi.em().persist(jpaEmployee);

        return EmployeeMapper.jpaEmployeeToEmployee(jpaEmployee);
    }

    @Override
    public Optional<Employee> getEmployee(String email) {
        try {
            JpaEmployee jpaEmployee = jpaApi.withTransaction(em ->
                    em.createQuery("Select e From JpaEmployee e where e.email = :email", JpaEmployee.class)
                            .setParameter("email", email)
                            .getSingleResult());

            return Optional.ofNullable(jpaEmployee).map(EmployeeMapper::jpaEmployeeToEmployee);
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    @Override
    public Optional<Employee> getEmployee(int id) {
        try {
            JpaEmployee jpaEmployee = jpaApi.withTransaction(em ->
                    em.createQuery("Select e From JpaEmployee e where e.id = :id", JpaEmployee.class)
                            .setParameter("id", id)
                            .getSingleResult());

            return Optional.ofNullable(jpaEmployee).map(EmployeeMapper::jpaEmployeeToEmployee);
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    @Override
    public Optional<Employee> getEmployeeByToken(String token) {
        try {
            JpaEmployee jpaEmployee = jpaApi.withTransaction(em ->
                    em.createQuery("Select e From JpaEmployee e where e.token = :token", JpaEmployee.class)
                            .setParameter("token", token)
                            .getSingleResult());

            return Optional.ofNullable(jpaEmployee).map(EmployeeMapper::jpaEmployeeToEmployee);
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    @Override
    public Optional<List<Employee>> getEmployees() {
        try {
            List<JpaEmployee> jpaEmployees = jpaApi.withTransaction(em ->
                    em.createQuery("Select e From JpaEmployee e", JpaEmployee.class)
                            .getResultList());

            return Optional.ofNullable(jpaEmployees).map(EmployeeMapper::jpaEmployeesToEmployees);
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    @Override
    public boolean removeEmployee(Employee employee) {
        try {
            int rows = jpaApi.withTransaction(em ->
                    em.createQuery("Delete From JpaEmployee where id = :id", JpaEmployee.class)
                            .setParameter("id", employee.getId())
                            .executeUpdate());

            return rows == 1;
        } catch (Exception ignored) {}

        return false;
    }

    @Override
    public boolean changePassword(Employee employee, String newPassword) {
        try {
            int rows = jpaApi.withTransaction(em ->
                    em.createQuery("Update JpaEmployee Set password = :newPassword where id = :id", JpaEmployee.class)
                            .setParameter("id", employee.getId())
                            .setParameter("newPassword", newPassword)
                            .executeUpdate());

            return rows == 1;
        } catch (Exception ignored) {}

        return false;
    }
}

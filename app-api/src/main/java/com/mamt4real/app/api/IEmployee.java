package com.mamt4real.app.api;

import com.mamt4real.app.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployee {
    Employee addEmployee(Employee employee);

    Optional<Employee> getEmployee(String email);

    Optional<Employee> getEmployee(int id);

    Optional<Employee> getEmployeeByToken(String token);

    Optional<List<Employee>> getEmployees();

    boolean removeEmployee(Employee employee);

    boolean changePassword(Employee employee, String newPassword);
}

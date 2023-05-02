package com.mamt4real.app.model;

import com.mamt4real.entities.JpaEmployee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {
    public static JpaEmployee employeeToJpaEmployee(Employee employee) {
        JpaEmployee jpaEmployee = new JpaEmployee();
        jpaEmployee.setId(employee.getId());
        jpaEmployee.setPassword(employee.getPassword());
        jpaEmployee.setEmail(employee.getEmail());
        jpaEmployee.setToken(employee.getToken());
        return jpaEmployee;
    }

    public static Employee jpaEmployeeToEmployee(JpaEmployee jpaEmployee) {
        Employee employee = new Employee();
        employee.setEmail(jpaEmployee.getEmail());
        employee.setPassword(jpaEmployee.getPassword());
        employee.setId(jpaEmployee.getId());
        employee.setToken(jpaEmployee.getToken());
        return employee;
    }

    public static List<Employee> jpaEmployeesToEmployees(List<JpaEmployee> jpaEmployees) {
        List<Employee> employees = new ArrayList<>();
        for (JpaEmployee jpaEmployee : jpaEmployees)
            employees.add(jpaEmployeeToEmployee(jpaEmployee));

        return employees;
    }
}

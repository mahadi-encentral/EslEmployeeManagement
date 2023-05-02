package com.mamt4real.app.model;

import com.mamt4real.entities.JpaAttendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceMapper {
    public static Attendance jpaAttendanceToAttendance(JpaAttendance jpaAttendance) {
        Attendance attendance = new Attendance();
        attendance.setId(jpaAttendance.getId());
        attendance.setDate(jpaAttendance.getDate());
        attendance.setEmployee(EmployeeMapper.jpaEmployeeToEmployee(jpaAttendance.getEmployee()));

        return attendance;
    }

    public static JpaAttendance attendanceToJpaAttendance(Attendance attendance) {
        JpaAttendance jpaAttendance = new JpaAttendance();
        jpaAttendance.setId(attendance.getId());
        jpaAttendance.setDate(attendance.getDate());
        jpaAttendance.setEmployee(EmployeeMapper.employeeToJpaEmployee(attendance.getEmployee()));

        return jpaAttendance;
    }

    public static List<Attendance> jpaAttendancesToAttendances(List<JpaAttendance> jpaAttendances) {
        List<Attendance> attendances = new ArrayList<>();
        for (JpaAttendance jpaAttendance : jpaAttendances)
            attendances.add(jpaAttendanceToAttendance(jpaAttendance));

        return attendances;
    }
}

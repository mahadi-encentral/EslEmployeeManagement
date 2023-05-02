package com.mamt4real.app.api;

import com.mamt4real.app.model.Attendance;
import com.mamt4real.app.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IAttendance {
    Attendance addAttendance(Attendance attendance);

    Optional<List<Attendance>> getAttendance(Employee employee);
}

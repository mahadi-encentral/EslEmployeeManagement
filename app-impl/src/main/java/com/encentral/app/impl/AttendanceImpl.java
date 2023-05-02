package com.encentral.app.impl;

import com.mamt4real.app.api.IAttendance;
import com.mamt4real.app.model.Attendance;
import com.mamt4real.app.model.AttendanceMapper;
import com.mamt4real.app.model.Employee;
import com.mamt4real.entities.JpaAttendance;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class AttendanceImpl implements IAttendance {
    private final JPAApi jpaApi;

    @Inject
    public AttendanceImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public Attendance addAttendance(Attendance attendance) {
        JpaAttendance jpaAttendance = AttendanceMapper.attendanceToJpaAttendance(attendance);
        jpaApi.em().persist(jpaAttendance);

        return AttendanceMapper.jpaAttendanceToAttendance(jpaAttendance);
    }

    @Override
    public Optional<List<Attendance>> getAttendance(Employee employee) {
        try {
            List<JpaAttendance> jpaAttendance = jpaApi.withTransaction(em ->
                    em.createQuery("Select a From JpaAttendance a where a.employee_id = :employeeId", JpaAttendance.class)
                            .setParameter("employeeId", employee.getId())
                            .getResultList());
            return Optional.ofNullable(jpaAttendance).map(AttendanceMapper::jpaAttendancesToAttendances);
        } catch (Exception ignored) {}

        return Optional.empty();
    }
}

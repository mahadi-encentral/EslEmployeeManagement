package com.mamt4real.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "attendance", schema = "public")
@Entity
public class JpaAttendance implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private JpaEmployee employee;

    public JpaEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(JpaEmployee employee) {
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
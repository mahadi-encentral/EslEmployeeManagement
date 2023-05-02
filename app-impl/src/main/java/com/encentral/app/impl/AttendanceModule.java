package com.encentral.app.impl;

import com.mamt4real.app.api.IAttendance;
import com.google.inject.AbstractModule;

public class AttendanceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IAttendance.class).to(AttendanceImpl.class);
    }
}

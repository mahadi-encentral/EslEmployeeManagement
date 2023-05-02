package com.encentral.app.impl;

import com.mamt4real.app.api.IEmployee;
import com.google.inject.AbstractModule;

public class EmployeeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IEmployee.class).to(EmployeeImpl.class);
    }
}

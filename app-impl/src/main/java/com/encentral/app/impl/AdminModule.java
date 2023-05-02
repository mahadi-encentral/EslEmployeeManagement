package com.encentral.app.impl;

import com.mamt4real.app.api.IAdmin;
import com.google.inject.AbstractModule;

public class AdminModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IAdmin.class).to(AdminImpl.class);
    }
}

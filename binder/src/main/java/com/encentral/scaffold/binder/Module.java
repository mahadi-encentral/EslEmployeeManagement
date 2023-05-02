/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encentral.scaffold.binder;

import com.encentral.app.impl.AdminModule;
import com.encentral.app.impl.AttendanceModule;
import com.encentral.app.impl.EmployeeModule;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;


/**
 * @author poseidon
 */
public class Module extends AbstractModule implements AkkaGuiceSupport {


    @Override
    protected void configure() {

        super.configure();

        bind(BigBang.class).asEagerSingleton();
        install(new AdminModule());
        install(new EmployeeModule());
        install(new AttendanceModule());


    }
}

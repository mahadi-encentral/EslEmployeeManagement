package com.mamt4real.app.api;

import com.mamt4real.app.model.Admin;

import java.util.Optional;

public interface IAdmin {
    Admin addAdmin(Admin admin);

    Optional<Admin> getAdmin(String email);

    Optional<Admin> getAdminByToken(String token);
}

package com.encentral.app.impl;

import com.mamt4real.app.api.IAdmin;
import com.mamt4real.app.model.Admin;
import com.mamt4real.app.model.AdminMapper;
import com.mamt4real.entities.JpaAdmin;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

public class AdminImpl implements IAdmin {
    private final JPAApi jpaApi;

    @Inject
    public AdminImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public Admin addAdmin(Admin admin) {
        JpaAdmin jpaAdmin = AdminMapper.adminToJpaAdmin(admin);
        jpaAdmin.setToken(UUID.randomUUID().toString());
        jpaApi.em().persist(jpaAdmin);

        return AdminMapper.jpaAdminToAdmin(jpaAdmin);
    }

    @Override
    public Optional<Admin> getAdmin(String email) {
        try {
            JpaAdmin jpaAdmin = jpaApi.withTransaction(em ->
                    em.createQuery("Select a From JpaAdmin a where a.email = :email", JpaAdmin.class)
                            .setParameter("email", email)
                            .getSingleResult());

            return Optional.ofNullable(jpaAdmin).map(AdminMapper::jpaAdminToAdmin);
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    @Override
    public Optional<Admin> getAdminByToken(String token) {
        try {
            JpaAdmin jpaAdmin = jpaApi.withTransaction(em ->
                    em.createQuery("Select a From JpaAdmin a where a.token = :token", JpaAdmin.class)
                            .setParameter("token", token)
                            .getSingleResult());

            return Optional.ofNullable(jpaAdmin).map(AdminMapper::jpaAdminToAdmin);
        } catch (Exception ignored) {}

        return Optional.empty();
    }
}

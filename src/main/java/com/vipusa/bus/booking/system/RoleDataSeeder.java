package com.vipusa.bus.booking.system;

import com.vipusa.bus.booking.system.defaults.USER_ROLE;
import com.vipusa.bus.booking.system.entity.Role;
import com.vipusa.bus.booking.system.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleDataSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @EventListener
    @Transactional
    public void LoadRoles(ContextRefreshedEvent event) {

        List<USER_ROLE> roles = Arrays.stream(USER_ROLE.values()).toList();

        for (USER_ROLE role : roles) {
            if (roleRepository.findByName(role.name()) == null) {
                roleRepository.save(new Role(role.name()));
            }
        }

    }
}

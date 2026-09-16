package com.vipusa.bus.booking.system;

import com.vipusa.bus.booking.system.defaults.USER_ROLE;
import com.vipusa.bus.booking.system.entity.Role;
import com.vipusa.bus.booking.system.exception.RoleNotFoundException;
import com.vipusa.bus.booking.system.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

    @Autowired
    private RoleRepository roleRepository;

    public Role getInstance(String role) throws RoleNotFoundException {
        String upperRole = role.toUpperCase();
        try {
            USER_ROLE.valueOf(upperRole); // validate
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundException("No role found for " + role);
        }
        return roleRepository.findByName(upperRole);
    }
}

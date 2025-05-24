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

    public Role getInstance(String role) throws RoleNotFoundException{
        switch (role){
            case "admin" -> {
                return roleRepository.findByRoleName(USER_ROLE.ADMIN);
            }
            case "user"->{
                return roleRepository.findByRoleName(USER_ROLE.USER);
            }
            default ->
                    throw  new RoleNotFoundException("No role found for " +  role);
        }
    }
}

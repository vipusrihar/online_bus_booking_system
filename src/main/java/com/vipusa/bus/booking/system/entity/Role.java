package com.vipusa.bus.booking.system.entity;

import com.vipusa.bus.booking.system.defaults.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private USER_ROLE roleName;

    public Role(USER_ROLE role){
        this.roleName=role;
    }


}

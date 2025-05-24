package com.vipusa.bus.booking.system.entity;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "Buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String busNumber;

    @Enumerated(EnumType.STRING)
    private BUS_TYPE busType;

    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    private DEPOTS ownedDEPO;

}

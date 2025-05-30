package com.vipusa.bus.booking.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Bus bus; // Many trips can use the same bus

    @ManyToOne
    private Route route; // Many trips can follow the same route

    private LocalDate startDate;
    private LocalTime startTime;

    private LocalDate endDate;
    private LocalTime endTime;

    @ElementCollection
    @MapKeyColumn(name = "seat_number")
    @Column(name = "is_booked")
    private Map<Integer, Boolean> seatStatus;
}

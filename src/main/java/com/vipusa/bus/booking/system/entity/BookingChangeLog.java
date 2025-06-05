package com.vipusa.bus.booking.system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class BookingChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Booking oldBooking;

    @OneToOne
    private Booking newBooking;

    private LocalDateTime changedAt;
}

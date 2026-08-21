package com.vipusa.bus.booking.system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "booking_change_log")
public class BookingChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_booking_id")
    private Booking oldBooking;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_booking_id")
    private Booking newBooking;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
}
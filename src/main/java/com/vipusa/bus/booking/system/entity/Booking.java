package com.vipusa.bus.booking.system.entity;

import com.vipusa.bus.booking.system.defaults.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Trip trip;

    @ElementCollection
    private List<Integer> seatNumbers;

    private LocalDate bookingDate;

    private LocalTime bookingTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}

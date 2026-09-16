package com.vipusa.bus.booking.system.response;

import com.vipusa.bus.booking.system.defaults.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {
    private Long id;
    private Long busId;
    private String busNumber;
    private Long routeId;
    private String routeNumber;
    private LocalDateTime departsAt;
    private LocalDateTime arrivesAt;
    private BigDecimal price;
    private TripStatus status;
    private Integer totalSeats;
    private Integer availableSeats;
    private List<SeatResponse> seats;
}
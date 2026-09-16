package com.vipusa.bus.booking.system.response;

import com.vipusa.bus.booking.system.defaults.BookingStatus;
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
public class BookingResponse {
    private Long id;
    private String bookingRef;
    private Long userId;
    private String userName;
    private Long tripId;
    private String tripDepartsAt;
    private String tripArrivesAt;
    private String routeStartLocation;
    private String routeEndLocation;
    private String busNumber;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<BookingSeatResponse> seats;
}
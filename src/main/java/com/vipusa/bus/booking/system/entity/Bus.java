package com.vipusa.bus.booking.system.entity;
import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bus number is required")
    @Column(name = "bus_number", nullable = false, unique = true)
    private String busNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bus_type", nullable = false)
    private BUS_TYPE busType;

    @NotNull
    @Min(value = 1, message = "Total seats must be at least 1")
    @Check(constraints = "total_seats > 0")
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_id", nullable = false)
    private Depot depot;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
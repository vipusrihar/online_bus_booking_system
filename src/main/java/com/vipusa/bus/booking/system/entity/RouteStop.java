package com.vipusa.bus.booking.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "route_stops",
        uniqueConstraints = @UniqueConstraint(name = "uk_route_stop_seq", columnNames = {"route_id", "sequence_order"}))
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @NotBlank(message = "Stop name is required")
    @Column(name = "stop_name", nullable = false)
    private String stopName;

    @NotNull
    @Min(value = 0, message = "Sequence order must be zero or greater")
    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;
}
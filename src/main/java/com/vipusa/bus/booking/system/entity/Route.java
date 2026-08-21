package com.vipusa.bus.booking.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Route number is required")
    @Column(name = "route_number", nullable = false, unique = true)
    private String routeNumber;

    @NotBlank(message = "Start location is required")
    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @NotBlank(message = "End location is required")
    @Column(name = "end_location", nullable = false)
    private String endLocation;

    @NotNull
    @Positive(message = "Distance must be positive")
    @Column(name = "distance_km")
    private Double distanceKm;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RouteStop> stops = new ArrayList<>();
}
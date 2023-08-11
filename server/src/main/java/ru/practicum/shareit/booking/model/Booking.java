package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;
    @NotNull
    @Column(name = "start_time_booking")
    private LocalDateTime start;
    @NotNull
    @Column(name = "end_time_booking")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User booker;
    @Enumerated(EnumType.STRING)
    private StatusBooking statusBooking;
}

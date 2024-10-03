package ru.practicum.shareit.model.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.model.booking.dto.BookingDto;
import ru.practicum.shareit.model.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.item.dto.ItemDtoShort;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.model.user.dto.UserDtoShort;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.model.booking.BookingStatus.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerMockTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingDtoCreate bookingDtoCreate;
    private User user;
    private Item item;
    private ItemDtoShort itemDtoShort;
    private UserDtoShort userDtoShort;
    private List<Booking> bookingList;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        user = new User(1L, "user", "user@mail.com");
        item = new Item(1L, "item", "des", true, user, 1L);
        booking = new Booking(
                1L, Instant.now(), Instant.now().plus(6, DAYS),
                item, user, WAITING);
        itemDtoShort = new ItemDtoShort(1L, "item", 1L);
        userDtoShort = new UserDtoShort(1L, "user");
        bookingDto = new BookingDto(
                1L, LocalDateTime.now(), LocalDateTime.now().plusDays(6), WAITING, itemDtoShort, userDtoShort);
        bookingDtoCreate = new BookingDtoCreate (1L, LocalDateTime.now(), LocalDateTime.now().plusDays(6));
        bookingList = List.of(booking);
    }

    @Test
    void createBookingTest() throws Exception {
        when(bookingService.add(any(), any(), any())).thenReturn(booking);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(booking))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void confirmBookingTest() throws Exception {
        when(bookingService.approve(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);
        mvc.perform(patch("/bookings/{bookingId}?approved={approved}"
                        , booking.getId(), true)
                        .content(mapper.writeValueAsString(booking.getId()))
                .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingTest() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(booking);
        mvc.perform(get("/bookings/{bookingId}", booking.getId())
                .content(mapper.writeValueAsString(bookingDto))
                .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
                );
    }

    @Test
    void getBookingsByUserTest() throws Exception {
        when(bookingService.getBookingsByUser(anyLong(), any())).thenReturn(bookingList);
        mvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", 1L)
                .param("state", "ALL"))
                .andExpect(status().isOk()
        );
    }

    @Test
    void getBookingsForOwnerItemsTest() throws Exception {
        when(bookingService.getBookingsByAllUserItems(anyLong(), any())).thenReturn(bookingList);
        mvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", 1L)
                .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}
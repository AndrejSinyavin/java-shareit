package ru.practicum.shareit.model.request;

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
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.item.dto.ItemDtoShort;
import ru.practicum.shareit.model.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.model.user.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ItemRequestControllerMockTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private User user;
    private Item item;
    ItemRequest itemRequest;
    ItemRequestDtoWithAnswer itemRequestDto;
    List<ItemDtoShort> itemDtoShortList;
    List<ItemRequestDtoWithAnswer> listDtoWithAnswer;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        user = new User(1L, "user", "user@mail.com");
        item = new Item(1L, "item", "desc", true, user, 1L);
        itemRequest = new ItemRequest(1L, "desc", user, Instant.now());
        itemDtoShortList = new ArrayList<>();
        itemRequestDto = new ItemRequestDtoWithAnswer(1L, "desc", Instant.now(), itemDtoShortList);
        listDtoWithAnswer = new ArrayList<>();
    }

    @Test
    void createItemRequestTest() throws Exception {
        when(itemRequestService.add(any(), any())).thenReturn(itemRequest);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void getItemRequestTest() throws Exception {
        when(itemRequestService.getItemRequest(any(), any())).thenReturn(itemRequestDto);
        mvc.perform(get("/requests/{id}", itemRequest.getId())
        .header("X-Sharer-User-Id", 1L)
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    void getAllOwnersRequestsTest() throws Exception {
        when(itemRequestService.getAllOwnersRequests(any())).thenReturn(listDtoWithAnswer);
        mvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
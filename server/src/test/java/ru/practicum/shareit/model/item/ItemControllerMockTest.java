package ru.practicum.shareit.model.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.model.item.dto.CommentDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.dto.ItemDtoBooking;
import ru.practicum.shareit.model.user.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Import({ItemMapper.class, ItemMapperImpl.class})
class ItemControllerMockTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemDto itemDto;
    private List<ItemDtoBooking> itemDtoBookingList;
    private List<Item> itemList;
    private Item item;
    private User user;
    private ItemDtoBooking itemDtoBooking;
    private CommentDtoCreate commentDtoCreate;
    private Comment comment;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(itemController).build();
        itemDto = new ItemDto(1L, "name", "desc", true);
        user = new User(1L, "user", "user@emal.com");
        itemDtoBooking = new ItemDtoBooking(
                1L,
                "name",
                "desc",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>()
        );
        item = new Item(1L,
                "name",
                "desc",
                true,
                user,
                1L
        );
        itemDtoBookingList = new ArrayList<>();
        itemList = new ArrayList<>();
        commentDtoCreate = new CommentDtoCreate("comment");
        comment = new Comment(1L, item, user, "comment", Instant.now());
    }

    @Test
    void addTest() throws Exception {
        when(itemService.add(any(), anyLong())).thenReturn(item);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("desc")
                );
    }

    @Test
    void updateTest() throws Exception {
        when(itemService.update(any(), anyLong(), anyLong())).thenReturn(item);
        mvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("desc")
                );
    }

    @Test
    void getTest() throws Exception {
        when(itemService.get(anyLong())).thenReturn(itemDtoBooking);
        mvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemDtoBooking.getId())
                .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("desc")
                );
    }

    @Test
    void listTest() throws Exception {
        when(itemService.getItemsByOwner(anyLong())).thenReturn(itemDtoBookingList);
        mvc.perform(get("/items").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void search() throws Exception {
        when(itemService.search(anyString())).thenReturn(itemList);
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", item.getDescription())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(1L, 1L, commentDtoCreate)).thenReturn(comment);
        mvc.perform(post("/items/{itemId}/comment", 1L)
                .content(mapper.writeValueAsString(commentDtoCreate))
                .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
}
package ru.practicum.shareit.model.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplUnitTest {
    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

    @BeforeAll
    static void setup() {

    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void get() {

    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void getItemsByOwner() {
    }

    @Test
    void search() {
    }

    @Test
    void addComment() {
    }
}
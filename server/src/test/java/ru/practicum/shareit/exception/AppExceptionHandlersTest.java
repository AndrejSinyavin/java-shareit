package ru.practicum.shareit.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Набор тестов для централизованного обработчика исключений приложения")
class AppExceptionHandlersTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Недопустимый параметр запроса - критерий поиска UNKNOWN")
    void handleBadRequestResponseIfValidatedArgumentNotValidTest() throws Exception {
        var request = get("/bookings")
                .header("X-Sharer-User-Id", 1L)
                .queryParam("state","UNKNOWN");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Сервер получил запрос от не авторизированного/несуществующего пользователя")
    void handleErrorInProcessingOfDataResponseIfOwnerNotFoundTest() throws Exception {
        var request = patch("/bookings/{booking-id}", 1L)
                .header("X-Sharer-User-Id", 9999L)
                .queryParam("approved","true");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Попытка создать пользователя с уже существующим email")
    void handleConflictRequestResponseIfUserAlreadyExistsTest() throws Exception {
        var request1 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"name\":\"name1\",\"email\":\"name@email.com\"}");
        mockMvc.perform(request1)
                .andExpect(status().isCreated());
        var request2 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"name\":\"name2\",\"email\":\"name@email.com\"}");
        mockMvc.perform(request2)
                .andExpect(status().isConflict());

    }

    @Test
    @DisplayName("Пользователь пытается подтвердить запрос на аренду предмета, ему не принадлежащему ")
    void handleAccessDeniedResponseIfNotOwnersApproveTest() throws Exception {
        var request1 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"name\":\"name1\",\"email\":\"name1@email.com\"}");
        mockMvc.perform(request1).andExpect(status().isCreated());
        var request2 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"name\":\"name2\",\"email\":\"name2@email.com\"}");
        mockMvc.perform(request2).andExpect(status().isCreated());
        var request3 = post("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"name\":\"item1\",\"description\":\"desc\",\"available\":true,\"requestId\":null}");
        mockMvc.perform(request3).andExpect(status().isCreated());
        var request4 = post("/bookings")
                .header("X-Sharer-User-Id", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"itemId\":1,\"start\": \"" + LocalDateTime.now().plusDays(1) +
                         "\",\"end\":\"" + LocalDateTime.now().plusDays(10) + "\"}");
        mockMvc.perform(request4).andExpect(status().isCreated());
        var request5 = patch("/bookings/1")
                .header("X-Sharer-User-Id", 2L)
                .queryParam("approved","true");
        mockMvc.perform(request5).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Отсутствует обязательное тело запроса ")
    void handleHttpMessageNotReadableExceptionResponseIfNoBodyRequestTest() throws Exception {
        var request1 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");
        mockMvc.perform(request1)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Пользователь не найден в процессе выполнения запроса")
    void handleNotFoundErrorResponseIfUserNotFoundTest() throws Exception {
        mockMvc.perform(get("/users/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Обработка прочих непредусмотренных ошибок и исключений")
    void handleInternalServerFailureResponseTest() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isInternalServerError());
    }
}
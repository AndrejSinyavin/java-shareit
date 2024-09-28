package ru.practicum.shareit.model.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.model.request.dto.ItemRequestDto;
import ru.practicum.shareit.model.request.dto.ItemRequestDtoWithAnswer;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("select new ru.practicum.shareit.model.request.dto.ItemRequestDtoWithAnswer(" +
            "i.id, i.description, i.created, null  )" +
            " from ItemRequest i where i.requester.id = ?1 order by i.created DESC")
    List<ItemRequestDtoWithAnswer> findByRequester_IdOrderByCreatedDesc(Long id);

    List<ItemRequestDto> findByRequester_IdNotOrderByCreatedDesc(Long id);
}
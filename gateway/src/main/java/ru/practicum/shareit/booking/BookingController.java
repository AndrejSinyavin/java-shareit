package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingSearchCriteria;
import ru.practicum.shareit.exception.EntityValidateException;

import java.util.Optional;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
	static String RESPONSE = "Response: {}";
	static String USER_UNDEFINED = "Не указан ID пользователя, создавшего запрос на сервер";
	static String ABSENT_HEADER = "Отсутствует заголовок ";
	static String BOOKING_STATE_WRONG = "Не распознан статус бронирования предмета ";
	static final String HEADER_SHARER = "X-Sharer-User-Id";
	static final String BOOKING_ID = "booking-id";
	static final String APPROVED = "approved";
	static final String STATE = "state";
	static final String ALL = "ALL";
	static final String POSITIVE = "ID может быть только положительным значением";
	String thisService = this.getClass().getSimpleName();
	BookingClient bookingClient;

	@PostMapping
	public Object createBooking(@RequestHeader(HEADER_SHARER) @Positive(message = POSITIVE) long bookerId,
								@RequestBody @Valid BookingDtoCreate requestDto) {
		checkSharerHeader(bookerId);
		var response = bookingClient.add(bookerId, requestDto);
		log.info(RESPONSE, response);
		return response;
	}

	@PatchMapping("/{booking-id}")
	public Object confirmBooking(
			@PathVariable(value = BOOKING_ID) @Positive(message = POSITIVE) long bookingId,
			@RequestHeader(value = HEADER_SHARER, required = false) @Positive(message = POSITIVE) long ownerId,
			@RequestParam(value = APPROVED) boolean approved) {
		checkSharerHeader(ownerId);
		var response = bookingClient.approve(ownerId, bookingId, approved);
		log.info(RESPONSE, response);
		return response;
	}

	@GetMapping("/{booking-id}")
	public Object getBooking(@PathVariable(value = BOOKING_ID) @Positive(message = POSITIVE) long bookingId,
							 @RequestHeader(value = HEADER_SHARER) @Positive(message = POSITIVE) long userId) {
		checkSharerHeader(userId);
		var response = bookingClient.get(userId, bookingId);
		log.info(RESPONSE, response);
		return response;
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByUser(
			@RequestHeader(value = HEADER_SHARER, required = false) @Positive(message = POSITIVE) Long userId,
			@RequestParam(value = STATE, defaultValue = ALL) String state) {
		checkSharerHeader(userId);
		checkState(state);
		var response = bookingClient.getBookingsByUser(userId, state);
		log.info(RESPONSE, response);
		return response;
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsForOwnerItems(
			@RequestHeader(value = HEADER_SHARER, required = false) @Positive(message = POSITIVE) Long ownerId,
			@RequestParam(value = STATE, defaultValue = ALL) String state) {
		checkSharerHeader(ownerId);
		checkState(state);
		var response = bookingClient.getBookingsForOwnerItems(ownerId, state);
		log.info(RESPONSE, response);
		return response;
	}

	private void checkSharerHeader(Long ownerId) {
		Optional.ofNullable(ownerId).orElseThrow(
				() -> new EntityValidateException(
						thisService, USER_UNDEFINED, ABSENT_HEADER.concat(HEADER_SHARER)
				)
		);
	}

	private void checkState(String state) {
		try {
			BookingSearchCriteria.valueOf(state);
		} catch (IllegalArgumentException e) {
			throw new EntityValidateException(thisService, BOOKING_STATE_WRONG, state);
		}
	}

}

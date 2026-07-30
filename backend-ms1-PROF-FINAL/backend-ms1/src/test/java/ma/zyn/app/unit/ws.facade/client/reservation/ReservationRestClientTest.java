package ma.zyn.app.unit.ws.facade.client.reservation;

import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.service.impl.client.reservation.ReservationClientServiceImpl;
import ma.zyn.app.ws.facade.client.reservation.ReservationRestClient;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import org.aspectj.lang.annotation.Before;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationRestClientTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationClientServiceImpl service;
    @Mock
    private ReservationConverter converter;

    @InjectMocks
    private ReservationRestClient controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllReservationTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<ReservationDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<ReservationDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveReservationTest() throws Exception {
        // Mock data
        ReservationDto requestDto = new ReservationDto();
        Reservation entity = new Reservation();
        Reservation saved = new Reservation();
        ReservationDto savedDto = new ReservationDto();

        // Mock the converter to return the reservation object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved reservation DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<ReservationDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        ReservationDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved reservation DTO
        assertEquals(savedDto.getReference(), responseBody.getReference());
        assertEquals(savedDto.getAmount(), responseBody.getAmount());
        assertEquals(savedDto.getPricePerNight(), responseBody.getPricePerNight());
    }

}

package ma.zyn.app.unit.ws.facade.collaborator.reservation;

import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.service.impl.collaborator.reservation.ReservationRequestCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.reservation.ReservationRequestRestCollaborator;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
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
public class ReservationRequestRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationRequestCollaboratorServiceImpl service;
    @Mock
    private ReservationRequestConverter converter;

    @InjectMocks
    private ReservationRequestRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllReservationRequestTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<ReservationRequestDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<ReservationRequestDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveReservationRequestTest() throws Exception {
        // Mock data
        ReservationRequestDto requestDto = new ReservationRequestDto();
        ReservationRequest entity = new ReservationRequest();
        ReservationRequest saved = new ReservationRequest();
        ReservationRequestDto savedDto = new ReservationRequestDto();

        // Mock the converter to return the reservationRequest object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved reservationRequest DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<ReservationRequestDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        ReservationRequestDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved reservationRequest DTO
        assertEquals(savedDto.getClientNote(), responseBody.getClientNote());
        assertEquals(savedDto.getStaffNote(), responseBody.getStaffNote());
    }

}

package ma.zyn.app.unit.ws.facade.admin.document;

import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.service.impl.admin.document.DocumentAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.document.DocumentRestAdmin;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.ws.dto.document.DocumentDto;
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
public class DocumentRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private DocumentAdminServiceImpl service;
    @Mock
    private DocumentConverter converter;

    @InjectMocks
    private DocumentRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllDocumentTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<DocumentDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<DocumentDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveDocumentTest() throws Exception {
        // Mock data
        DocumentDto requestDto = new DocumentDto();
        Document entity = new Document();
        Document saved = new Document();
        DocumentDto savedDto = new DocumentDto();

        // Mock the converter to return the document object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved document DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<DocumentDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        DocumentDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved document DTO
        assertEquals(savedDto.getFileName(), responseBody.getFileName());
        assertEquals(savedDto.getFile(), responseBody.getFile());
        assertEquals(savedDto.getExtractedVendor(), responseBody.getExtractedVendor());
        assertEquals(savedDto.getExtractedAmount(), responseBody.getExtractedAmount());
    }

}

package ma.zyn.app.ws.facade.open;

import ma.zyn.app.bean.core.client.Client;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.service.facade.admin.client.ClientAdminService;
import ma.zyn.app.service.facade.admin.property.PropertyAdminService;
import ma.zyn.app.service.facade.admin.reservation.ReservationRequestAdminService;
import ma.zyn.app.service.facade.admin.reservation.ReservationRequestStatusAdminService;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.ws.dto.property.PropertyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/open/reservation-request/")
public class ReservationRequestRestOpen {

    private final PropertyAdminService propertyService;
    private final PropertyConverter propertyConverter;
    private final ClientAdminService clientService;
    private final ReservationRequestAdminService reservationRequestService;
    private final ReservationRequestStatusAdminService reservationRequestStatusService;

    public ReservationRequestRestOpen(PropertyAdminService propertyService,
                                      PropertyConverter propertyConverter,
                                      ClientAdminService clientService,
                                      ReservationRequestAdminService reservationRequestService,
                                      ReservationRequestStatusAdminService reservationRequestStatusService) {
        this.propertyService = propertyService;
        this.propertyConverter = propertyConverter;
        this.clientService = clientService;
        this.reservationRequestService = reservationRequestService;
        this.reservationRequestStatusService = reservationRequestStatusService;
    }

    // Liste publique des propriétés (infos utiles uniquement, pas de données sensibles).
    @GetMapping("properties")
    public ResponseEntity<List<PropertyDto>> findAvailableProperties() {
        List<Property> list = propertyService.findAll();
        propertyConverter.initList(false);
        propertyConverter.initObject(true);
        List<PropertyDto> dtos = propertyConverter.toDto(list);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    public static class PublicRequestInput {
        public Long propertyId;
        public String fullName;
        public String phone;
        public String checkIn;
        public String checkOut;
        public String message;
    }

    // Crée une demande de réservation, sans authentification.
    @PostMapping("")
    public ResponseEntity<String> createRequest(@RequestBody PublicRequestInput input) {
        if (input == null || input.propertyId == null
                || input.fullName == null || input.fullName.isBlank()
                || input.phone == null || input.phone.isBlank()) {
            return new ResponseEntity<>("Champs requis manquants", HttpStatus.BAD_REQUEST);
        }

        Property property = propertyService.findById(input.propertyId);
        if (property == null) {
            return new ResponseEntity<>("Propriété introuvable", HttpStatus.NOT_FOUND);
        }

        // Cherche un client existant avec ce téléphone, sinon en crée un nouveau.
        Client client = clientService.findAll().stream()
                .filter(c -> input.phone.equals(c.getPhone()))
                .findFirst()
                .orElse(null);

        if (client == null) {
            client = new Client();
            client.setFullName(input.fullName);
            client.setPhone(input.phone);
            client.setUsername("guest_" + UUID.randomUUID().toString().substring(0, 8));
            client.setPassword(UUID.randomUUID().toString());
            client = clientService.create(client);
            if (client == null) {
                return new ResponseEntity<>("Erreur lors de la création du client", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        ReservationRequestStatus pending = reservationRequestStatusService.findAll().stream()
                .filter(s -> "EnAttente".equals(s.getCode()))
                .findFirst()
                .orElse(null);

        StringBuilder note = new StringBuilder();
        if (input.checkIn != null || input.checkOut != null) {
            note.append("Dates souhaitées : du ").append(input.checkIn).append(" au ").append(input.checkOut).append(". ");
        }
        if (input.message != null && !input.message.isBlank()) {
            note.append(input.message);
        }

        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setClient(client);
        reservationRequest.setRequestedProperty(property);
        reservationRequest.setReservationRequestStatus(pending);
        reservationRequest.setClientNote(note.toString());

        ReservationRequest saved = reservationRequestService.create(reservationRequest);
        if (saved == null) {
            return new ResponseEntity<>("Erreur lors de la création de la demande", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Demande envoyée", HttpStatus.CREATED);
    }
}
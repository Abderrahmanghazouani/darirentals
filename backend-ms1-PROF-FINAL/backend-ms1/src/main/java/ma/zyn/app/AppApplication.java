package ma.zyn.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.openfeign.EnableFeignClients;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;


import ma.zyn.app.bean.core.client.Client;
import ma.zyn.app.service.facade.admin.client.ClientAdminService;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.service.facade.admin.auth.CollaboratorAdminService;
import ma.zyn.app.zynerator.security.bean.*;
import ma.zyn.app.zynerator.security.common.AuthoritiesConstants;
import ma.zyn.app.zynerator.security.service.facade.*;

import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.service.facade.admin.auth.CollaboratorRoleAdminService;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.service.facade.admin.task.TaskTypeAdminService;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.service.facade.admin.task.TaskStatusAdminService;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.service.facade.admin.charge.ChargeTypeAdminService;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.service.facade.admin.payment.PaymentStatusAdminService;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.service.facade.admin.reservation.ReservationStatusAdminService;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.service.facade.admin.report.FinancialReportScopeAdminService;
import ma.zyn.app.bean.core.payment.PaymentType;
import ma.zyn.app.service.facade.admin.payment.PaymentTypeAdminService;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.service.facade.admin.reservation.ReservationRequestStatusAdminService;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.service.facade.admin.reservation.ReservationPlatformAdminService;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.service.facade.admin.ai.AiUsageTypeAdminService;
import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.service.facade.admin.provider.ServiceTypeAdminService;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.service.facade.admin.property.PropertyTypeAdminService;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.service.facade.admin.task.TaskPriorityAdminService;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.service.facade.admin.property.PropertyStatusAdminService;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.service.facade.admin.currency.CurrencyAdminService;
import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.service.facade.admin.report.FinancialReportTypeAdminService;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.service.facade.admin.document.DocumentTypeAdminService;

import ma.zyn.app.zynerator.security.bean.User;
import ma.zyn.app.zynerator.security.bean.Role;

@SpringBootApplication
//@EnableFeignClients
public class AppApplication {
    public static ConfigurableApplicationContext ctx;


    //state: primary success info secondary warning danger contrast
    //_STATE(Pending=warning,Rejeted=danger,Validated=success)
    public static void main(String[] args) {
        ctx=SpringApplication.run(AppApplication.class, args);
    }


    @Bean
    ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // Sans ça, Jackson sérialise LocalDate/LocalDateTime en tableau [année,mois,jour,...]
        // au lieu d'une chaîne ISO ("2026-08-10"), ce que le frontend (et tout client JSON standard)
        // n'attend pas.
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static ConfigurableApplicationContext getCtx() {
        return ctx;
    }

    @Bean
    public CommandLineRunner demo(UserService userService, RoleService roleService, ModelPermissionService modelPermissionService, ActionPermissionService actionPermissionService, ModelPermissionUserService modelPermissionUserService , ClientAdminService clientService, CollaboratorAdminService collaboratorService) {
    return (args) -> {
        if(true){

            createCollaboratorRole();
            createTaskType();
            createTaskStatus();
            createChargeType();
            createPaymentStatus();
            createReservationStatus();
            createFinancialReportScope();
            createPaymentType();
            createReservationRequestStatus();
            createReservationPlatform();
            createAiUsageType();
            createServiceType();
            createPropertyType();
            createTaskPriority();
            createPropertyStatus();
            createCurrency();
            createFinancialReportType();
            createDocumentType();

        /*
        List<ModelPermission> modelPermissions = new ArrayList<>();
        addPermission(modelPermissions);
        modelPermissions.forEach(e -> modelPermissionService.create(e));
        List<ActionPermission> actionPermissions = new ArrayList<>();
        addActionPermission(actionPermissions);
        actionPermissions.forEach(e -> actionPermissionService.create(e));
        */

		// User Admin
        User userForAdmin = new User("admin");
		userForAdmin.setPassword("123");
		// Role Admin
		Role roleForAdmin = new Role();
		roleForAdmin.setAuthority(AuthoritiesConstants.ADMIN);
		roleForAdmin.setCreatedAt(LocalDateTime.now());
		Role roleForAdminSaved = roleService.create(roleForAdmin);
		RoleUser roleUserForAdmin = new RoleUser();
		roleUserForAdmin.setRole(roleForAdminSaved);
		if (userForAdmin.getRoleUsers() == null)
			userForAdmin.setRoleUsers(new ArrayList<>());

		userForAdmin.getRoleUsers().add(roleUserForAdmin);


        userForAdmin.setModelPermissionUsers(modelPermissionUserService.initModelPermissionUser());

        userService.create(userForAdmin);

		// User Client
        Client userForClient = new Client("client");
		userForClient.setPassword("123");
		// Role Client
		Role roleForClient = new Role();
		roleForClient.setAuthority(AuthoritiesConstants.CLIENT);
		roleForClient.setCreatedAt(LocalDateTime.now());
		Role roleForClientSaved = roleService.create(roleForClient);
		RoleUser roleUserForClient = new RoleUser();
		roleUserForClient.setRole(roleForClientSaved);
		if (userForClient.getRoleUsers() == null)
			userForClient.setRoleUsers(new ArrayList<>());

		userForClient.getRoleUsers().add(roleUserForClient);


        userForClient.setModelPermissionUsers(modelPermissionUserService.initModelPermissionUser());

        clientService.create(userForClient);

		// User Collaborator
        Collaborator userForCollaborator = new Collaborator("collaborator");
		userForCollaborator.setPassword("123");
		// Role Collaborator
		Role roleForCollaborator = new Role();
		roleForCollaborator.setAuthority(AuthoritiesConstants.COLLABORATOR);
		roleForCollaborator.setCreatedAt(LocalDateTime.now());
		Role roleForCollaboratorSaved = roleService.create(roleForCollaborator);
		RoleUser roleUserForCollaborator = new RoleUser();
		roleUserForCollaborator.setRole(roleForCollaboratorSaved);
		if (userForCollaborator.getRoleUsers() == null)
			userForCollaborator.setRoleUsers(new ArrayList<>());

		userForCollaborator.getRoleUsers().add(roleUserForCollaborator);


        userForCollaborator.setModelPermissionUsers(modelPermissionUserService.initModelPermissionUser());

        collaboratorService.create(userForCollaborator);

            }
        };
    }



    private void createCollaboratorRole(){
            CollaboratorRole itemDanger = new CollaboratorRole();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("SubAdmin");
            itemDanger.setCode("SubAdmin");
            collaboratorRoleService.create(itemDanger);
            CollaboratorRole itemInfo = new CollaboratorRole();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Gestionnaire");
            itemInfo.setCode("Gestionnaire");
            collaboratorRoleService.create(itemInfo);

    }
    private void createTaskType(){
            TaskType itemSuccess = new TaskType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Menage");
            itemSuccess.setCode("Menage");
            taskTypeService.create(itemSuccess);
            TaskType itemInfo = new TaskType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Generale");
            itemInfo.setCode("Generale");
            taskTypeService.create(itemInfo);

    }
    private void createTaskStatus(){
            TaskStatus itemSuccess = new TaskStatus();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Terminee");
            itemSuccess.setCode("Terminee");
            taskStatusService.create(itemSuccess);
            TaskStatus itemDanger = new TaskStatus();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("AFaire");
            itemDanger.setCode("AFaire");
            taskStatusService.create(itemDanger);
            TaskStatus itemWarning = new TaskStatus();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("EnCours");
            itemWarning.setCode("EnCours");
            taskStatusService.create(itemWarning);

    }
    private void createChargeType(){
            ChargeType itemInfo = new ChargeType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Eau");
            itemInfo.setCode("Eau");
            chargeTypeService.create(itemInfo);
            ChargeType itemWarning = new ChargeType();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Electricite");
            itemWarning.setCode("Electricite");
            chargeTypeService.create(itemWarning);
            ChargeType itemWarning2 = new ChargeType();
            itemWarning2.setStyle("warning2");
            itemWarning2.setLabel("Autre");
            itemWarning2.setCode("Autre");
            chargeTypeService.create(itemWarning2);
            ChargeType itemInfo2 = new ChargeType();
            itemInfo2.setStyle("info2");
            itemInfo2.setLabel("Internet");
            itemInfo2.setCode("Internet");
            chargeTypeService.create(itemInfo2);
            ChargeType itemDanger = new ChargeType();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("Maintenance");
            itemDanger.setCode("Maintenance");
            chargeTypeService.create(itemDanger);
            ChargeType itemSuccess = new ChargeType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Menage");
            itemSuccess.setCode("Menage");
            chargeTypeService.create(itemSuccess);

    }
    private void createPaymentStatus(){
            PaymentStatus itemSuccess = new PaymentStatus();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Paye");
            itemSuccess.setCode("Paye");
            paymentStatusService.create(itemSuccess);
            PaymentStatus itemWarning = new PaymentStatus();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Partiel");
            itemWarning.setCode("Partiel");
            paymentStatusService.create(itemWarning);
            PaymentStatus itemDanger = new PaymentStatus();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("EnAttente");
            itemDanger.setCode("EnAttente");
            paymentStatusService.create(itemDanger);

    }
    private void createReservationStatus(){
            ReservationStatus itemInfo2 = new ReservationStatus();
            itemInfo2.setStyle("info2");
            itemInfo2.setLabel("Terminee");
            itemInfo2.setCode("Terminee");
            reservationStatusService.create(itemInfo2);
            ReservationStatus itemSuccess = new ReservationStatus();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Confirmee");
            itemSuccess.setCode("Confirmee");
            reservationStatusService.create(itemSuccess);
            ReservationStatus itemWarning = new ReservationStatus();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("EnCours");
            itemWarning.setCode("EnCours");
            reservationStatusService.create(itemWarning);
            ReservationStatus itemDanger = new ReservationStatus();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("Annulee");
            itemDanger.setCode("Annulee");
            reservationStatusService.create(itemDanger);

    }
    private void createFinancialReportScope(){
            FinancialReportScope itemInfo = new FinancialReportScope();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Entreprise");
            itemInfo.setCode("Entreprise");
            financialReportScopeService.create(itemInfo);
            FinancialReportScope itemSuccess = new FinancialReportScope();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Proprietes");
            itemSuccess.setCode("Proprietes");
            financialReportScopeService.create(itemSuccess);

    }
    private void createPaymentType(){
            PaymentType itemWarning = new PaymentType();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Avance");
            itemWarning.setCode("Avance");
            paymentTypeService.create(itemWarning);
            PaymentType itemInfo = new PaymentType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Solde");
            itemInfo.setCode("Solde");
            paymentTypeService.create(itemInfo);
            PaymentType itemSuccess = new PaymentType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("PaiementComplet");
            itemSuccess.setCode("PaiementComplet");
            paymentTypeService.create(itemSuccess);

    }
    private void createReservationRequestStatus(){
            ReservationRequestStatus itemSuccess = new ReservationRequestStatus();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Confirmee");
            itemSuccess.setCode("Confirmee");
            reservationRequestStatusService.create(itemSuccess);
            ReservationRequestStatus itemDanger = new ReservationRequestStatus();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("Rejetee");
            itemDanger.setCode("Rejetee");
            reservationRequestStatusService.create(itemDanger);
            ReservationRequestStatus itemWarning = new ReservationRequestStatus();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("EnAttente");
            itemWarning.setCode("EnAttente");
            reservationRequestStatusService.create(itemWarning);
            ReservationRequestStatus itemInfo = new ReservationRequestStatus();
            itemInfo.setStyle("info");
            itemInfo.setLabel("PropositionAlternative");
            itemInfo.setCode("PropositionAlternative");
            reservationRequestStatusService.create(itemInfo);

    }
    private void createReservationPlatform(){
            ReservationPlatform itemSuccess = new ReservationPlatform();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Booking");
            itemSuccess.setCode("Booking");
            reservationPlatformService.create(itemSuccess);
            ReservationPlatform itemInfo = new ReservationPlatform();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Airbnb");
            itemInfo.setCode("Airbnb");
            reservationPlatformService.create(itemInfo);
            ReservationPlatform itemWarning = new ReservationPlatform();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Direct");
            itemWarning.setCode("Direct");
            reservationPlatformService.create(itemWarning);

    }
    private void createAiUsageType(){
            AiUsageType itemWarning = new AiUsageType();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("ExtractionPasseport");
            itemWarning.setCode("ExtractionPasseport");
            aiUsageTypeService.create(itemWarning);
            AiUsageType itemInfo = new AiUsageType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("ExtractionFacture");
            itemInfo.setCode("ExtractionFacture");
            aiUsageTypeService.create(itemInfo);
            AiUsageType itemSuccess = new AiUsageType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Autre");
            itemSuccess.setCode("Autre");
            aiUsageTypeService.create(itemSuccess);

    }
    private void createServiceType(){
            ServiceType itemInfo = new ServiceType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Comptabilite");
            itemInfo.setCode("Comptabilite");
            serviceTypeService.create(itemInfo);
            ServiceType itemDanger2 = new ServiceType();
            itemDanger2.setStyle("danger2");
            itemDanger2.setLabel("Autre");
            itemDanger2.setCode("Autre");
            serviceTypeService.create(itemDanger2);
            ServiceType itemWarning = new ServiceType();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Maintenance");
            itemWarning.setCode("Maintenance");
            serviceTypeService.create(itemWarning);
            ServiceType itemSuccess = new ServiceType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Menage");
            itemSuccess.setCode("Menage");
            serviceTypeService.create(itemSuccess);

    }
    private void createPropertyType(){
            PropertyType itemSuccess = new PropertyType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Appartement");
            itemSuccess.setCode("Appartement");
            propertyTypeService.create(itemSuccess);
            PropertyType itemInfo2 = new PropertyType();
            itemInfo2.setStyle("info2");
            itemInfo2.setLabel("Villa");
            itemInfo2.setCode("Villa");
            propertyTypeService.create(itemInfo2);
            PropertyType itemInfo = new PropertyType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Riad");
            itemInfo.setCode("Riad");
            propertyTypeService.create(itemInfo);
            PropertyType itemWarning = new PropertyType();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Studio");
            itemWarning.setCode("Studio");
            propertyTypeService.create(itemWarning);

    }
    private void createTaskPriority(){
            TaskPriority itemWarning = new TaskPriority();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Moyenne");
            itemWarning.setCode("Moyenne");
            taskPriorityService.create(itemWarning);
            TaskPriority itemInfo = new TaskPriority();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Basse");
            itemInfo.setCode("Basse");
            taskPriorityService.create(itemInfo);
            TaskPriority itemDanger = new TaskPriority();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("Haute");
            itemDanger.setCode("Haute");
            taskPriorityService.create(itemDanger);

    }
    private void createPropertyStatus(){
            PropertyStatus itemSuccess = new PropertyStatus();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Active");
            itemSuccess.setCode("Active");
            propertyStatusService.create(itemSuccess);
            PropertyStatus itemDanger = new PropertyStatus();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("Inactive");
            itemDanger.setCode("Inactive");
            propertyStatusService.create(itemDanger);

    }
    private void createCurrency(){
            Currency itemSuccess = new Currency();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("MAD");
            itemSuccess.setCode("MAD");
            currencyService.create(itemSuccess);
            Currency itemInfo = new Currency();
            itemInfo.setStyle("info");
            itemInfo.setLabel("EUR");
            itemInfo.setCode("EUR");
            currencyService.create(itemInfo);
            Currency itemDanger = new Currency();
            itemDanger.setStyle("danger");
            itemDanger.setLabel("GBP");
            itemDanger.setCode("GBP");
            currencyService.create(itemDanger);
            Currency itemWarning = new Currency();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("USD");
            itemWarning.setCode("USD");
            currencyService.create(itemWarning);

    }
    private void createFinancialReportType(){
            FinancialReportType itemInfo = new FinancialReportType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Mensuel");
            itemInfo.setCode("Mensuel");
            financialReportTypeService.create(itemInfo);
            FinancialReportType itemSuccess = new FinancialReportType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("Annuel");
            itemSuccess.setCode("Annuel");
            financialReportTypeService.create(itemSuccess);

    }
    private void createDocumentType(){
            DocumentType itemInfo = new DocumentType();
            itemInfo.setStyle("info");
            itemInfo.setLabel("Passeport");
            itemInfo.setCode("Passeport");
            documentTypeService.create(itemInfo);
            DocumentType itemSuccess = new DocumentType();
            itemSuccess.setStyle("success");
            itemSuccess.setLabel("TicketDeCaisse");
            itemSuccess.setCode("TicketDeCaisse");
            documentTypeService.create(itemSuccess);
            DocumentType itemWarning = new DocumentType();
            itemWarning.setStyle("warning");
            itemWarning.setLabel("Facture");
            itemWarning.setCode("Facture");
            documentTypeService.create(itemWarning);

    }

    private static String fakeString(String attributeName, int i) {
        return attributeName + i;
    }

    private static Long fakeLong(String attributeName, int i) {
        return  10L * i;
    }
    private static Integer fakeInteger(String attributeName, int i) {
        return  10 * i;
    }

    private static Double fakeDouble(String attributeName, int i) {
        return 10D * i;
    }

    private static BigDecimal fakeBigDecimal(String attributeName, int i) {
        return  BigDecimal.valueOf(i*1L*10);
    }

    private static Boolean fakeBoolean(String attributeName, int i) {
        return i % 2 == 0 ? true : false;
    }
    private static LocalDateTime fakeLocalDateTime(String attributeName, int i) {
        return LocalDateTime.now().plusDays(i);
    }


    private static void addPermission(List<ModelPermission> modelPermissions) {
        modelPermissions.add(new ModelPermission("CollaboratorRole"));
        modelPermissions.add(new ModelPermission("Reservation"));
        modelPermissions.add(new ModelPermission("Payment"));
        modelPermissions.add(new ModelPermission("TaskType"));
        modelPermissions.add(new ModelPermission("FinancialReport"));
        modelPermissions.add(new ModelPermission("Enterprise"));
        modelPermissions.add(new ModelPermission("TaskStatus"));
        modelPermissions.add(new ModelPermission("ChargeType"));
        modelPermissions.add(new ModelPermission("EnterpriseMembership"));
        modelPermissions.add(new ModelPermission("PaymentStatus"));
        modelPermissions.add(new ModelPermission("ReservationStatus"));
        modelPermissions.add(new ModelPermission("FinancialReportScope"));
        modelPermissions.add(new ModelPermission("PaymentType"));
        modelPermissions.add(new ModelPermission("Charge"));
        modelPermissions.add(new ModelPermission("ReservationRequestStatus"));
        modelPermissions.add(new ModelPermission("AiQuota"));
        modelPermissions.add(new ModelPermission("Client"));
        modelPermissions.add(new ModelPermission("ReservationPlatform"));
        modelPermissions.add(new ModelPermission("AiUsageType"));
        modelPermissions.add(new ModelPermission("Document"));
        modelPermissions.add(new ModelPermission("CollaboratorPermissionOverride"));
        modelPermissions.add(new ModelPermission("ServiceType"));
        modelPermissions.add(new ModelPermission("ServiceProvider"));
        modelPermissions.add(new ModelPermission("ReservationRequest"));
        modelPermissions.add(new ModelPermission("FinancialReportProperty"));
        modelPermissions.add(new ModelPermission("AiUsageLog"));
        modelPermissions.add(new ModelPermission("PropertyType"));
        modelPermissions.add(new ModelPermission("ExchangeRate"));
        modelPermissions.add(new ModelPermission("City"));
        modelPermissions.add(new ModelPermission("TaskPriority"));
        modelPermissions.add(new ModelPermission("Collaborator"));
        modelPermissions.add(new ModelPermission("PropertyStatus"));
        modelPermissions.add(new ModelPermission("Currency"));
        modelPermissions.add(new ModelPermission("FinancialReportType"));
        modelPermissions.add(new ModelPermission("Country"));
        modelPermissions.add(new ModelPermission("Task"));
        modelPermissions.add(new ModelPermission("DocumentType"));
        modelPermissions.add(new ModelPermission("Property"));
        modelPermissions.add(new ModelPermission("User"));
        modelPermissions.add(new ModelPermission("ModelPermission"));
        modelPermissions.add(new ModelPermission("ActionPermission"));
    }

    private static void addActionPermission(List<ActionPermission> actionPermissions) {
        actionPermissions.add(new ActionPermission("list"));
        actionPermissions.add(new ActionPermission("create"));
        actionPermissions.add(new ActionPermission("delete"));
        actionPermissions.add(new ActionPermission("edit"));
        actionPermissions.add(new ActionPermission("view"));
        actionPermissions.add(new ActionPermission("duplicate"));
    }


    @Autowired
    CollaboratorRoleAdminService collaboratorRoleService;
    @Autowired
    TaskTypeAdminService taskTypeService;
    @Autowired
    TaskStatusAdminService taskStatusService;
    @Autowired
    ChargeTypeAdminService chargeTypeService;
    @Autowired
    PaymentStatusAdminService paymentStatusService;
    @Autowired
    ReservationStatusAdminService reservationStatusService;
    @Autowired
    FinancialReportScopeAdminService financialReportScopeService;
    @Autowired
    PaymentTypeAdminService paymentTypeService;
    @Autowired
    ReservationRequestStatusAdminService reservationRequestStatusService;
    @Autowired
    ReservationPlatformAdminService reservationPlatformService;
    @Autowired
    AiUsageTypeAdminService aiUsageTypeService;
    @Autowired
    ServiceTypeAdminService serviceTypeService;
    @Autowired
    PropertyTypeAdminService propertyTypeService;
    @Autowired
    TaskPriorityAdminService taskPriorityService;
    @Autowired
    PropertyStatusAdminService propertyStatusService;
    @Autowired
    CurrencyAdminService currencyService;
    @Autowired
    FinancialReportTypeAdminService financialReportTypeService;
    @Autowired
    DocumentTypeAdminService documentTypeService;
}



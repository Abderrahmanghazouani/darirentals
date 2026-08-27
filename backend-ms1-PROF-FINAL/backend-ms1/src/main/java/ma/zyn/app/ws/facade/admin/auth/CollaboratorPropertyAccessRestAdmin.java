package  ma.zyn.app.ws.facade.admin.auth;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import ma.zyn.app.bean.core.auth.CollaboratorPropertyAccess;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorPropertyAccessCriteria;
import ma.zyn.app.service.facade.admin.auth.CollaboratorPropertyAccessAdminService;
import ma.zyn.app.ws.converter.auth.CollaboratorPropertyAccessConverter;
import ma.zyn.app.ws.dto.auth.CollaboratorPropertyAccessDto;
import ma.zyn.app.zynerator.util.PaginatedList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Chantier 3 (NOTES-permissions.md) : CRUD des affectations Collaborator <-> Property.
 * Pas de logique de permission specifique ici - c'est un detail d'implementation gere
 * exclusivement par l'admin (via l'ecran de creation/edition d'un collaborateur
 * Gestionnaire). L'application reelle de la restriction se fait dans
 * PropertyCollaboratorServiceImpl/EnterpriseAccessService, pas ici.
 */
@RestController
@RequestMapping("/api/admin/collaboratorPropertyAccess/")
public class CollaboratorPropertyAccessRestAdmin {

    @Operation(summary = "Finds a list of all collaboratorPropertyAccesses")
    @GetMapping("")
    public ResponseEntity<List<CollaboratorPropertyAccessDto>> findAll() throws Exception {
        List<CollaboratorPropertyAccess> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<CollaboratorPropertyAccessDto> dtos = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        return new ResponseEntity<>(dtos, status);
    }

    @Operation(summary = "Finds a collaboratorPropertyAccess by id")
    @GetMapping("id/{id}")
    public ResponseEntity<CollaboratorPropertyAccessDto> findById(@PathVariable Long id) {
        CollaboratorPropertyAccess t = service.findById(id);
        if (t != null) {
            converter.init(true);
            CollaboratorPropertyAccessDto dto = converter.toDto(t);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified collaboratorPropertyAccess")
    @PostMapping("")
    public ResponseEntity<CollaboratorPropertyAccessDto> save(@RequestBody CollaboratorPropertyAccessDto dto) throws Exception {
        if (dto != null) {
            converter.init(true);
            CollaboratorPropertyAccess myT = converter.toItem(dto);
            CollaboratorPropertyAccess t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            } else {
                CollaboratorPropertyAccessDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified collaboratorPropertyAccess")
    @PutMapping("")
    public ResponseEntity<CollaboratorPropertyAccessDto> update(@RequestBody CollaboratorPropertyAccessDto dto) throws Exception {
        ResponseEntity<CollaboratorPropertyAccessDto> res;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            CollaboratorPropertyAccess t = service.findById(dto.getId());
            converter.copy(dto, t);
            CollaboratorPropertyAccess updated = service.update(t);
            CollaboratorPropertyAccessDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of collaboratorPropertyAccess")
    @PostMapping("multiple")
    public ResponseEntity<List<CollaboratorPropertyAccessDto>> delete(@RequestBody List<CollaboratorPropertyAccessDto> dtos) throws Exception {
        ResponseEntity<List<CollaboratorPropertyAccessDto>> res;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<CollaboratorPropertyAccess> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified collaboratorPropertyAccess")
    @DeleteMapping("id/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) throws Exception {
        ResponseEntity<Long> res;
        HttpStatus status = HttpStatus.PRECONDITION_FAILED;
        if (id != null) {
            boolean resultDelete = service.deleteById(id);
            if (resultDelete) {
                status = HttpStatus.OK;
            }
        }
        res = new ResponseEntity<>(id, status);
        return res;
    }

    @Operation(summary = "find by collaborator id")
    @GetMapping("collaborator/id/{id}")
    public List<CollaboratorPropertyAccessDto> findByCollaboratorId(@PathVariable Long id) {
        return findDtos(service.findByCollaboratorId(id));
    }

    @Operation(summary = "delete by collaborator id")
    @DeleteMapping("collaborator/id/{id}")
    public int deleteByCollaboratorId(@PathVariable Long id) {
        return service.deleteByCollaboratorId(id);
    }

    @Operation(summary = "find by property id")
    @GetMapping("property/id/{id}")
    public List<CollaboratorPropertyAccessDto> findByPropertyId(@PathVariable Long id) {
        return findDtos(service.findByPropertyId(id));
    }

    @Operation(summary = "delete by property id")
    @DeleteMapping("property/id/{id}")
    public int deleteByPropertyId(@PathVariable Long id) {
        return service.deleteByPropertyId(id);
    }

    @Operation(summary = "Finds a collaboratorPropertyAccess and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<CollaboratorPropertyAccessDto> findWithAssociatedLists(@PathVariable Long id) {
        CollaboratorPropertyAccess loaded = service.findWithAssociatedLists(id);
        converter.init(true);
        CollaboratorPropertyAccessDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds collaboratorPropertyAccesses by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<CollaboratorPropertyAccessDto>> findByCriteria(@RequestBody CollaboratorPropertyAccessCriteria criteria) throws Exception {
        List<CollaboratorPropertyAccess> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<CollaboratorPropertyAccessDto> dtos = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        return new ResponseEntity<>(dtos, status);
    }

    @Operation(summary = "Finds paginated collaboratorPropertyAccesses by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody CollaboratorPropertyAccessCriteria criteria) throws Exception {
        List<CollaboratorPropertyAccess> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<CollaboratorPropertyAccessDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets collaboratorPropertyAccess data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody CollaboratorPropertyAccessCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    public List<CollaboratorPropertyAccessDto> findDtos(List<CollaboratorPropertyAccess> list) {
        converter.initObject(true);
        return converter.toDto(list);
    }

    public CollaboratorPropertyAccessRestAdmin(CollaboratorPropertyAccessAdminService service, CollaboratorPropertyAccessConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    private final CollaboratorPropertyAccessAdminService service;
    private final CollaboratorPropertyAccessConverter converter;
}

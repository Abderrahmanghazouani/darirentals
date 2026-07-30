package  ma.zyn.app.ws.facade.collaborator.property;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.dao.criteria.core.property.PropertyStatusCriteria;
import ma.zyn.app.service.facade.collaborator.property.PropertyStatusCollaboratorService;
import ma.zyn.app.ws.converter.property.PropertyStatusConverter;
import ma.zyn.app.ws.dto.property.PropertyStatusDto;
import ma.zyn.app.zynerator.controller.AbstractController;
import ma.zyn.app.zynerator.dto.AuditEntityDto;
import ma.zyn.app.zynerator.util.PaginatedList;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ma.zyn.app.zynerator.process.Result;


import org.springframework.web.multipart.MultipartFile;
import ma.zyn.app.zynerator.dto.FileTempDto;

@RestController
@RequestMapping("/api/collaborator/propertyStatus/")
public class PropertyStatusRestCollaborator {




    @Operation(summary = "Finds a list of all propertyStatuss")
    @GetMapping("")
    public ResponseEntity<List<PropertyStatusDto>> findAll() throws Exception {
        ResponseEntity<List<PropertyStatusDto>> res = null;
        List<PropertyStatus> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PropertyStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all propertyStatuss")
    @GetMapping("optimized")
    public ResponseEntity<List<PropertyStatusDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<PropertyStatusDto>> res = null;
        List<PropertyStatus> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PropertyStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a propertyStatus by id")
    @GetMapping("id/{id}")
    public ResponseEntity<PropertyStatusDto> findById(@PathVariable Long id) {
        PropertyStatus t = service.findById(id);
        if (t != null) {
            PropertyStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a propertyStatus by label")
    @GetMapping("label/{label}")
    public ResponseEntity<PropertyStatusDto> findByLabel(@PathVariable String label) {
	    PropertyStatus t = service.findByReferenceEntity(new PropertyStatus(label));
        if (t != null) {
            PropertyStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  propertyStatus")
    @PostMapping("")
    public ResponseEntity<PropertyStatusDto> save(@RequestBody PropertyStatusDto dto) throws Exception {
        if(dto!=null){
            PropertyStatus myT = converter.toItem(dto);
            PropertyStatus t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                PropertyStatusDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  propertyStatus")
    @PutMapping("")
    public ResponseEntity<PropertyStatusDto> update(@RequestBody PropertyStatusDto dto) throws Exception {
        ResponseEntity<PropertyStatusDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            PropertyStatus t = service.findById(dto.getId());
            converter.copy(dto,t);
            PropertyStatus updated = service.update(t);
            PropertyStatusDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of propertyStatus")
    @PostMapping("multiple")
    public ResponseEntity<List<PropertyStatusDto>> delete(@RequestBody List<PropertyStatusDto> dtos) throws Exception {
        ResponseEntity<List<PropertyStatusDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<PropertyStatus> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified propertyStatus")
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


    @Operation(summary = "Finds a propertyStatus and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<PropertyStatusDto> findWithAssociatedLists(@PathVariable Long id) {
        PropertyStatus loaded =  service.findWithAssociatedLists(id);
        PropertyStatusDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds propertyStatuss by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<PropertyStatusDto>> findByCriteria(@RequestBody PropertyStatusCriteria criteria) throws Exception {
        ResponseEntity<List<PropertyStatusDto>> res = null;
        List<PropertyStatus> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PropertyStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated propertyStatuss by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody PropertyStatusCriteria criteria) throws Exception {
        List<PropertyStatus> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<PropertyStatusDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets propertyStatus data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody PropertyStatusCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<PropertyStatusDto> findDtos(List<PropertyStatus> list){
        List<PropertyStatusDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<PropertyStatusDto> getDtoResponseEntity(PropertyStatusDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public PropertyStatusRestCollaborator(PropertyStatusCollaboratorService service, PropertyStatusConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final PropertyStatusCollaboratorService service;
    private final PropertyStatusConverter converter;





}

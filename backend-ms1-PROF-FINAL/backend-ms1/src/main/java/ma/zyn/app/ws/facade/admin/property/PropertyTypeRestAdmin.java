package  ma.zyn.app.ws.facade.admin.property;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.dao.criteria.core.property.PropertyTypeCriteria;
import ma.zyn.app.service.facade.admin.property.PropertyTypeAdminService;
import ma.zyn.app.ws.converter.property.PropertyTypeConverter;
import ma.zyn.app.ws.dto.property.PropertyTypeDto;
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
@RequestMapping("/api/admin/propertyType/")
public class PropertyTypeRestAdmin {




    @Operation(summary = "Finds a list of all propertyTypes")
    @GetMapping("")
    public ResponseEntity<List<PropertyTypeDto>> findAll() throws Exception {
        ResponseEntity<List<PropertyTypeDto>> res = null;
        List<PropertyType> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PropertyTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all propertyTypes")
    @GetMapping("optimized")
    public ResponseEntity<List<PropertyTypeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<PropertyTypeDto>> res = null;
        List<PropertyType> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PropertyTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a propertyType by id")
    @GetMapping("id/{id}")
    public ResponseEntity<PropertyTypeDto> findById(@PathVariable Long id) {
        PropertyType t = service.findById(id);
        if (t != null) {
            PropertyTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a propertyType by label")
    @GetMapping("label/{label}")
    public ResponseEntity<PropertyTypeDto> findByLabel(@PathVariable String label) {
	    PropertyType t = service.findByReferenceEntity(new PropertyType(label));
        if (t != null) {
            PropertyTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  propertyType")
    @PostMapping("")
    public ResponseEntity<PropertyTypeDto> save(@RequestBody PropertyTypeDto dto) throws Exception {
        if(dto!=null){
            PropertyType myT = converter.toItem(dto);
            PropertyType t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                PropertyTypeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  propertyType")
    @PutMapping("")
    public ResponseEntity<PropertyTypeDto> update(@RequestBody PropertyTypeDto dto) throws Exception {
        ResponseEntity<PropertyTypeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            PropertyType t = service.findById(dto.getId());
            converter.copy(dto,t);
            PropertyType updated = service.update(t);
            PropertyTypeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of propertyType")
    @PostMapping("multiple")
    public ResponseEntity<List<PropertyTypeDto>> delete(@RequestBody List<PropertyTypeDto> dtos) throws Exception {
        ResponseEntity<List<PropertyTypeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<PropertyType> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified propertyType")
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


    @Operation(summary = "Finds a propertyType and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<PropertyTypeDto> findWithAssociatedLists(@PathVariable Long id) {
        PropertyType loaded =  service.findWithAssociatedLists(id);
        PropertyTypeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds propertyTypes by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<PropertyTypeDto>> findByCriteria(@RequestBody PropertyTypeCriteria criteria) throws Exception {
        ResponseEntity<List<PropertyTypeDto>> res = null;
        List<PropertyType> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PropertyTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated propertyTypes by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody PropertyTypeCriteria criteria) throws Exception {
        List<PropertyType> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<PropertyTypeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets propertyType data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody PropertyTypeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<PropertyTypeDto> findDtos(List<PropertyType> list){
        List<PropertyTypeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<PropertyTypeDto> getDtoResponseEntity(PropertyTypeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public PropertyTypeRestAdmin(PropertyTypeAdminService service, PropertyTypeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final PropertyTypeAdminService service;
    private final PropertyTypeConverter converter;





}

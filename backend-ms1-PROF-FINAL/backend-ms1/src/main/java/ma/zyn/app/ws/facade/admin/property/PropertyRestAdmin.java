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

import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;
import ma.zyn.app.service.facade.admin.property.PropertyAdminService;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.ws.dto.property.PropertyDto;
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
@RequestMapping("/api/admin/property/")
public class PropertyRestAdmin {




    @Operation(summary = "Finds a list of all propertys")
    @GetMapping("")
    public ResponseEntity<List<PropertyDto>> findAll() throws Exception {
        ResponseEntity<List<PropertyDto>> res = null;
        List<Property> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<PropertyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all propertys")
    @GetMapping("optimized")
    public ResponseEntity<List<PropertyDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<PropertyDto>> res = null;
        List<Property> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<PropertyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a property by id")
    @GetMapping("id/{id}")
    public ResponseEntity<PropertyDto> findById(@PathVariable Long id) {
        Property t = service.findById(id);
        if (t != null) {
            converter.init(true);
            PropertyDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a property by name")
    @GetMapping("name/{name}")
    public ResponseEntity<PropertyDto> findByName(@PathVariable String name) {
	    Property t = service.findByReferenceEntity(new Property(name));
        if (t != null) {
            converter.init(true);
            PropertyDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  property")
    @PostMapping("")
    public ResponseEntity<PropertyDto> save(@RequestBody PropertyDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            Property myT = converter.toItem(dto);
            Property t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                PropertyDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  property")
    @PutMapping("")
    public ResponseEntity<PropertyDto> update(@RequestBody PropertyDto dto) throws Exception {
        ResponseEntity<PropertyDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            Property t = service.findById(dto.getId());
            converter.copy(dto,t);
            Property updated = service.update(t);
            PropertyDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of property")
    @PostMapping("multiple")
    public ResponseEntity<List<PropertyDto>> delete(@RequestBody List<PropertyDto> dtos) throws Exception {
        ResponseEntity<List<PropertyDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<Property> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified property")
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

    @Operation(summary = "find by propertyType code")
    @GetMapping("propertyType/code/{code}")
    public List<PropertyDto> findByPropertyTypeCode(@PathVariable String code){
        return findDtos(service.findByPropertyTypeCode(code));
    }
    @Operation(summary = "delete by propertyType code")
    @DeleteMapping("propertyType/code/{code}")
    public int deleteByPropertyTypeCode(@PathVariable String code){
        return service.deleteByPropertyTypeCode(code);
    }
    @Operation(summary = "find by propertyStatus code")
    @GetMapping("propertyStatus/code/{code}")
    public List<PropertyDto> findByPropertyStatusCode(@PathVariable String code){
        return findDtos(service.findByPropertyStatusCode(code));
    }
    @Operation(summary = "delete by propertyStatus code")
    @DeleteMapping("propertyStatus/code/{code}")
    public int deleteByPropertyStatusCode(@PathVariable String code){
        return service.deleteByPropertyStatusCode(code);
    }
    @Operation(summary = "find by city id")
    @GetMapping("city/id/{id}")
    public List<PropertyDto> findByCityId(@PathVariable Long id){
        return findDtos(service.findByCityId(id));
    }
    @Operation(summary = "delete by city id")
    @DeleteMapping("city/id/{id}")
    public int deleteByCityId(@PathVariable Long id){
        return service.deleteByCityId(id);
    }
    @Operation(summary = "find by enterprise id")
    @GetMapping("enterprise/id/{id}")
    public List<PropertyDto> findByEnterpriseId(@PathVariable Long id){
        return findDtos(service.findByEnterpriseId(id));
    }
    @Operation(summary = "delete by enterprise id")
    @DeleteMapping("enterprise/id/{id}")
    public int deleteByEnterpriseId(@PathVariable Long id){
        return service.deleteByEnterpriseId(id);
    }

    @Operation(summary = "Finds a property and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<PropertyDto> findWithAssociatedLists(@PathVariable Long id) {
        Property loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        PropertyDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds propertys by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<PropertyDto>> findByCriteria(@RequestBody PropertyCriteria criteria) throws Exception {
        ResponseEntity<List<PropertyDto>> res = null;
        List<Property> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<PropertyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated propertys by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody PropertyCriteria criteria) throws Exception {
        List<Property> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<PropertyDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets property data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody PropertyCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<PropertyDto> findDtos(List<Property> list){
        converter.initList(false);
        converter.initObject(true);
        List<PropertyDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<PropertyDto> getDtoResponseEntity(PropertyDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public PropertyRestAdmin(PropertyAdminService service, PropertyConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final PropertyAdminService service;
    private final PropertyConverter converter;





}

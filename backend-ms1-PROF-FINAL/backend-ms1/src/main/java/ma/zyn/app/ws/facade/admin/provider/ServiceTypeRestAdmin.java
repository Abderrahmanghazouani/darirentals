package  ma.zyn.app.ws.facade.admin.provider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.dao.criteria.core.provider.ServiceTypeCriteria;
import ma.zyn.app.service.facade.admin.provider.ServiceTypeAdminService;
import ma.zyn.app.ws.converter.provider.ServiceTypeConverter;
import ma.zyn.app.ws.dto.provider.ServiceTypeDto;
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
@RequestMapping("/api/admin/serviceType/")
public class ServiceTypeRestAdmin {




    @Operation(summary = "Finds a list of all serviceTypes")
    @GetMapping("")
    public ResponseEntity<List<ServiceTypeDto>> findAll() throws Exception {
        ResponseEntity<List<ServiceTypeDto>> res = null;
        List<ServiceType> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ServiceTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all serviceTypes")
    @GetMapping("optimized")
    public ResponseEntity<List<ServiceTypeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ServiceTypeDto>> res = null;
        List<ServiceType> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ServiceTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a serviceType by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ServiceTypeDto> findById(@PathVariable Long id) {
        ServiceType t = service.findById(id);
        if (t != null) {
            ServiceTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a serviceType by label")
    @GetMapping("label/{label}")
    public ResponseEntity<ServiceTypeDto> findByLabel(@PathVariable String label) {
	    ServiceType t = service.findByReferenceEntity(new ServiceType(label));
        if (t != null) {
            ServiceTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  serviceType")
    @PostMapping("")
    public ResponseEntity<ServiceTypeDto> save(@RequestBody ServiceTypeDto dto) throws Exception {
        if(dto!=null){
            ServiceType myT = converter.toItem(dto);
            ServiceType t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ServiceTypeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  serviceType")
    @PutMapping("")
    public ResponseEntity<ServiceTypeDto> update(@RequestBody ServiceTypeDto dto) throws Exception {
        ResponseEntity<ServiceTypeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ServiceType t = service.findById(dto.getId());
            converter.copy(dto,t);
            ServiceType updated = service.update(t);
            ServiceTypeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of serviceType")
    @PostMapping("multiple")
    public ResponseEntity<List<ServiceTypeDto>> delete(@RequestBody List<ServiceTypeDto> dtos) throws Exception {
        ResponseEntity<List<ServiceTypeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<ServiceType> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified serviceType")
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


    @Operation(summary = "Finds a serviceType and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ServiceTypeDto> findWithAssociatedLists(@PathVariable Long id) {
        ServiceType loaded =  service.findWithAssociatedLists(id);
        ServiceTypeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds serviceTypes by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ServiceTypeDto>> findByCriteria(@RequestBody ServiceTypeCriteria criteria) throws Exception {
        ResponseEntity<List<ServiceTypeDto>> res = null;
        List<ServiceType> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ServiceTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated serviceTypes by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ServiceTypeCriteria criteria) throws Exception {
        List<ServiceType> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<ServiceTypeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets serviceType data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ServiceTypeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ServiceTypeDto> findDtos(List<ServiceType> list){
        List<ServiceTypeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ServiceTypeDto> getDtoResponseEntity(ServiceTypeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ServiceTypeRestAdmin(ServiceTypeAdminService service, ServiceTypeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ServiceTypeAdminService service;
    private final ServiceTypeConverter converter;





}

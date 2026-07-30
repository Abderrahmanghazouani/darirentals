package  ma.zyn.app.ws.facade.collaborator.provider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.dao.criteria.core.provider.ServiceProviderCriteria;
import ma.zyn.app.service.facade.collaborator.provider.ServiceProviderCollaboratorService;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
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
@RequestMapping("/api/collaborator/serviceProvider/")
public class ServiceProviderRestCollaborator {




    @Operation(summary = "Finds a list of all serviceProviders")
    @GetMapping("")
    public ResponseEntity<List<ServiceProviderDto>> findAll() throws Exception {
        ResponseEntity<List<ServiceProviderDto>> res = null;
        List<ServiceProvider> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<ServiceProviderDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all serviceProviders")
    @GetMapping("optimized")
    public ResponseEntity<List<ServiceProviderDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ServiceProviderDto>> res = null;
        List<ServiceProvider> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<ServiceProviderDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a serviceProvider by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ServiceProviderDto> findById(@PathVariable Long id) {
        ServiceProvider t = service.findById(id);
        if (t != null) {
            converter.init(true);
            ServiceProviderDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a serviceProvider by name")
    @GetMapping("name/{name}")
    public ResponseEntity<ServiceProviderDto> findByName(@PathVariable String name) {
	    ServiceProvider t = service.findByReferenceEntity(new ServiceProvider(name));
        if (t != null) {
            converter.init(true);
            ServiceProviderDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  serviceProvider")
    @PostMapping("")
    public ResponseEntity<ServiceProviderDto> save(@RequestBody ServiceProviderDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            ServiceProvider myT = converter.toItem(dto);
            ServiceProvider t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ServiceProviderDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  serviceProvider")
    @PutMapping("")
    public ResponseEntity<ServiceProviderDto> update(@RequestBody ServiceProviderDto dto) throws Exception {
        ResponseEntity<ServiceProviderDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ServiceProvider t = service.findById(dto.getId());
            converter.copy(dto,t);
            ServiceProvider updated = service.update(t);
            ServiceProviderDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of serviceProvider")
    @PostMapping("multiple")
    public ResponseEntity<List<ServiceProviderDto>> delete(@RequestBody List<ServiceProviderDto> dtos) throws Exception {
        ResponseEntity<List<ServiceProviderDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<ServiceProvider> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified serviceProvider")
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

    @Operation(summary = "find by serviceType code")
    @GetMapping("serviceType/code/{code}")
    public List<ServiceProviderDto> findByServiceTypeCode(@PathVariable String code){
        return findDtos(service.findByServiceTypeCode(code));
    }
    @Operation(summary = "delete by serviceType code")
    @DeleteMapping("serviceType/code/{code}")
    public int deleteByServiceTypeCode(@PathVariable String code){
        return service.deleteByServiceTypeCode(code);
    }
    @Operation(summary = "find by enterprise id")
    @GetMapping("enterprise/id/{id}")
    public List<ServiceProviderDto> findByEnterpriseId(@PathVariable Long id){
        return findDtos(service.findByEnterpriseId(id));
    }
    @Operation(summary = "delete by enterprise id")
    @DeleteMapping("enterprise/id/{id}")
    public int deleteByEnterpriseId(@PathVariable Long id){
        return service.deleteByEnterpriseId(id);
    }

    @Operation(summary = "Finds a serviceProvider and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ServiceProviderDto> findWithAssociatedLists(@PathVariable Long id) {
        ServiceProvider loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        ServiceProviderDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds serviceProviders by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ServiceProviderDto>> findByCriteria(@RequestBody ServiceProviderCriteria criteria) throws Exception {
        ResponseEntity<List<ServiceProviderDto>> res = null;
        List<ServiceProvider> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<ServiceProviderDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated serviceProviders by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ServiceProviderCriteria criteria) throws Exception {
        List<ServiceProvider> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<ServiceProviderDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets serviceProvider data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ServiceProviderCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ServiceProviderDto> findDtos(List<ServiceProvider> list){
        converter.initList(false);
        converter.initObject(true);
        List<ServiceProviderDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ServiceProviderDto> getDtoResponseEntity(ServiceProviderDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ServiceProviderRestCollaborator(ServiceProviderCollaboratorService service, ServiceProviderConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ServiceProviderCollaboratorService service;
    private final ServiceProviderConverter converter;





}

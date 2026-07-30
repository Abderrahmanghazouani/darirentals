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

import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.dao.criteria.core.property.CityCriteria;
import ma.zyn.app.service.facade.admin.property.CityAdminService;
import ma.zyn.app.ws.converter.property.CityConverter;
import ma.zyn.app.ws.dto.property.CityDto;
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
@RequestMapping("/api/admin/city/")
public class CityRestAdmin {




    @Operation(summary = "Finds a list of all citys")
    @GetMapping("")
    public ResponseEntity<List<CityDto>> findAll() throws Exception {
        ResponseEntity<List<CityDto>> res = null;
        List<City> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<CityDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all citys")
    @GetMapping("optimized")
    public ResponseEntity<List<CityDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<CityDto>> res = null;
        List<City> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<CityDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a city by id")
    @GetMapping("id/{id}")
    public ResponseEntity<CityDto> findById(@PathVariable Long id) {
        City t = service.findById(id);
        if (t != null) {
            converter.init(true);
            CityDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a city by name")
    @GetMapping("name/{name}")
    public ResponseEntity<CityDto> findByName(@PathVariable String name) {
	    City t = service.findByReferenceEntity(new City(name));
        if (t != null) {
            converter.init(true);
            CityDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  city")
    @PostMapping("")
    public ResponseEntity<CityDto> save(@RequestBody CityDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            City myT = converter.toItem(dto);
            City t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                CityDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  city")
    @PutMapping("")
    public ResponseEntity<CityDto> update(@RequestBody CityDto dto) throws Exception {
        ResponseEntity<CityDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            City t = service.findById(dto.getId());
            converter.copy(dto,t);
            City updated = service.update(t);
            CityDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of city")
    @PostMapping("multiple")
    public ResponseEntity<List<CityDto>> delete(@RequestBody List<CityDto> dtos) throws Exception {
        ResponseEntity<List<CityDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<City> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified city")
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


    @Operation(summary = "Finds a city and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<CityDto> findWithAssociatedLists(@PathVariable Long id) {
        City loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        CityDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds citys by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<CityDto>> findByCriteria(@RequestBody CityCriteria criteria) throws Exception {
        ResponseEntity<List<CityDto>> res = null;
        List<City> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<CityDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated citys by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody CityCriteria criteria) throws Exception {
        List<City> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<CityDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets city data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody CityCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<CityDto> findDtos(List<City> list){
        converter.initList(false);
        converter.initObject(true);
        List<CityDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<CityDto> getDtoResponseEntity(CityDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public CityRestAdmin(CityAdminService service, CityConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final CityAdminService service;
    private final CityConverter converter;





}

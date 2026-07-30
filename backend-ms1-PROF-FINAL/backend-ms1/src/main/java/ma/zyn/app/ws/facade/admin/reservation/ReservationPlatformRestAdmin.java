package  ma.zyn.app.ws.facade.admin.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.dao.criteria.core.reservation.ReservationPlatformCriteria;
import ma.zyn.app.service.facade.admin.reservation.ReservationPlatformAdminService;
import ma.zyn.app.ws.converter.reservation.ReservationPlatformConverter;
import ma.zyn.app.ws.dto.reservation.ReservationPlatformDto;
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
@RequestMapping("/api/admin/reservationPlatform/")
public class ReservationPlatformRestAdmin {




    @Operation(summary = "Finds a list of all reservationPlatforms")
    @GetMapping("")
    public ResponseEntity<List<ReservationPlatformDto>> findAll() throws Exception {
        ResponseEntity<List<ReservationPlatformDto>> res = null;
        List<ReservationPlatform> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationPlatformDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all reservationPlatforms")
    @GetMapping("optimized")
    public ResponseEntity<List<ReservationPlatformDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ReservationPlatformDto>> res = null;
        List<ReservationPlatform> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationPlatformDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a reservationPlatform by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ReservationPlatformDto> findById(@PathVariable Long id) {
        ReservationPlatform t = service.findById(id);
        if (t != null) {
            ReservationPlatformDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a reservationPlatform by label")
    @GetMapping("label/{label}")
    public ResponseEntity<ReservationPlatformDto> findByLabel(@PathVariable String label) {
	    ReservationPlatform t = service.findByReferenceEntity(new ReservationPlatform(label));
        if (t != null) {
            ReservationPlatformDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  reservationPlatform")
    @PostMapping("")
    public ResponseEntity<ReservationPlatformDto> save(@RequestBody ReservationPlatformDto dto) throws Exception {
        if(dto!=null){
            ReservationPlatform myT = converter.toItem(dto);
            ReservationPlatform t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ReservationPlatformDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  reservationPlatform")
    @PutMapping("")
    public ResponseEntity<ReservationPlatformDto> update(@RequestBody ReservationPlatformDto dto) throws Exception {
        ResponseEntity<ReservationPlatformDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ReservationPlatform t = service.findById(dto.getId());
            converter.copy(dto,t);
            ReservationPlatform updated = service.update(t);
            ReservationPlatformDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of reservationPlatform")
    @PostMapping("multiple")
    public ResponseEntity<List<ReservationPlatformDto>> delete(@RequestBody List<ReservationPlatformDto> dtos) throws Exception {
        ResponseEntity<List<ReservationPlatformDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<ReservationPlatform> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified reservationPlatform")
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


    @Operation(summary = "Finds a reservationPlatform and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ReservationPlatformDto> findWithAssociatedLists(@PathVariable Long id) {
        ReservationPlatform loaded =  service.findWithAssociatedLists(id);
        ReservationPlatformDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds reservationPlatforms by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ReservationPlatformDto>> findByCriteria(@RequestBody ReservationPlatformCriteria criteria) throws Exception {
        ResponseEntity<List<ReservationPlatformDto>> res = null;
        List<ReservationPlatform> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationPlatformDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated reservationPlatforms by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ReservationPlatformCriteria criteria) throws Exception {
        List<ReservationPlatform> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<ReservationPlatformDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets reservationPlatform data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ReservationPlatformCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ReservationPlatformDto> findDtos(List<ReservationPlatform> list){
        List<ReservationPlatformDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ReservationPlatformDto> getDtoResponseEntity(ReservationPlatformDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ReservationPlatformRestAdmin(ReservationPlatformAdminService service, ReservationPlatformConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ReservationPlatformAdminService service;
    private final ReservationPlatformConverter converter;





}

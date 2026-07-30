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

import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.dao.criteria.core.reservation.ReservationCriteria;
import ma.zyn.app.service.facade.admin.reservation.ReservationAdminService;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
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
@RequestMapping("/api/admin/reservation/")
public class ReservationRestAdmin {




    @Operation(summary = "Finds a list of all reservations")
    @GetMapping("")
    public ResponseEntity<List<ReservationDto>> findAll() throws Exception {
        ResponseEntity<List<ReservationDto>> res = null;
        List<Reservation> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<ReservationDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all reservations")
    @GetMapping("optimized")
    public ResponseEntity<List<ReservationDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ReservationDto>> res = null;
        List<Reservation> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<ReservationDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a reservation by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ReservationDto> findById(@PathVariable Long id) {
        Reservation t = service.findById(id);
        if (t != null) {
            converter.init(true);
            ReservationDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a reservation by reference")
    @GetMapping("reference/{reference}")
    public ResponseEntity<ReservationDto> findByReference(@PathVariable String reference) {
	    Reservation t = service.findByReferenceEntity(new Reservation(reference));
        if (t != null) {
            converter.init(true);
            ReservationDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  reservation")
    @PostMapping("")
    public ResponseEntity<ReservationDto> save(@RequestBody ReservationDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            Reservation myT = converter.toItem(dto);
            Reservation t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ReservationDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  reservation")
    @PutMapping("")
    public ResponseEntity<ReservationDto> update(@RequestBody ReservationDto dto) throws Exception {
        ResponseEntity<ReservationDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            Reservation t = service.findById(dto.getId());
            converter.copy(dto,t);
            Reservation updated = service.update(t);
            ReservationDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of reservation")
    @PostMapping("multiple")
    public ResponseEntity<List<ReservationDto>> delete(@RequestBody List<ReservationDto> dtos) throws Exception {
        ResponseEntity<List<ReservationDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<Reservation> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified reservation")
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


    @Operation(summary = "Finds a reservation and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ReservationDto> findWithAssociatedLists(@PathVariable Long id) {
        Reservation loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        ReservationDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds reservations by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ReservationDto>> findByCriteria(@RequestBody ReservationCriteria criteria) throws Exception {
        ResponseEntity<List<ReservationDto>> res = null;
        List<Reservation> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<ReservationDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated reservations by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ReservationCriteria criteria) throws Exception {
        List<Reservation> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<ReservationDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets reservation data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ReservationCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ReservationDto> findDtos(List<Reservation> list){
        converter.initList(false);
        converter.initObject(true);
        List<ReservationDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ReservationDto> getDtoResponseEntity(ReservationDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ReservationRestAdmin(ReservationAdminService service, ReservationConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ReservationAdminService service;
    private final ReservationConverter converter;





}

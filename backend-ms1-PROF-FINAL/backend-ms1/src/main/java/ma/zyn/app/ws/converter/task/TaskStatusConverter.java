package  ma.zyn.app.ws.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.ws.dto.task.TaskStatusDto;

@Component
public class TaskStatusConverter {



    public TaskStatus toItem(TaskStatusDto dto) {
        if (dto == null) {
            return null;
        } else {
        TaskStatus item = new TaskStatus();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getDescription()))
                item.setDescription(dto.getDescription());
            if(StringUtil.isNotEmpty(dto.getCode()))
                item.setCode(dto.getCode());
            if(StringUtil.isNotEmpty(dto.getLabel()))
                item.setLabel(dto.getLabel());
            if(StringUtil.isNotEmpty(dto.getStyle()))
                item.setStyle(dto.getStyle());
            if(dto.getIsDefault() != null)
                item.setIsDefault(dto.getIsDefault());
            if(StringUtil.isNotEmpty(dto.getSortOrder()))
                item.setSortOrder(dto.getSortOrder());



        return item;
        }
    }


    public TaskStatusDto toDto(TaskStatus item) {
        if (item == null) {
            return null;
        } else {
            TaskStatusDto dto = new TaskStatusDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getDescription()))
                dto.setDescription(item.getDescription());
            if(StringUtil.isNotEmpty(item.getCode()))
                dto.setCode(item.getCode());
            if(StringUtil.isNotEmpty(item.getLabel()))
                dto.setLabel(item.getLabel());
            if(StringUtil.isNotEmpty(item.getStyle()))
                dto.setStyle(item.getStyle());
                dto.setIsDefault(item.getIsDefault());
            if(StringUtil.isNotEmpty(item.getSortOrder()))
                dto.setSortOrder(item.getSortOrder());


        return dto;
        }
    }


	
    public List<TaskStatus> toItem(List<TaskStatusDto> dtos) {
        List<TaskStatus> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (TaskStatusDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<TaskStatusDto> toDto(List<TaskStatus> items) {
        List<TaskStatusDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (TaskStatus item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(TaskStatusDto dto, TaskStatus t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<TaskStatus> copy(List<TaskStatusDto> dtos) {
        List<TaskStatus> result = new ArrayList<>();
        if (dtos != null) {
            for (TaskStatusDto dto : dtos) {
                TaskStatus instance = new TaskStatus();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}

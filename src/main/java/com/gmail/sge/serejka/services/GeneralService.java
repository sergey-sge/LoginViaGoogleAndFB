package com.gmail.sge.serejka.services;

import com.gmail.sge.serejka.dto.AccountDTO;
import com.gmail.sge.serejka.dto.TaskDTO;
import com.gmail.sge.serejka.dto.TaskToNotifyDTO;
import org.springframework.data.domain.Pageable;


import java.util.Date;
import java.util.List;

public interface GeneralService {
    void addAccount(AccountDTO accountDTO, List<TaskDTO> tasks);
    void addTask(String email, TaskDTO taskDTO);
    List<TaskDTO> getTasks(String email, Pageable pageable);
    List<TaskToNotifyDTO> getTasksToNotify(Date now);
    Long count(String email);
    void delete(List<Long> idList);
}

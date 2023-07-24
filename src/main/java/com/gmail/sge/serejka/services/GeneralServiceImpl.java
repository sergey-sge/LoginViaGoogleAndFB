package com.gmail.sge.serejka.services;

import com.gmail.sge.serejka.dto.AccountDTO;
import com.gmail.sge.serejka.dto.TaskDTO;
import com.gmail.sge.serejka.dto.TaskToNotifyDTO;
import com.gmail.sge.serejka.model.Account;
import com.gmail.sge.serejka.model.Task;
import com.gmail.sge.serejka.repos.AccountRepository;
import com.gmail.sge.serejka.repos.TaskRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class GeneralServiceImpl implements GeneralService {

    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;

    public GeneralServiceImpl(AccountRepository accountRepository, TaskRepository taskRepository) {
        this.accountRepository = accountRepository;
        this.taskRepository = taskRepository;
    }

    // DB -> E(20) -> R -> S <-> DTO -> C -> V(5) / JSON

    @Transactional
    @Override
    public void addAccount(AccountDTO accountDTO, List<TaskDTO> tasks) {
        if (accountRepository.existsByEmail(accountDTO.getEmail()))
            return; // do nothing

        Account account = Account.fromDTO(accountDTO);

        tasks.forEach((x) -> {
            Task task = Task.fromDTO(x);
            account.addTask(task);
        });

        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void addTask(String email, TaskDTO taskDTO) {
        Account account = accountRepository.findByEmail(email);
        Task task = Task.fromDTO(taskDTO);

        account.addTask(task);

        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskDTO> getTasks(String email, Pageable pageable) {
        List<TaskDTO> result = new ArrayList<>();
        List<Task> tasks = taskRepository.findByAccountEmail(email, pageable);

        tasks.forEach((x) -> result.add(x.toDTO()));
        return result;
    }

    /*

    19:57:31
    [19:57:00; 19:58:00)

     */

    @Transactional(readOnly = true)
    @Override
    public List<TaskToNotifyDTO> getTasksToNotify(Date now) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(now);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date from = calendar.getTime();

        calendar.add(Calendar.MINUTE, 1);
        Date to = calendar.getTime();

        return taskRepository.findTasksToNotify(from, to);
    }

    @Transactional(readOnly = true)
    @Override
    public Long count(String email) {
        return taskRepository.countByAccountEmail(email);
    }

    @Transactional
    @Override
    public void delete(List<Long> idList) {
        idList.forEach((x) -> taskRepository.deleteById(x));
    }
}

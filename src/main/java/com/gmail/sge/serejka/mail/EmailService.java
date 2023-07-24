package com.gmail.sge.serejka.mail;

import com.gmail.sge.serejka.dto.TaskToNotifyDTO;

public interface EmailService {
    void sendMessage(TaskToNotifyDTO task);
}

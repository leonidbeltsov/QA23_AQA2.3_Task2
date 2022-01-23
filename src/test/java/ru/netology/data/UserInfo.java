package ru.netology.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfo {
    private String login;
    private String password;
    private String status;
}

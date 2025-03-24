package ru.netology.web.data;

import lombok.Value;

@Value
public class UserInfo {
    String login;
    String password;
}
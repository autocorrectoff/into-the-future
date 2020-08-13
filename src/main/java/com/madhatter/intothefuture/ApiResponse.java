package com.madhatter.intothefuture;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ApiResponse {
    private int userId;
    private int id;
    private String title;
    private boolean completed;
}

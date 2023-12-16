package com.olamiredev.accelepay.model;

import com.olamiredev.accelepay.enums.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseModel{

    private String fullName;

    private UserType userType;

    private String email;

    private String phoneNumber;

    private String apiKey;

}

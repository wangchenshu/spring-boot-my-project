package com.myproject.myproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fb_users")
public class FbUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String messengerUserId;
    private String firstName;
    private String lastName;
    private String gender;
    private String profilePicUrl;
    private int timezone;
    private String locale;
}

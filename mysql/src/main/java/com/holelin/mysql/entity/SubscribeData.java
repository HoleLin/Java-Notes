package com.holelin.mysql.entity;

import javax.persistence.*;

@Table(name = "subscribe_data")
@Entity
public class SubscribeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private Integer number;
}

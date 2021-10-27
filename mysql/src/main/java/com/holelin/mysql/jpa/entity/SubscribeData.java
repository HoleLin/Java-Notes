package com.holelin.mysql.jpa.entity;

import javax.persistence.*;

@Table(catalog = "subscribe_data")
@Entity
public class SubscribeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private Integer number;
}

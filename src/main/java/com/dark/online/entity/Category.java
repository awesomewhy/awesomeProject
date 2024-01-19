package com.dark.online.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "type_id")
//    private Type type;
//
//    @ManyToOne
//    @JoinColumn(name = "kind_id")
//    private Kind kind;
//
//    @ManyToOne
//    @JoinColumn(name = "group_id")
//    private Group group;
}
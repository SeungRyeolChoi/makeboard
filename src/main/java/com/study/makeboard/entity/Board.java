package com.study.makeboard.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //class가 db에 있는 table을 의미
@Data

public class Board {//entity class 이름은 table이름과 일치하는 것이 좋다
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;

    private String filename;

    private String filepath;

}

package com.dlisaev.cropper.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Crop crop;

    @Column(nullable = false)
    private Boolean typeOffer; // false - продажа, true - покупка

    @Column(nullable = true)
    private Integer volume;

    @Column(nullable = true)
    private Integer pricePerTon; //в рублях

    @Column(columnDefinition = "text")
    private String info;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime createDate;

    public Offer() {
    }

    @PrePersist
    protected void onCreate(){
        this.createDate = LocalDateTime.now();
    }
}

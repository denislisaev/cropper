package com.dlisaev.cropper.entity;

import com.dlisaev.cropper.annotations.OnlyPositiveConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @OnlyPositiveConstraint
    private Integer volume;

    @Column(nullable = true)
    @OnlyPositiveConstraint
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

package com.dlisaev.cropper.entity;

import com.dlisaev.cropper.entity.enums.Crops;
import com.dlisaev.cropper.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean typeOffer; // false - продажа, true - покупка

    @Column(nullable = true)
    private String location;

    @Column(columnDefinition = "text")
    private String info;

    @Column(nullable = true)
    private Integer volume;

    @Column(nullable = true)
    private Integer pricePerTon; //в рублях

    @Enumerated(EnumType.STRING)
    private Crops crop;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Offer() {
    }

    @PrePersist
    protected void onCreate(){
        this.createDate = LocalDateTime.now();
    }
}

package com.triple.mileage.tripleproject.mileage.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewId;
    private String placeId;
    private String userId;
    private String content;
    private String photoIds;
    private String delYn;

}

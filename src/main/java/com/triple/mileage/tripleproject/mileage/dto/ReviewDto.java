package com.triple.mileage.tripleproject.mileage.dto;


import com.triple.mileage.tripleproject.mileage.domain.Review;
import lombok.Builder;
import lombok.Data;

@Data
public class ReviewDto {
    String[] photoId;
    String reviewContent;
    String reviewId;
    String userId;
    String placeId;
    String delYn;

}

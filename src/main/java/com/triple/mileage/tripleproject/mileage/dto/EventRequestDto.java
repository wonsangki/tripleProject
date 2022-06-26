package com.triple.mileage.tripleproject.mileage.dto;

import com.triple.mileage.tripleproject.mileage.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {
    String type;
    String action;
    String reviewId;
    String content;
    String[] attachedPhotoIds;
    String userId;
    String placeId;

}

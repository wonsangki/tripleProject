package com.triple.mileage.tripleproject.mileage.repository;

import com.triple.mileage.tripleproject.mileage.domain.Review;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 해당 리뷰가 작성된 적 있는지
    Review findByReviewIdAndDelYn(String reviewId, String delYn);

    Review findByReviewId(String reviewId);

    //
    List<Review> findAllByUserIdAndDelYn(String userId, String delYn);

    int countByPlaceIdAndDelYn(String placeId, String delYn);
    @Transactional
    @Modifying
    @Query("Update Review r Set r.content = :content, r.photoIds = :photoIds where r.id = :id")
    int updateReview(Long id, String content, String photoIds);
    @Transactional
    @Modifying
    @Query("Update Review r Set r.delYn = 'Y' where r.id = :id")
    int deleteReview(Long id);

}
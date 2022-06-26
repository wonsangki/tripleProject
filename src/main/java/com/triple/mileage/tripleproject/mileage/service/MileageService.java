package com.triple.mileage.tripleproject.mileage.service;

import com.triple.mileage.tripleproject.mileage.domain.PointHistory;
import com.triple.mileage.tripleproject.mileage.domain.Review;
import com.triple.mileage.tripleproject.mileage.domain.User;
import com.triple.mileage.tripleproject.mileage.dto.EventRequestDto;
import com.triple.mileage.tripleproject.mileage.dto.UserDto;
import com.triple.mileage.tripleproject.mileage.repository.PointHistoryRepository;
import com.triple.mileage.tripleproject.mileage.repository.ReviewRepository;
import com.triple.mileage.tripleproject.mileage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributesException;
import java.util.Arrays;

@Service
public class MileageService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;


    public void setMileageEvent(EventRequestDto request){
        switch(request.getAction()){
            case "ADD":
                addReview(request);
                break;
            case "MOD":
                modReview(request);
                break;
            case "DELETE":
                deleteReview(request);
                break;
        }
    }

    private void addReview(EventRequestDto request){
        if(!hasReview(request.getReviewId())){
            Review review = Review.builder()
                    .reviewId(request.getReviewId())
                    .userId(request.getUserId())
                    .content(request.getContent())
                    .placeId(request.getPlaceId())
                    .photoIds(Arrays.toString(request.getAttachedPhotoIds()))
                    .delYn("N")
                    .build();


            int curPoint = calcPoint(request.getContent(),request.getAttachedPhotoIds());
            // 포인트 히스토리
            curPoint += isFirstReview(request.getPlaceId());

            PointHistory ph = PointHistory.builder().
                    changedPoint(curPoint)
                    .userId(review.getUserId())
                    .build();

            pointHistoryRepository.save(ph);

            reviewRepository.save(review);

            // 유저 포인트 가감
            savePoint(request.getUserId(), curPoint);

        } else {
            throw new IllegalArgumentException("요청한 Request 의 Review ID는 이미 등록된 ID 입니다.");
        }
    }

    private void modReview(EventRequestDto request){
        if(hasReview(request.getReviewId())){
            Review review = reviewRepository.findByReviewIdAndDelYn(request.getReviewId(),"N");

            int prevPoint = calcPoint(review.getContent(), review.getPhotoIds().split(","));
            reviewRepository.updateReview(review.getId(), request.getContent(), Arrays.toString(request.getAttachedPhotoIds()));
            int newPoint = calcPoint(request.getContent(), request.getAttachedPhotoIds());

            if(newPoint!=prevPoint) {
                PointHistory ph = PointHistory.builder()
                        .changedPoint(newPoint-prevPoint)
                        .userId(review.getUserId())
                        .build();

                pointHistoryRepository.save(ph);

                User user = userRepository.findByUserId(ph.getUserId());

                userRepository.updateUserPoint(user.getId(), user.getPoint() + newPoint - prevPoint);

            }

        } else {
            throw new IllegalArgumentException("요청한 Request 의 Review ID는 등록되지 않은 ID 입니다.");
        }
    }
    private void deleteReview(EventRequestDto request){
        if(hasReview(request.getReviewId())){
            Review review = reviewRepository.findByReviewIdAndDelYn(request.getReviewId(), "N");

            User user = userRepository.findByUserId(review.getUserId());

            userRepository.updateUserPoint(user.getId(), user.getPoint() - calcPoint(review.getContent(), review.getPhotoIds().split(",")));

            reviewRepository.deleteReview(review.getId());
        } else {
            throw new IllegalArgumentException("요청한 Request 의 Review ID는 등록되지 않은 ID 입니다.");
        }
    }

    /**
     * review Id 중복 체크
     * @return
     */
    private boolean hasReview(String reviewId) {
        // 여기선 디비 조회해서 이미 작성된 review Id 인지 체크한다.
        if (reviewRepository.findByReviewIdAndDelYn(reviewId, "N") == null){
            return false;
        }

        return true;
    }

    /**
     * 리뷰 내용과 사진 id 사이즈로 점수 계산
     * @param content
     * @param photoIds
     * @return
     */
    private int calcPoint(String content, String[] photoIds) {
        int point = 0;

        System.out.println(photoIds);

        if(content.length() > 0) point++;
        if(photoIds.length > 0) point++;

        System.out.println(point);

        return point;
    }

    /*
        대충 작성 필요한 메소드

        1. 공통
            1.1. 리뷰 받아서 점수 계산하는 메소드
            2.1. 해당 리뷰가 특정 장소의 첫번째 리뷰인지 검사
        2. 추가
            2.1. 해당 리뷰가 특정 장소의 첫번째 리뷰인지 검사
        3. 수정
            3.1.
        4. 삭제

     */

    private int isFirstReview(String placeId){
        // place 로 카운트해서 없으면 첫 리뷰
        System.out.println(reviewRepository.countByPlaceIdAndDelYn(placeId, "N"));
        if(reviewRepository.countByPlaceIdAndDelYn(placeId, "N") == 0) {
            System.out.println("this is first");
            return 1;
        }
        System.out.println("is not first");
        return 0;
    }

    private void savePoint(String userId, int point){
        User user = userRepository.findByUserId(userId);
        if(user == null){
            User newUser = User.builder()
                    .userId(userId)
                    .point(point)
                    .build();

            userRepository.save(newUser);
        } else {
            userRepository.updateUserPoint(user.getId(), user.getPoint() + point);
        }
    }

}

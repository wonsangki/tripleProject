package com.triple.mileage.tripleproject.mileage.controller;


import com.triple.mileage.tripleproject.mileage.domain.PointHistory;
import com.triple.mileage.tripleproject.mileage.domain.User;
import com.triple.mileage.tripleproject.mileage.dto.EventRequestDto;
import com.triple.mileage.tripleproject.mileage.repository.PointHistoryRepository;
import com.triple.mileage.tripleproject.mileage.repository.UserRepository;
import com.triple.mileage.tripleproject.mileage.service.MileageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MileageController {

    @Autowired
    MileageService mileageService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @PostMapping("/events")
    public String getEvent(@RequestBody EventRequestDto event){
        System.out.println(event.toString());

        try {
            mileageService.setMileageEvent(event);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }

        return "Good";
    }

    @GetMapping("/events/user")
    public String getUserPoints(){
        StringBuilder sb = new StringBuilder();
        for(User user : userRepository.findAll()){
            System.out.println(user.toString());
            sb.append(user.toString());
        }

        return sb.toString();
    }


    @GetMapping("/events/history")
    public String getPointHistory(){
        StringBuilder sb = new StringBuilder();
        for(PointHistory pH : pointHistoryRepository.findAll()){
            sb.append(pH.toString());
        }

        return sb.toString();
    }
}

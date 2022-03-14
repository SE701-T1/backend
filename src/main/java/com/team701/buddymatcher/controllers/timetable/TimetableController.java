package com.team701.buddymatcher.controllers.timetable;

import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.services.timetable.TimetableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
@RequestMapping("/api/timetable")
public class TimetableController {
    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @ApiOperation("Get a user's Timetable by their id")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Timetable> findTimetable(@RequestParam(name = "u") String userId) {
        Timetable t = timetableService.retrieve("");
        return new ResponseEntity<>(t, HttpStatus.OK);
    }
}


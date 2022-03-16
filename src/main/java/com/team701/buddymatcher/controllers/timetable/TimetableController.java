package com.team701.buddymatcher.controllers.timetable;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.timetable.CourseDTO;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.services.timetable.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Timetable")
@RequestMapping("/api/timetable")
public class TimetableController {
    private final TimetableService timetableService;

    private ModelMapper modelMapper;

    @Autowired
    public TimetableController(TimetableService timetableService, ModelMapper modelMapper) {
        this.timetableService = timetableService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary ="Get a user's Timetable by their id")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Timetable> findTimetable(@RequestParam(name = "u") String userId) {
        Timetable t = timetableService.retrieve("");
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @Operation(summary ="Get method to get all user's courses")
    @GetMapping(path = "/users/courses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getCourses (@PathVariable("id") Long userId) {
        try {
            List<Course> courses = timetableService.getCourses(userId);
            List<CourseDTO> courseDTOs = courses.stream()
                    .map(course -> modelMapper.map(course, CourseDTO.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary ="Get method to get a course using courseId")
    @GetMapping(path = "/courses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> getCourse (@PathVariable("id") Long courseId) {
        try {
            Course course = timetableService.getCourse(courseId);
            CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);

            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
    }

}


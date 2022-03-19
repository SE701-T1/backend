package com.team701.buddymatcher.controllers.timetable;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.dtos.timetable.CourseDTO;
import com.team701.buddymatcher.services.timetable.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Timetable")
@RequestMapping("/api/timetable")
@SessionAttributes("UserId")
@SecurityRequirement(name = "JWT")
public class TimetableController {
    private final TimetableService timetableService;

    private ModelMapper modelMapper;

    @Autowired
    public TimetableController(TimetableService timetableService, ModelMapper modelMapper) {
        this.timetableService = timetableService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary ="Get method to get current user courses")
    @GetMapping(path = "/users/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getSelfCourses (@Parameter(hidden = true) @SessionAttribute("UserId") Long userId) {
        return getCourseListById(userId);
    }

    @Operation(summary ="Get method to get userId user courses")
    @GetMapping(path = "/users/courses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getCourses (@PathVariable("id") Long userId) {
        return getCourseListById(userId);
    }

    private ResponseEntity<List<CourseDTO>> getCourseListById(Long userId) {
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
    @GetMapping(path = "/course/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> getCourse (@PathVariable("id") Long courseId) {
        try {
            Course course = timetableService.getCourse(courseId);
            CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);

            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
    }
  
    @Operation(summary = "Post method to upload a course url for current user")
    @PostMapping(path="users/upload")
    public ResponseEntity<Void> uploadTimetable(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId, @RequestBody String timetableUrl) throws IOException, InterruptedException {
        HttpRequest timetableRequest = HttpRequest.newBuilder()
                .uri(URI.create(timetableUrl))
                .GET()
                .build();
        HttpResponse<String> timetableResponse = HttpClient.newBuilder()
                .build()
                .send(timetableRequest, HttpResponse.BodyHandlers.ofString());
        InputStream timetableStream = new ByteArrayInputStream(timetableResponse.body().getBytes(StandardCharsets.UTF_8));
        List<String> result;
        try {
            result = timetableService.getCalInfoFromIcs(timetableStream);
            timetableService.populateCourses(userId, result);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ParserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Timetable URL is invalid");
        }
    }

}


package com.team701.buddymatcher.controllers.timetable;

import com.team701.buddymatcher.controllers.users.UserController;
import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.dtos.timetable.CourseDTO;
import com.team701.buddymatcher.services.timetable.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.fortuna.ical4j.data.ParserException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Timetable")
@RequestMapping("/api/timetable")
@SessionAttributes("UserId")
@SecurityRequirement(name = "JWT")
public class TimetableController {

    private final TimetableService timetableService;
    private final ModelMapper modelMapper;
    private final UserController userController;

    @Autowired
    public TimetableController(TimetableService timetableService,
                               ModelMapper modelMapper,
                               UserController userController) {
        this.timetableService = timetableService;
        this.modelMapper = modelMapper;
        this.userController = userController;
    }

    /**
     * Returns a Course if course ID is in the database, otherwise throws NoSuchElementException
     */
    private Course isCourseValid(Long courseId) throws NoSuchElementException {
        return timetableService.getCourse(courseId);
    }

    @Operation(summary ="Get method to get current user courses")
    @GetMapping(path = "/users/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getSelfCourses (@Parameter(hidden = true)
                                                           @SessionAttribute("UserId") Long userId) {
        try {
            this.userController.isUserValid(userId);
            return getCourseListById(userId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary ="Get method to get userId user courses")
    @GetMapping(path = "/users/courses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getCourses (@PathVariable("id") Long userId) {
        try {
            this.userController.isUserValid(userId);
            return getCourseListById(userId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private ResponseEntity<List<CourseDTO>> getCourseListById(Long userId) {
        List<Course> courses = this.timetableService.getCourses(userId);
        List<CourseDTO> courseDTOs = courses.stream()
                .map(course -> {
                    // Map the course object to a DTO and then set the buddy count
                    CourseDTO dto = this.modelMapper.map(course, CourseDTO.class);
                    dto.setBuddyCount(Math.toIntExact(
                            this.timetableService.getBuddyCountFromCourse(userId,
                                    course.getCourseId())));
                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
    }

    @Operation(summary ="Get method to get a course using courseId")
    @GetMapping(path = "/course/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> getCourse (@PathVariable("id") Long courseId) {
        try {
            Course course = this.isCourseValid(courseId);
            CourseDTO courseDTO = this.modelMapper.map(course, CourseDTO.class);
            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
    }
  
    @Operation(summary = "Post method to upload a course url for current user")
    @PostMapping(path="users/upload")
    public ResponseEntity<Void> uploadTimetable(@Parameter(hidden = true)
                                                @SessionAttribute("UserId") Long userId,
                                                @RequestBody String timetableUrl) {
        try {
            timetableUrl = URLDecoder.decode(timetableUrl,
                                             StandardCharsets.UTF_8.toString()).replace("=", "");
            HttpRequest timetableRequest = HttpRequest.newBuilder()
                    .uri(URI.create(timetableUrl))
                    .GET()
                    .build();
            HttpResponse<String> timetableResponse = HttpClient.newBuilder()
                    .build()
                    .send(timetableRequest, HttpResponse.BodyHandlers.ofString());
            InputStream timetableStream = new ByteArrayInputStream(timetableResponse.body()
                                                                                    .getBytes(StandardCharsets.UTF_8));
            List<String> result;
            result = this.timetableService.getCalInfoFromIcs(timetableStream);
            this.timetableService.populateCourses(userId, result);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ParserException | IOException | InterruptedException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Timetable URL is invalid");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Delete method to withdraw course courseId from user userId")
    @DeleteMapping(path = "/course/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCourse(@SessionAttribute("UserId") Long userId,
                                             @PathVariable("id") Long courseId) {
        try {
            this.isCourseValid(courseId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        try{
            this.timetableService.deleteCourse(userId, courseId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Post method to upload a course .isc file for the current user")
    @PostMapping(path="users/upload/file")
    public ResponseEntity<Void> uploadTimetableFile(@Parameter(hidden = true)
                                                    @SessionAttribute("UserId") Long userId,
                                                    @RequestParam("file") MultipartFile file) {
        try {
            InputStream timetableStream = new ByteArrayInputStream(file.getBytes());
            List<String> result = this.timetableService.getCalInfoFromIcs(timetableStream);
            this.timetableService.populateCourses(userId, result);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ParserException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Timetable file is invalid");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}

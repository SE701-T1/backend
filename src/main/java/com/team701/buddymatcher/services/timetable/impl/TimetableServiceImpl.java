package com.team701.buddymatcher.services.timetable.impl;

import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.services.timetable.TimetableService;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {
    @Override
    public Timetable retrieve(String userId) {
        Timetable t = new Timetable();
        t.setClassNames(Arrays.asList("SE701", "SE754", "SE751"));
        return t;
    }
    /**
     * This method parses the input .ics file and retrieves the course names from the file.
     * @param file
     * @return A list of string that contains the courses from the ics file.
     */
    @Override
    public List<String> getCalInfoFromIcs(File file) throws Exception {
        Calendar calendar;
        FileInputStream fin = new FileInputStream(file);
        CalendarBuilder builder = new CalendarBuilder();
        calendar = builder.build(fin);
        List<String> result = new ArrayList<>();

        for (Iterator i = calendar.getComponents().iterator(); i.hasNext(); ) {
            Component component = (Component) i.next();

            for (Iterator j = component.getProperties().iterator(); j.hasNext(); ) {
                try {
                    String event;
                    Property property = (Property) j.next();
                    if ("SUMMARY".equals(property.getName())) {
                        event = property.getValue();
                        result.add(event);
                    }

                } catch (Exception e) {

                }
            }
        }
        //Remove the duplicate course names from the calendarInfo list
        List<String> courseList = new ArrayList<>();
        for (String course : result) {
            if (!courseList.contains(course)) {
                courseList.add(course);
            }
        }

        return courseList;
    }

}

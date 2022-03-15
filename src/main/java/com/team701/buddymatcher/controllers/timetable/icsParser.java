package com.team701.buddymatcher.controllers.timetable;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

public class icsParser {
     public static void main(String args[]) throws Exception {
        icsParser parser = new icsParser();
        File file = new File("C:/Users/Daniel/UoACal.ics");
        List<String> result = parser.getCalInfoFromIcs(file);
        System.out.println(result);
    }
    public List<String> getCalInfoFromIcs(File file) throws Exception {
        Calendar calendar = new Calendar();
         /** calendar.getProperties().add(
                new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN); **/

        FileInputStream fin = new FileInputStream(file);
        CalendarBuilder builder = new CalendarBuilder();
        calendar = builder.build(fin);
        List<String> result = new ArrayList<>();

        for (Iterator i = calendar.getComponents().iterator(); i.hasNext(); ) {
            List<String> calenderInfo = new ArrayList<>();
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

package com.team701.buddymatcher.services.timetable;

import com.team701.buddymatcher.domain.timetable.Timetable;

import java.io.File;
import java.util.List;

public interface TimetableService {
    Timetable retrieve(String userId);
    List<String> getCalInfoFromIcs(File file) throws Exception;
}

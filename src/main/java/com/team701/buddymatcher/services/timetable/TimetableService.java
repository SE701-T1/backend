package com.team701.buddymatcher.services.timetable;

import com.team701.buddymatcher.domain.timetable.Timetable;

public interface TimetableService {
    Timetable retrieve(String userId);
}

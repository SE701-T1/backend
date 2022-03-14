package com.team701.buddymatcher.services.timetable.impl;

import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.services.timetable.TimetableService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {
    @Override
    public Timetable retrieve(String userId) {
        Timetable t = new Timetable();
        t.setClassNames(Arrays.asList("SE701", "SE754", "SE751"));
        return t;
    }

}

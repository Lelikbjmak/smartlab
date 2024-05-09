package com.innowise.smartlab.service;

import com.innowise.smartlab.entity.League;
import com.innowise.smartlab.entity.LeagueSchedule;

public interface LeagueScheduleService {

  LeagueSchedule composeSchedule(League league);
}

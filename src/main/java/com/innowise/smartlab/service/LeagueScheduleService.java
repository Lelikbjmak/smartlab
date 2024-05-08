package com.innowise.smartlab.service;

import com.innowise.smartlab.dto.LeagueDto;
import com.innowise.smartlab.dto.MatchDto;
import java.util.List;

public interface LeagueScheduleService {

  List<MatchDto> composeSchedule(LeagueDto league);
}

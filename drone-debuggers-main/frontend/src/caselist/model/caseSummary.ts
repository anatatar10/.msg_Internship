import {ColleagueDTO} from "./case";

export interface CaseSummary{
  systemCaseId: string,
  caseDate: Date,
  flightNumber: string,
  firstPassengerName: string,
  lastPassengerName: string,
  status: string,
  assignedColleague: String,
  assignedColleagueDTO: ColleagueDTO
}


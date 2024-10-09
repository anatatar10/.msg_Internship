import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {
  CaseFormPassengerDetailsComponent
} from "./components/case-form-passenger-details/case-form-passenger-details.component";
import {
  CaseFormReservationDetailsComponent
} from "./components/case-form-reservation-details/case-form-reservation-details.component";
import {CaseFormFlightDetailsComponent} from "./components/case-form-flight-details/case-form-flight-details.component";
import {
  CaseFormDisruptionDetailsComponent
} from "./components/case-form-distruption-details/case-form-disruption-details.component";
import {CaseFormUserDetailsComponent} from "./components/case-form-user-details/case-form-user-details.component";
import {CaseFormWrapperComponent} from "./components/case-form-wrapper/case-form-wrapper.component";
import {
  CaseFormProblemFlightDetailsComponent
} from "./components/case-form-problem-flight-details/case-form-problem-flight-details.component";

const routes: Routes = [
  {
    path: '', component: CaseFormWrapperComponent,
    children: [
      {path: '', redirectTo: 'reservation-details', pathMatch: 'full'}, // Redirect to 'reservation-details' by default
      {path: 'reservation-details', component: CaseFormReservationDetailsComponent},
      {path: 'flight-details', component: CaseFormFlightDetailsComponent},
      {path: 'problem-flight-details', component: CaseFormProblemFlightDetailsComponent},
      {path: 'disruption-details', component: CaseFormDisruptionDetailsComponent},
      {path: 'user-details', component: CaseFormUserDetailsComponent},
      {path: 'passenger-details', component: CaseFormPassengerDetailsComponent}
    ]
  },

];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CaseFormPageRoutingModule {
}

import {HttpClient, HttpClientModule, provideHttpClient} from "@angular/common/http";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {NgModule} from "@angular/core";

import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";

import {NavigationModule} from "../navigation/navigation.module";
import {RouterOutlet} from "@angular/router";
import {CommonModule} from "@angular/common";
import {
  CaseFormPassengerDetailsComponent
} from "./components/case-form-passenger-details/case-form-passenger-details.component";
import {CaseFormPageRoutingModule} from "./case-form-page-routing.module";
import {SharedModule} from "../shared/shared.module";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {
  CaseFormReservationDetailsComponent
} from './components/case-form-reservation-details/case-form-reservation-details.component';
import {AutoCompleteModule} from "primeng/autocomplete";
import {InputText, InputTextModule} from "primeng/inputtext";
import {IconFieldModule} from 'primeng/iconfield';
import {InputIconModule} from 'primeng/inputicon';
import {CaseFormFlightDetailsComponent} from './components/case-form-flight-details/case-form-flight-details.component';
import {CalendarModule} from "primeng/calendar";
import {FlightInfoComponent} from './components/flight-info/flight-info.component';
import {
  CaseFormDisruptionDetailsComponent
} from './components/case-form-distruption-details/case-form-disruption-details.component';
import {DropdownModule} from "primeng/dropdown";
import {InputTextareaModule} from "primeng/inputtextarea";
import {CaseFormUserDetailsComponent} from './components/case-form-user-details/case-form-user-details.component';
import {CheckboxModule} from "primeng/checkbox";
import {FileUploadModule} from "primeng/fileupload";
import {CaseFormWrapperComponent} from './components/case-form-wrapper/case-form-wrapper.component';
import {CaseFormProgressBarComponent} from './components/case-form-progress-bar/case-form-progress-bar.component';
import {
  CaseFormProblemFlightDetailsComponent
} from './components/case-form-problem-flight-details/case-form-problem-flight-details.component';
import {ProblemFlightInfoComponent} from './components/problem-flight-info/problem-flight-info.component';
import {AirportDisplayPipe} from './pipes/airport-display.pipe';
import {LandingPageModule} from "../landing-page/landing-page.module";
import {ToastModule} from "primeng/toast";

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    CaseFormPassengerDetailsComponent,
    CaseFormReservationDetailsComponent,
    CaseFormFlightDetailsComponent,
    FlightInfoComponent,
    CaseFormDisruptionDetailsComponent,
    CaseFormUserDetailsComponent,
    CaseFormWrapperComponent,
    CaseFormProgressBarComponent,
    CaseFormProblemFlightDetailsComponent,
    ProblemFlightInfoComponent,
    AirportDisplayPipe
  ],
    imports: [
        CommonModule,
        FormsModule,
        TranslateModule,
        NavigationModule,
        RouterOutlet,
        CaseFormPageRoutingModule,
        ReactiveFormsModule,
        SharedModule,
        ButtonDirective,
        Ripple,
        AutoCompleteModule,
        InputTextModule,
        IconFieldModule,
        InputIconModule,
        CalendarModule,
        DropdownModule,
        InputTextareaModule,
        CheckboxModule,
        FileUploadModule,
        LandingPageModule,
        ToastModule

    ],
  exports: [CaseFormPassengerDetailsComponent, CaseFormReservationDetailsComponent, CaseFormUserDetailsComponent],
  providers: [provideHttpClient()],
  bootstrap: []
})
export class CaseFormPageModule {
}

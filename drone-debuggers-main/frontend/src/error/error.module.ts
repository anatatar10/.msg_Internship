import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { MainErrorPageComponent } from './components/main-error-page.component';
import {LandingPageModule} from "../landing-page/landing-page.module";
import {TranslateModule} from "@ngx-translate/core";
import {NavigationModule} from "../navigation/navigation.module";



@NgModule({
  declarations: [
    MainErrorPageComponent
  ],
    imports: [
        CommonModule,
        LandingPageModule,
        NgOptimizedImage,
        TranslateModule,
        NavigationModule
    ]
})
export class ErrorModule { }

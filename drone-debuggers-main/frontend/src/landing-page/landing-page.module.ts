import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import { LandingPageRoutingModule } from './landing-page-routing.module';
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {TranslateModule} from "@ngx-translate/core";
import { TopBarComponent } from './components/top-bar/top-bar.component';
import {LandingPageComponent} from "./components/landing-page-component/landing-page.component";
import {SharedModule} from "../shared/shared.module";
import {FooterComponent} from "../footer/footer.component";
import {ToastModule} from "primeng/toast";


@NgModule({
  declarations: [
    LandingPageComponent,
    TopBarComponent,
    FooterComponent
  ],
    exports: [
        LandingPageComponent,
        FooterComponent
    ],
    imports: [
        CommonModule,
        LandingPageRoutingModule,
        NgOptimizedImage,
        ButtonDirective,
        Ripple,
        TranslateModule,
        SharedModule,
        ToastModule
    ]
})
export class LandingPageModule { }

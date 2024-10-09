import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NavigationComponent } from './components/navigation.component';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "../shared/shared.module";
import {RouterModule} from "@angular/router";
import {RouterLink} from "@angular/router";
import {ButtonDirective} from "primeng/button";



@NgModule({
    declarations: [
        NavigationComponent,
    ],
    exports: [
        NavigationComponent,
    ],
    imports: [
        CommonModule,
        SharedModule,
        TranslateModule,
        RouterModule,
        TranslateModule,
        RouterLink,
        NgOptimizedImage,
        RouterLink,
        ButtonDirective
    ]
})
export class NavigationModule { }

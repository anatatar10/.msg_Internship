import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MainLayoutComponent} from "./components/main-layout.component";
import {TranslateModule} from "@ngx-translate/core";
import {NavigationModule} from "../navigation/navigation.module";
import {RouterOutlet} from "@angular/router";



@NgModule({
  declarations: [MainLayoutComponent],
  imports: [
    CommonModule,
    TranslateModule,
    NavigationModule,
    RouterOutlet,
  ],
  exports: [MainLayoutComponent]
})
export class MainLayoutModule { }

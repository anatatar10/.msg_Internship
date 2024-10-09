import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { AppComponent } from './app.component';
import {NavigationModule} from "../navigation/navigation.module";
import {RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";
import {SharedModule} from "../shared/shared.module";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import {TableModule} from "primeng/table";
import {PdfDownloadModule} from "../pdf-download/pdf-download.module";
import {UserModule} from "../user/user.module";
import {ToastModule} from "primeng/toast";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AuthInterceptor} from "../shared/interceptors/auth.interceptor";
import {ApiInterceptor} from "../shared/interceptors/api.interceptor";
import {ErrorInterceptor} from "../shared/interceptors/error.interceptor";
import { MainLayoutModule } from '../main-layout/main-layout.module';
import { CaseListAdminModule } from '../caselist-admin/case-list-admin.module';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import {InputTextModule} from "primeng/inputtext";
import {LandingPageModule} from "../landing-page/landing-page.module";
import {CookieService} from "ngx-cookie-service";
import { MainErrorPageComponent } from '../error/components/main-error-page.component';
import { ErrorModule } from "../error/error.module"
import {PdfListModule} from "../pdf-list/pdf-list.module";




export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    FormsModule,
    CommonModule,
    TableModule,
    HttpClientModule,
    PdfDownloadModule,
    MainLayoutModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    SharedModule,
    NavigationModule,
    RouterOutlet,
    AppRoutingModule,
    ToastModule,
    BrowserAnimationsModule,
    UserModule,
    PdfListModule,
    InputTextModule,
    LandingPageModule,
    CaseListAdminModule,
    ConfirmDialogModule,
    InputTextModule,
    PdfDownloadModule,
    NgOptimizedImage,
    ErrorModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}

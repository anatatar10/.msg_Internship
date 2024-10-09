import { Injectable, OnDestroy } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MessageService } from 'primeng/api';
import { TranslateService, LangChangeEvent } from "@ngx-translate/core";

const toastLife: number = 5000;

@Injectable()
export class ErrorInterceptor implements HttpInterceptor, OnDestroy {

  private errorSummary: string;
  private errorMessage: string;
  private errorCode: string;
  private readonly langChangeSubscription: Subscription | undefined;
  private isToastDisplayed: boolean;

  constructor(private messageService: MessageService, private translate: TranslateService) {
    this.errorMessage = "";
    this.errorCode = "";
    this.errorSummary = "";
    this.isToastDisplayed = false;

    // Subscribe to language change events
    this.langChangeSubscription = this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
      if (this.isToastDisplayed) {
        // Update the error message and summary when the language changes
        this.updateErrorMessageAndSummary();
      }
    });
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        this.errorCode = error.error?.error_code || 'base';

        let toTranslate = "errors." + this.errorCode;
        // Fetch the error message and summary using the current language
        this.errorMessage = this.translate.instant(toTranslate);

        // If no specific error code translation is found, use the base error message
        if (this.errorMessage === toTranslate) {
          this.errorMessage = this.translate.instant("errors.base");
        }
        this.errorSummary = this.translate.instant("errors.error");

        // If a toast is already displayed, update it; otherwise, create a new one
        if (this.isToastDisplayed) {
          this.updateErrorMessageAndSummary();
        } else {
          this.showNewToast();
        }

        return throwError(() => new Error(this.errorMessage));
      })
    );
  }

  private showNewToast() {
    this.messageService.add({
      severity: 'error',
      summary: this.errorSummary, // Use the translated error summary
      detail: this.errorMessage,  // Use the translated error message
      life: toastLife // Display the toast for 5 seconds
    });
    this.isToastDisplayed = true;

    // Reset the flag after the toast life duration (5 seconds in this case)
    setTimeout(() => {
      this.isToastDisplayed = false;
    }, toastLife);
  }

  private updateErrorMessageAndSummary() {
    // Clear the previous toast and show the updated one
    this.messageService.clear();
    this.messageService.add({
      severity: 'error',
      summary: this.translate.instant("errors.error"),
      detail: this.translate.instant("errors." + this.errorCode),
      life: toastLife
    });

    // No need to reset isToastDisplayed since we are updating the existing toast
  }

  ngOnDestroy() {
    // Unsubscribe from language change events to prevent memory leaks
    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
    }
  }
}

import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environment/environment";

@Injectable()
export class ApiInterceptor implements HttpInterceptor {
  private baseURL = environment.backendApiUrl;

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.startsWith('/api')) {
      const apiReq = req.clone({ url: `${this.baseURL}${req.url}` });
      return next.handle(apiReq);
    }

    return next.handle(req);
  }
}


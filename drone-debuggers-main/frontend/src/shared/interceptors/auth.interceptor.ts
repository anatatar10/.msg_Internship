import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {UserRoleService} from "../../user/service/user-role.service";
import {RolesEnum} from "../../case-form-page/models/enums/roles.enum";
import {Router} from "@angular/router";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private token: string|null = sessionStorage.getItem('token');


  constructor(private userRoleService: UserRoleService, private routing :Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.includes('/api/assets/i18n/')) {
      return next.handle(req);
    }


    if (req.url.includes('/api/airport/all')) {
      return next.handle(req);
    }

    if (req.url.includes('/api/*')) {
      return next.handle(req);
    }

    if(req.url.includes('/api/auth/login')){
      return next.handle(req).pipe(
        tap(event => {
          if (event instanceof HttpResponse && event.status === 200) {
            const responseBody = event.body;
            if (responseBody.token && responseBody.roles) {
              this.token = responseBody.token;
              sessionStorage.setItem('token', responseBody.token);
              sessionStorage.setItem('role', responseBody.roles[0].roleName);
              let role: RolesEnum = RolesEnum.Nobody;
              if(responseBody.roles[0].roleName === "System Admin"){
                role = RolesEnum.SysAdmin;
              }else{
                if (responseBody.roles[0].roleName === "Colleague"){
                  role = RolesEnum.Colleague;
                }
                else{
                  if(responseBody.roles[0].roleName === "Passenger"){
                    role = RolesEnum.Passenger;
                  }
                }
              }

              if(responseBody.firstLogIn.valueOf()){
                role = RolesEnum.FirstLogin;
              }
              this.userRoleService.updateRole(role);
              if(responseBody.firstLogIn === "true"){
                this.routing.navigate(["login","first-login"])
              }
            }
          }
        })
      );
    }

    const clonedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${this.token}`)
    });

    return next.handle(clonedRequest);
  }
}

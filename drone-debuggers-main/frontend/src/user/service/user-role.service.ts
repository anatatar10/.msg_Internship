import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { RolesEnum } from '../../case-form-page/models/enums/roles.enum';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class UserRoleService {
  // Initialize the BehaviorSubject with a default role, e.g., 'guest'
  private userRoleSubject: BehaviorSubject<RolesEnum>;

  // Expose the Observable (read-only) part of the BehaviorSubject
  userRole$;

  // Define a cookie name for storing the role
  private readonly roleCookieName = 'userRole';

  constructor(private cookieService: CookieService) {
    // Get the stored role from cookies or use a default role
    const storedRole = this.cookieService.get(this.roleCookieName) as RolesEnum;

    // Initialize the BehaviorSubject with the stored role or a default value
    this.userRoleSubject = new BehaviorSubject<RolesEnum>(storedRole || RolesEnum.Nobody);

    // Assign the observable
    this.userRole$ = this.userRoleSubject.asObservable();
  }

  // Method to get the current role
  getCurrentRole(): RolesEnum {
    return this.userRoleSubject.getValue();
  }

  // Method to update the role and store it in a cookie
  updateRole(newRole: RolesEnum): void {
    this.userRoleSubject.next(newRole);

    // Update the role in the cookie
    this.cookieService.set(this.roleCookieName, newRole, undefined, '/'); // Cookie set without expiration will be a session cookie
  }

}

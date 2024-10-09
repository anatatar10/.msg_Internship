import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {UserRoleService} from "../../user/service/user-role.service";
import {RolesEnum} from "../../case-form-page/models/enums/roles.enum";


@Injectable({
  providedIn: 'root',
})
export class PassengerGuard implements CanActivate {
  constructor(private router: Router, private userRoleService: UserRoleService) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    if (this.userRoleService.getCurrentRole() === RolesEnum.Passenger) {
      return true;
    } else {
      // Navigate to login page
      this.router.navigate(['error']);
      return false;
    }
  }
}

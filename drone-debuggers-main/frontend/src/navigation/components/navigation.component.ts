import { Component, OnDestroy, OnInit } from '@angular/core';
import { RolesEnum } from "../../case-form-page/models/enums/roles.enum";
import { UserRoleService } from '../../user/service/user-role.service';
import { Subscription } from "rxjs";
import { LoginService } from "../../login/services/login.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'], // Corrected `styleUrl` to `styleUrls`
})
export class NavigationComponent implements OnInit, OnDestroy {

  loggedInRole: RolesEnum = RolesEnum.Nobody;
  private roleServiceSubscription: Subscription = new Subscription();

  // Declare isNavbarCollapsed property
  isNavbarCollapsed: boolean = true;

  constructor(
    private userRoleService: UserRoleService,
    private router: Router,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.roleServiceSubscription = this.userRoleService.userRole$.subscribe(role => {
      this.loggedInRole = role;
    });
  }

  logOut(): void {
    // Step 1: Update the user role to 'Nobody'
    this.userRoleService.updateRole(RolesEnum.Nobody);

    // Step 2: Notify the backend to blacklist the JWT token
    const token = sessionStorage.getItem("token");
    if (token) {
      this.loginService.logOut(token).subscribe({
        next: () => {
        },
        error: (err) => {
          console.error('Error during logout:', err);
        }
      });
    }

    // Step 3: Clear session storage
    sessionStorage.removeItem("email");
    sessionStorage.removeItem("role");
    sessionStorage.removeItem("token");

    // Step 4: Redirect to the login page
    this.router.navigate(["login"]);
  }

  ngOnDestroy() {
    this.roleServiceSubscription.unsubscribe();
  }

  onNavigate(): void {
    this.router.navigate(['/case-form']);
  }

  protected readonly RolesEnum = RolesEnum;
}

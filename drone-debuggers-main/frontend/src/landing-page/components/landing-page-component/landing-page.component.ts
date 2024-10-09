import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {UserRoleService} from "../../../user/service/user-role.service";
import {RolesEnum} from "../../../case-form-page/models/enums/roles.enum";

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.scss'
})
export class LandingPageComponent {

  constructor(private router: Router, private userRoleService: UserRoleService) {
  }


  public goToCreateCase(){
    if (this.userRoleService.getCurrentRole() === RolesEnum.FirstLogin){
      this.router.navigate(['login', 'change-password']);
    }
    else{
      this.router.navigate(['/case-form']);
    }
  }

  public goToLogIn(){
    if (this.userRoleService.getCurrentRole() === RolesEnum.FirstLogin){
      this.router.navigate(['login', 'change-password']);
    }
    else{
      this.router.navigate(['/login']);
    }
  }


}

import {Component, OnInit} from '@angular/core';
import {PrimeNGConfig} from "primeng/api";
import {UserRoleService} from "../user/service/user-role.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  constructor(private primengConfig: PrimeNGConfig, private userRoleService : UserRoleService) {
  }

  ngOnInit() {
    this.primengConfig.ripple = true;
  }


}

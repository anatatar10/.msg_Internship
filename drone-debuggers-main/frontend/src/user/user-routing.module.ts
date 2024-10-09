import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {UserListDTOComponent} from "./components/user-list-dto/user-list-dto.component";
import {CreateColleaguePageComponent} from "./components/create-colleague-page/create-colleague-page.component";

const routes: Routes = [
  { path: '', component: UserListDTOComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }

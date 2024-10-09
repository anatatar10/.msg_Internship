import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CaseListAdminComponent} from "./components/case-list-admin.component";

const routes: Routes = [
  {path:'', component: CaseListAdminComponent},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CaseListAdminRoutingModule { }

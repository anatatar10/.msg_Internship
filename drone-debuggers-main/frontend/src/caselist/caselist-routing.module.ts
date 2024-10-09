import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CaseDetailsComponent} from "./case-details/components/case-details-component/case-details.component";
import {CaseTableComponent} from "./case-table/case-table.component";

const routes: Routes = [
  { path: '', component: CaseTableComponent },
  { path: 'profile/:id', component: CaseDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CaseListRoutingModule { }

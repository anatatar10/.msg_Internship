import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PdfDownloadComponent} from "../pdf-download/components/pdf-download.component";
import {UserListDTOComponent} from "../user/components/user-list-dto/user-list-dto.component";
import {MainLayoutComponent} from "../main-layout/components/main-layout.component";
import { LandingPageComponent } from '../landing-page/components/landing-page-component/landing-page.component';
import {CreateColleaguePageComponent} from "../user/components/create-colleague-page/create-colleague-page.component";
import {CaseListAdminModule} from "../caselist-admin/case-list-admin.module";
import {CaseListAdminComponent} from "../caselist-admin/components/case-list-admin.component";
import {ColleagueGuard} from "../shared/guards/colleague.guard";
import {NobodyGuard} from "../shared/guards/nobody.guard";
import {PassengerGuard} from "../shared/guards/passenger.guard";
import {SystemAdministratorGuard} from "../shared/guards/sys-admin.guard";
import {MainErrorPageComponent} from "../error/components/main-error-page.component";
import {PdfListComponent} from "../pdf-list/components/pdf-list.component";
import {CaseFormGuard} from "../shared/guards/case-form.guard";
import {CasesGuard} from "../shared/guards/cases.guard";

const routes: Routes = [
  {
    path: '',
    component: LandingPageComponent
  },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: 'cases',
        loadChildren: () => import('../caselist/caselist.module').then(m => m.CaselistModule),
        canActivate: [CasesGuard]
      },
      {
        path: 'login',
        loadChildren: () => import('../login/login.module').then(m => m.LoginModule),
        canActivate: [NobodyGuard]
      },
      {
        path: 'case-form',
        loadChildren: () => import('../case-form-page/case-form-page.module').then(m => m.CaseFormPageModule),
        canActivate: [CaseFormGuard]
      },
      {
        path: 'user-list',
        component:UserListDTOComponent,
        loadChildren: () => import('../user/user.module').then(m => m.UserModule),
        canActivate: [SystemAdministratorGuard]
      },
      {
        path: 'create-colleague',
        component: CreateColleaguePageComponent,
        canActivate: [SystemAdministratorGuard]
      },
      {
        path: 'cases/admin',
        component: CaseListAdminComponent,
        canActivate: [SystemAdministratorGuard]
      },
      {
        path: 'pdf-list',
        component: PdfListComponent,
        canActivate: [ColleagueGuard]
      }
    ]
  },
  { path: '**', component:MainErrorPageComponent},
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

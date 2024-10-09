import {Component, OnInit} from '@angular/core';
import {UserListDTO} from "../../model/userListDTO";
import {UserService} from "../../service/user.service";
import {firstValueFrom, take} from "rxjs";
import {Router} from "@angular/router";
import {ConfirmationService, MessageService} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-user-list-dto',
  templateUrl: './user-list-dto.component.html',
  styleUrl: './user-list-dto.component.scss',
  providers: [ConfirmationService, MessageService]
})
export class UserListDTOComponent implements OnInit{

  userListDTO: UserListDTO[] = [];

  constructor(private userService: UserService, private router: Router, private messageService: MessageService, private confirmationService: ConfirmationService, private translate:TranslateService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  private async loadUsers(): Promise<void> {
    try {
      this.userListDTO = await firstValueFrom(this.userService.getUsersFromDB());
    } catch (error) {
      console.error('Error loading users:', error);
      throw error;
    }
  }

  public goToCreateColleague(): void {
    this.router.navigate(["create-colleague"]);
  }

  confirmDelete(email: string, event: Event): void {
    this.translate.get('userListDTO.DELETE_CONFIRMATION').pipe(take(1)).subscribe((translatedMessage: string) => {
      this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: translatedMessage,
        header: this.translate.instant('userListDTO.CONFIRMATION'),
        icon: 'pi pi-exclamation-triangle',
        acceptButtonStyleClass: 'p-button-danger p-button-text',
        rejectButtonStyleClass: 'p-button-text',
        accept: () => {
          this.deleteUser(email);
        },
        reject: () => {
          this.translate.get('userListDTO.TOAST_DELETE_CANCELLED').pipe(take(1)).subscribe((translatedCancelMessage: string) => {
            this.messageService.add({
              severity: 'info',
              summary: this.translate.instant('errors.error'),
              detail: translatedCancelMessage,
              life: 3000
            });
          });
        }
      });
    });
  }



  private deleteUser(email: string): void {
    this.userService.deleteUser(email).subscribe(() => {
      this.userListDTO = this.userListDTO.filter(userItem => userItem.email !== email);
      this.loadUsers();
      this.translate.get('userListDTO.TOAST_DELETE_SUCCESSFUL').pipe(take(1)).subscribe((translatedSuccessMessage: string) => {
        this.messageService.add({
          severity: 'success',
          summary: this.translate.instant('user-details.success'),
          detail: translatedSuccessMessage,
          life: 4000
        });
      });
    }, error => {
      this.translate.get('userListDTO.TOAST_DELETE_ERROR').pipe(take(1)).subscribe((translatedErrorMessage: string) => {
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('errors.error'),
          detail: translatedErrorMessage,
          life: 4000
        });
      });
    });
  }

}


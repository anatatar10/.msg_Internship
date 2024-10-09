import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginService} from '../services/login.service';
import {LoginData} from "../models/loginData";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {UserRoleService} from "../../user/service/user-role.service";
import {RolesEnum} from "../../case-form-page/models/enums/roles.enum";

@Component({
  selector: 'app-auth-form-page',
  templateUrl: './first-login.component.html',
  styleUrls: ['./first-login.component.scss']
})
export class FirstLoginComponent implements OnInit,OnDestroy {
  passwordChangeForm: FormGroup = new FormGroup({});

  constructor(private fb: FormBuilder, private loginService: LoginService, private router:Router, private userRoleService: UserRoleService) { }

  subscriptions: Subscription = new Subscription();

  ngOnInit(): void {
    this.passwordChangeForm = this.fb.group({
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(6)  // Adjust this as needed
      ]),
      passwordCheck: new FormControl('', [
        Validators.required
      ])
    });

    // Watch for changes in the password fields to trigger validation
    this.subscriptions.add(this.passwordChangeForm.valueChanges.subscribe(() => {
      this.passwordChangeForm.updateValueAndValidity();
    }));
    // Subscribe to value changes to perform custom validation
    this.subscriptions.add(this.passwordChangeForm.get('password')?.valueChanges.subscribe(() => this.validatePasswords()));
    this.subscriptions.add(this.passwordChangeForm.get('passwordCheck')?.valueChanges.subscribe(() => this.validatePasswords()));


  }

  // Validator function to check if passwords match
  validatePasswords(): void {
    const password = this.passwordChangeForm.get('password')?.value;
    const passwordCheck = this.passwordChangeForm.get('passwordCheck')?.value;

    if (password && passwordCheck && password !== passwordCheck) {
      this.passwordChangeForm.get('passwordCheck')?.setErrors({ mismatch: true });
    } else {
      this.passwordChangeForm.get('passwordCheck')?.setErrors(null);
    }
  }

  // Getter for password form control
  get password(): FormControl {
    return this.passwordChangeForm.get('password') as FormControl;
  }

  get passwordCheck(): FormControl {
    return this.passwordChangeForm.get('passwordCheck') as FormControl;
  }

  onSubmit(): void {
    if (this.passwordChangeForm.valid) {
      const loginData: LoginData = this.passwordChangeForm.value;
      this.subscriptions.add(this.loginService.changePassword(loginData.password, sessionStorage.getItem("email")).subscribe({
        next: (response) => {
          this.router.navigate(['login']);
          this.userRoleService.updateRole(RolesEnum.Nobody);
        }
      }));
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}

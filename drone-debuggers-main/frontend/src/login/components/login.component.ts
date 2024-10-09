import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginService} from '../services/login.service';
import {LoginData} from "../models/loginData";
import {Router} from "@angular/router";

const emailRegexForValidation : string = '^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$';

@Component({
  selector: 'app-auth-form-page',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  constructor(private fb: FormBuilder, private loginService: LoginService, private router: Router) { } // Inject LoginService

  loginForm: FormGroup = new FormGroup({});

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: new FormControl('', [
        Validators.required,
        Validators.pattern(emailRegexForValidation)
      ]),
      password: new FormControl('', [
        Validators.required
      ])
    });
  }

  // Getter for email form control
  get username(): FormControl {
    return this.loginForm.get('username') as FormControl;
  }

  // Getter for password form control
  get password(): FormControl {
    return this.loginForm.get('password') as FormControl;
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const loginData: LoginData = this.loginForm.value;
      this.loginService.loginAttempt(loginData).subscribe({
        next: (response:any) => {
          sessionStorage.setItem('email', this.loginForm.value.username);

            let isFirstLogin;
            if(response.firstLogIn){
              isFirstLogin=response.firstLogIn.valueOf();
            }
            else {
              isFirstLogin = false;
            }

            if(isFirstLogin){
              this.router.navigate(['login', 'change-password']);
              sessionStorage.removeItem('token');
              sessionStorage.removeItem('role');
            }
            else{
              //passenger case
              if(sessionStorage.getItem("role") === "Passenger"){
                this.router.navigate(['cases']);
              }
              else{
                //colleague case
                if(sessionStorage.getItem("role") === "Colleague"){
                  this.router.navigate(['cases']);
                }
                //admin case
                else {
                  if(sessionStorage.getItem("role") === "System Admin"){
                    this.router.navigate(['user-list']);
                  }
                }
              }
          }
        }
      });
    }
  }
}

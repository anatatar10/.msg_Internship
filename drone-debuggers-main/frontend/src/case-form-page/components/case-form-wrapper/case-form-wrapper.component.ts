import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-case-form-wrapper',
  templateUrl: './case-form-wrapper.component.html',
  styleUrl: './case-form-wrapper.component.scss'
})
export class CaseFormWrapperComponent implements OnInit {
  constructor(private router:Router) { }

  ngOnInit(): void {
    this.router.navigate(['/case-form']);
  }

}

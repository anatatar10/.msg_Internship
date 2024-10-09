import {Component, OnInit, Renderer2} from '@angular/core';

@Component({
  selector: 'app-error-page',
  templateUrl: './main-error-page.component.html',
  styleUrl: './main-error-page.component.scss'
})
export class MainErrorPageComponent implements OnInit{
  constructor(private renderer: Renderer2) {
  }
  ngOnInit(): void {
    this.renderer.addClass(document.body, 'no-scroll');

  }
}

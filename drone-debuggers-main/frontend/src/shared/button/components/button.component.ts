import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss'],
})
export class ButtonComponent {

  @Input() label: string = '';
  @Input() type: string = 'button';
  @Input() disabled: boolean = false;
  @Input() selected: boolean = false;
  @Input() isLanguageSwitch: boolean = false; // New input for language switch


  @Output() onClick = new EventEmitter<void>();

  handleClick() {
    if (!this.disabled) {
      this.onClick.emit();
    }
  }

  getFlagSrc(): string {
    if (this.label === 'Engleză') {
      return 'assets/flags/united-kingdom.png';
    } else if (this.label === 'Română') {
      return 'assets/flags/romania.png';
    }
    return '';
  }
}

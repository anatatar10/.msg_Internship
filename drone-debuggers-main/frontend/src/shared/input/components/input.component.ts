import {Component, Input, Output, EventEmitter, booleanAttribute, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
})
export class InputComponent implements ControlValueAccessor{
  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  @Input() required: boolean = false;
  @Input() value: any = '';
  @Output() focus = new EventEmitter<void>();
  @Output() blur = new EventEmitter<void>();

  @Output() valueChange = new EventEmitter<string>();

  private onTouched: () => void = () => {};
  private onChange: (value: string) => void = () => {};


  handleInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.value = input.value;
    this.onChange(this.value);
  }


  getIconClass(): string {
    switch (this.type) {
      case 'password':
        return 'fas fa-lock';
      case 'email':
        return 'fas fa-envelope';
      default:
        return 'fas fa-pencil-alt';
    }
  }


  writeValue(value: string): void {
    this.value = value;
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }
}

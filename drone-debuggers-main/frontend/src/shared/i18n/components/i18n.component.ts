import { Component } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-i18n',
  templateUrl: './i18n.component.html',
  styleUrl: './i18n.component.scss'
})
export class I18nComponent {

  selectedLanguage: string = 'ro'; // Default selected language

  constructor(private translate: TranslateService) {
    translate.setDefaultLang(this.selectedLanguage);
    translate.use(this.selectedLanguage);
  }

  switchLanguage(lang: string) {
    this.selectedLanguage = lang;
    this.translate.use(lang);
  }

  isSelected(lang: string): boolean {
    return this.selectedLanguage === lang;
  }
}

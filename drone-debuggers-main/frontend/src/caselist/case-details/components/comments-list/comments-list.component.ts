import {Component, Input} from '@angular/core';
import {Comment} from "../../models/comment";

@Component({
  selector: 'app-comments-list',
  templateUrl: './comments-list.component.html',
  styleUrl: './comments-list.component.scss'
})
export class CommentsListComponent {
  @Input() comment : Comment | undefined;
}

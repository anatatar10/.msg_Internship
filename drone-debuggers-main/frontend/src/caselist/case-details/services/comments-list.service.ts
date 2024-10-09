import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Comment} from "../models/comment";
@Injectable({
  providedIn: 'root'
})
export class CommentsListService {

  constructor(private http: HttpClient) {}
  private getCommentApiUrl: string = "/api/comment/case/"
  private addCommentApiUrl: string = "/api/comment/add/"
  public getCommentsByCaseId(caseId: string | null | undefined) {
    return this.http.get<Comment[]>(this.getCommentApiUrl+caseId);
  }

  public addComment(caseId: string | null | undefined, comment: Comment) {
    return this.http.post<Comment>(this.addCommentApiUrl+caseId, comment);
  }
}

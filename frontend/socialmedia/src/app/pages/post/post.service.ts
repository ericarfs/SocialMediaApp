import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, tap } from 'rxjs';
import { AnswerResponse, LikeResponse, ShareResponse } from '../types/answer-response';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = 'http://localhost:8080/api';

  private answersSubject = new BehaviorSubject<AnswerResponse[]>([]);
  answers$ = this.answersSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadAnswers() {
    this.http.get<AnswerResponse[]>(this.baseUrl)
      .subscribe(a => this.answersSubject.next(a));
  }

  toggleLike(id: number) {
    return this.http.patch<LikeResponse>(`${this.baseUrl}/answers/${id}/likes`,{});
  }

  toggleShare(id: number) {
    return this.http.patch<ShareResponse>(`${this.baseUrl}/${id}/shares`,{});
  }
}

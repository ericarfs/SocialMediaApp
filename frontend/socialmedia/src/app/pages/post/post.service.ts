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
    return this.http.patch<ShareResponse>(`${this.baseUrl}/answers/${id}/shares`,{});
  }

  sendQuestion(id: number, body:string, anon: boolean){
    return this.http.post(`${this.baseUrl}/questions/in-response-to/${id}`, { body, anon });
  }

  update(id: number, body:string){
    return this.http.patch(`${this.baseUrl}/answers/${id}`, { body });
  }

  delete(id: number){
    return this.http.delete(`${this.baseUrl}/answers/${id}`);
  }
}

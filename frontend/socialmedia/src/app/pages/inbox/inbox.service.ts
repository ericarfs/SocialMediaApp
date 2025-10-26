import { computed, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { QuestionResponse } from '../types/question-response';
import { tap } from 'rxjs';


export interface PagedModel<T> {
  content: T[];
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };

}

@Injectable({
  providedIn: 'root'
})
export class InboxService {
  private baseUrl = 'http://localhost:8080/api/questions'

  private questions = signal<QuestionResponse[]>([]);
  private currentPage = signal(0);
  private totalPages = signal(1);
  private loading = signal(false);

  questions$ = computed(() => this.questions());
  loading$ = computed(() => this.loading());

  constructor(private http: HttpClient) {}

  loadNextPage(size: number = 10): void {
    if (this.loading() || this.currentPage() >= this.totalPages()) return;

    this.loading.set(true);

    const params = new HttpParams()
      .set('page', this.currentPage())
      .set('size', size)
      .set('sort', 'createdAt,desc');

    this.http.get<PagedModel<QuestionResponse>>(`${this.baseUrl}/me`, { params }).subscribe({
      next: (res) => {
        this.questions.set([...this.questions(), ...res.content]);
        this.currentPage.set(res.page.number + 1);
        this.totalPages.set(res.page.totalPages);
      },
      error: (err) => console.error('Erro ao carregar perguntas:', err),
      complete: () => this.loading.set(false),
    });
  }

  resetFeed(): void {
    this.questions.set([]);
    this.currentPage.set(0);
    this.totalPages.set(1);
  }

  delete(id: number) {
    return this.http.delete(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        this.questions.update(current => current.filter(q => q.id !== id));
      })
    );
  }

  sendAnswer(body: string, id: number) {
    return this.http.post(`${this.baseUrl}/${id}/answer`, { body }).pipe(
      tap(() => {
        this.questions.update(current => current.filter(q => q.id !== id));
      })
    );
  }
}

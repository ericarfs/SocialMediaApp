import { computed, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnswerResponse } from '../types/answer-response';


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
export class HomeService {
  private baseUrl = 'http://localhost:8080/api/users/me/feed'

  private answers = signal<AnswerResponse[]>([]);
  private currentPage = signal(0);
  private totalPages = signal(1);
  private loading = signal(false);

  answers$ = computed(() => this.answers());
  loading$ = computed(() => this.loading());

  constructor(private http: HttpClient) {}

  loadNextPage(size: number = 10): void {
    if (this.loading() || this.currentPage() >= this.totalPages()) return;

    this.loading.set(true);

    const params = new HttpParams()
      .set('page', this.currentPage())
      .set('size', size);

    this.http.get<PagedModel<AnswerResponse>>(this.baseUrl, { params }).subscribe({
      next: (res) => {
        this.answers.set([...this.answers(), ...res.content]);
        this.currentPage.set(res.page.number + 1);
        this.totalPages.set(res.page.totalPages);
      },
      error: (err) => console.error('Erro ao carregar feed:', err),
      complete: () => this.loading.set(false),
    });
  }

  resetFeed(): void {
    this.answers.set([]);
    this.currentPage.set(0);
    this.totalPages.set(1);
  }
}

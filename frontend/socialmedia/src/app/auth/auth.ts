import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, tap, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export interface User {
  username: string;
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<string | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  private baseUrl = "http://localhost:8080";
  token: string|null = null;

  constructor(private http: HttpClient, private router: Router) {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  get currentUser(): String | null {
    return this.currentUserSubject.value;
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/auth/token`, { username, password })
      .pipe(
        tap(res => {
          localStorage.setItem('accessToken', res.access);
          localStorage.setItem('refreshToken', res.refresh);
          localStorage.setItem('currentUser', JSON.stringify(username));
          this.currentUserSubject.next(username);
        })
      );
  }

  register(email: string, username: string, password: string ){
    return this.http.post(`${this.baseUrl}/auth/register`, { email, username, password });
  }

  resetPassword(token: string, newPassword: string){
    return this.http.post(`${this.baseUrl}/auth/reset-password/${token}`, { newPassword });
  }

  generateResetToken(email: string){
    return this.http.post(`${this.baseUrl}/auth/reset-password`, { email });
  }

  checkResetToken(token: string){
    return this.http.get(`${this.baseUrl}/auth/reset-password/${token}`);
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  refreshToken(): Observable<any> {
    localStorage.removeItem('accessToken');
    const token = localStorage.getItem('refreshToken');
    if (!token) {
      this.logout();
      return throwError(() => new Error('No refresh token.'));
    }
    return this.http.post<any>(`${this.baseUrl}/auth/token/refresh`, { refreshToken: token })
      .pipe(
        tap(res => localStorage.setItem('accessToken', res.access)),
        catchError(err => {
          this.logout();
          return throwError(() => err);
        })
      );
  }

  getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({ 'Authorization': token ? `Bearer ${token}` : '' });
  }

  isLoggedIn(): boolean {
    return !!this.currentUser;
  }

  loadToken(){
    const token = localStorage.getItem('accessToken');
    if(token) this.token = token;
    return this.token;
  }

  isAuthenticated(){
    return !!this.loadToken();
  }
}

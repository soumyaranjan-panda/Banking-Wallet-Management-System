import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL = 'http://localhost:9005';
  private readonly TOKEN_KEY = 'auth_token';
  private readonly ROLE_KEY = 'user_role';

  constructor(private http: HttpClient) {
    // Load token from localStorage on service initialization
    this.loadTokenFromStorage();
  }

  register(username: string, password: string): Observable<any> {
    return this.http.post(`${this.BASE_URL}/auth/register`, {
      username,
      password
    });
  }

  login(username: string, password: string): Observable<string> {
    return this.http.post(`${this.BASE_URL}/auth/login`, {
      username,
      password
    }, { responseType: 'text' }).pipe(
      tap(token => {
        this.setToken(token);
      })
    );
  }

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const role = payload.role || 'USER';
      localStorage.setItem(this.ROLE_KEY, role);
    } catch (e) {
      localStorage.setItem(this.ROLE_KEY, 'USER');
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRole(): string | null {
    return localStorage.getItem(this.ROLE_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.ROLE_KEY);
  }

  private loadTokenFromStorage(): void {
    // Token is already in localStorage, no need to do anything
    // This method is here for potential future use
  }
}

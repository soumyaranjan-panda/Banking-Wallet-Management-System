import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
// import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private BASE_URL = 'http://localhost:9005';
  token: string | null = null;

  constructor(private http: HttpClient) {}

  register(username: string, password: string) {
    return this.http.post(
      `${this.BASE_URL}/auth/register`,
      { username, password }
    );
  }

  login(username: string, password: string) {
    return this.http.post(
      `${this.BASE_URL}/auth/login`,
      { username, password },
      { responseType: 'text' }
    );
  }

  setToken(token: string) {
    this.token = token;
  }

  getAuthHeader() {
    return {
      Authorization: `Bearer ${this.token}`
    };
  }
}

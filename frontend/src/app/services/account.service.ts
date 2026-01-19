import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface Account {
  id: number;
  userId: number;
  balance: number;
  blocked: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private readonly BASE_URL = 'http://localhost:9005';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  createAccount(): Observable<Account> {
    return this.http.post<Account>(
      `${this.BASE_URL}/account/create`,
      {},
      { headers: this.getHeaders() }
    );
  }

  getAccount(accountId: number): Observable<Account> {
    return this.http.get<Account>(
      `${this.BASE_URL}/account/${accountId}`,
      { headers: this.getHeaders() }
    );
  }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(
      `${this.BASE_URL}/account`,
      { headers: this.getHeaders() }
    );
  }

  getBalance(accountId: number): Observable<number> {
    return this.http.get<number>(
      `${this.BASE_URL}/account/${accountId}/balance`,
      { headers: this.getHeaders() }
    );
  }

  creditAccount(accountId: number, amount: number): Observable<any> {
    return this.http.put(
      `${this.BASE_URL}/account/credit/${accountId}?amount=${amount}`,
      {},
      { headers: this.getHeaders() }
    );
  }

  debitAccount(accountId: number, amount: number): Observable<any> {
    return this.http.put(
      `${this.BASE_URL}/account/debit/${accountId}?amount=${amount}`,
      {},
      { headers: this.getHeaders() }
    );
  }

  blockAccount(accountId: number): Observable<any> {
    return this.http.put(
      `${this.BASE_URL}/account/block/${accountId}`,
      {},
      { headers: this.getHeaders() }
    );
  }

  validateOwner(accountId: number): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.BASE_URL}/account/${accountId}/validate-owner`,
      { headers: this.getHeaders() }
    );
  }

  accountExists(accountId: number): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.BASE_URL}/account/${accountId}/exists`,
      { headers: this.getHeaders() }
    );
  }
}

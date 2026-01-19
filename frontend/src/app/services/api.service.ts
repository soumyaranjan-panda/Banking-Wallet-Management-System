import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';
// import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private BASE_URL = 'http://localhost:9005';

  constructor(
    private http: HttpClient,
    private auth: AuthService
  ) {}

  private headers() {
    console.log('JWT TOKEN:', this.auth.token);
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.auth.token}`
      })
    };
  }

  createAccount() {
    return this.http.post(
      `${this.BASE_URL}/account/create`,
      {},
      this.headers()
    );
  }

  getBalance(accountId: number) {
    return this.http.get(
      `${this.BASE_URL}/account/${accountId}/balance`,
      this.headers()
    );
  }

  credit(accountId: number, amount: number) {
    return this.http.put(
      `${this.BASE_URL}/account/credit/${accountId}?amount=${amount}`,
      {},
      this.headers()
    );
  }

  debit(accountId: number, amount: number) {
    return this.http.put(
      `${this.BASE_URL}/account/debit/${accountId}?amount=${amount}`,
      {},
      this.headers()
    );
  }

  blockAccount(accountId: number) {
    return this.http.put(
      `${this.BASE_URL}/account/block/${accountId}`,
      {},
      this.headers()
    );
  }

  transfer(fromId: number, toId: number, amount: number) {
    return this.http.post(
      `${this.BASE_URL}/transaction/transfer`,
      {
        fromAccountId: fromId,
        toAccountId: toId,
        amount
      },
      this.headers()
    );
  }

  history(accountId: number) {
    return this.http.get(
      `${this.BASE_URL}/transaction/history/${accountId}`,
      this.headers()
    );
  }
}

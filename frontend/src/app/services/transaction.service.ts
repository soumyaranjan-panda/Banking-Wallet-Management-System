import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface Transaction {
  id: number;
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  type: string;
  status: string;
  createdAt: string;
}

export interface TransferRequest {
  fromAccountId: number;
  toAccountId: number;
  amount: number;
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
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

  transfer(request: TransferRequest): Observable<Transaction> {
    return this.http.post<Transaction>(
      `${this.BASE_URL}/transaction/transfer`,
      request,
      { headers: this.getHeaders() }
    );
  }

  getTransactionHistory(accountId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(
      `${this.BASE_URL}/transaction/history/${accountId}`,
      { headers: this.getHeaders() }
    );
  }

  getAllTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(
      `${this.BASE_URL}/transaction/history`,
      { headers: this.getHeaders() }
    );
  }
}

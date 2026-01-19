import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService, Transaction } from '../../services/transaction.service';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {
  fromAccountId: number | null = null;
  toAccountId: number | null = null;
  amount = 0;
  transactions: Transaction[] = [];
  accounts: any[] = [];
  loading = false;
  error = '';
  success = '';

  constructor(
    private transactionService: TransactionService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.loadAccounts();
    this.loadAllTransactions();
  }

  loadAccounts(): void {
    this.accountService.getAllAccounts().subscribe({
      next: (accounts) => {
        const previousFromAccountId = this.fromAccountId;
        this.accounts = accounts;
        
        // Preserve selected account if it still exists, otherwise select first account
        if (accounts.length > 0) {
          const accountExists = accounts.some(acc => acc.id === previousFromAccountId);
          if (accountExists && previousFromAccountId) {
            this.fromAccountId = previousFromAccountId;
          } else {
            this.fromAccountId = accounts[0].id;
          }
        } else {
          this.fromAccountId = null;
        }
        // Don't set toAccountId - let user enter any account ID
      },
      error: (err) => {
        this.error = 'Failed to load accounts';
      }
    });
  }

  transfer(): void {
    if (!this.fromAccountId || !this.toAccountId || this.amount <= 0) {
      this.error = 'Please fill in all fields with valid values';
      return;
    }

    if (this.fromAccountId === this.toAccountId) {
      this.error = 'From and To accounts cannot be the same';
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    this.transactionService.transfer({
      fromAccountId: this.fromAccountId,
      toAccountId: this.toAccountId,
      amount: this.amount
    }).subscribe({
      next: (transaction) => {
        this.success = `Transfer successful! Transaction ID: ${transaction.id}`;
        this.amount = 0;
        this.loadAllTransactions();
        this.loadAccounts(); // Refresh account balances
        this.loading = false;
        setTimeout(() => {
          this.success = '';
        }, 5000);
      },
      error: (err) => {
        this.loading = false;
        let errorMessage = 'Transfer failed';
        if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }
        this.error = errorMessage;
      }
    });
  }

  loadAllTransactions(): void {
    this.loading = true;
    this.transactionService.getAllTransactions().subscribe({
      next: (transactions) => {
        this.transactions = transactions;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load transaction history';
        this.loading = false;
      }
    });
  }

  refresh(): void {
    this.loadAccounts();
    this.loadAllTransactions();
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString();
  }
}

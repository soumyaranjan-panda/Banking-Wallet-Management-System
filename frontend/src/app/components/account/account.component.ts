import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccountService, Account } from '../../services/account.service';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  selectedAccountId: number | null = null;
  balance: number | null = null;
  creditAmount = 0;
  debitAmount = 0;
  loading = false;
  error = '';
  success = '';
  createdAccountId: number | null = null;

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    // Don't load accounts automatically - user will enter account ID manually
  }

  createAccount(): void {
    this.loading = true;
    this.error = '';
    this.success = '';

    this.accountService.createAccount().subscribe({
      next: (account) => {
        this.createdAccountId = account.id;
        this.selectedAccountId = account.id;
        this.success = `Account created successfully! Account ID: ${account.id}`;
        this.loadBalance();
        this.loading = false;
        setTimeout(() => {
          this.success = '';
        }, 5000);
      },
      error: (err) => {
        this.loading = false;
        // Handle both string and object error responses
        let errorMessage = 'Failed to create account';
        if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        } else if (err.error?.errors) {
          const validationErrors = Object.values(err.error.errors).join(', ');
          errorMessage = validationErrors || errorMessage;
        }
        this.error = errorMessage;
      }
    });
  }

  loadBalance(): void {
    if (!this.selectedAccountId) return;

    this.accountService.getBalance(this.selectedAccountId).subscribe({
      next: (balance) => {
        this.balance = balance;
      },
      error: (err) => {
        this.error = 'Failed to load balance';
      }
    });
  }

  onAccountChange(): void {
    if (this.selectedAccountId) {
      this.loadBalance();
    }
  }

  creditAccount(): void {
    if (!this.selectedAccountId || this.creditAmount <= 0) {
      this.error = 'Please enter a valid amount';
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    this.accountService.creditAccount(this.selectedAccountId, this.creditAmount).subscribe({
      next: () => {
        this.success = `Successfully credited $${this.creditAmount} to account ${this.selectedAccountId}`;
        this.creditAmount = 0;
        this.loadBalance();
        this.loading = false;
        setTimeout(() => {
          this.success = '';
        }, 5000);
      },
      error: (err) => {
        this.loading = false;
        let errorMessage = 'Failed to credit account';
        if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }
        this.error = errorMessage;
      }
    });
  }

  debitAccount(): void {
    if (!this.selectedAccountId || this.debitAmount <= 0) {
      this.error = 'Please enter a valid amount';
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    this.accountService.debitAccount(this.selectedAccountId, this.debitAmount).subscribe({
      next: () => {
        this.success = `Successfully debited $${this.debitAmount} from account ${this.selectedAccountId}`;
        this.debitAmount = 0;
        this.loadBalance();
        this.loading = false;
        setTimeout(() => {
          this.success = '';
        }, 5000);
      },
      error: (err) => {
        this.loading = false;
        let errorMessage = 'Failed to debit account';
        if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }
        this.error = errorMessage;
      }
    });
  }
}

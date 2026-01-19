import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccountService, Account } from '../../services/account.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  selectedAccountId: number | null = null;
  loading = false;
  error = '';
  success = '';

  constructor(
    private accountService: AccountService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdmin()) {
      this.error = 'Access denied. Admin privileges required.';
      return;
    }
  }

  blockAccount(): void {
    if (!this.selectedAccountId) {
      this.error = 'Please select an account';
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    this.accountService.blockAccount(this.selectedAccountId).subscribe({
      next: () => {
        this.success = `Account ${this.selectedAccountId} has been blocked successfully`;
        this.loading = false;
        setTimeout(() => {
          this.success = '';
        }, 5000);
      },
      error: (err) => {
        this.loading = false;
        let errorMessage = 'Failed to block account';
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

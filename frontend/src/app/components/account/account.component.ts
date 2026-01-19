import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './account.component.html'
})
export class AccountComponent {

  accountId = 1;
  amount = 0;
  balance: any;

  constructor(private api: ApiService) {}

  create() {
    this.api.createAccount().subscribe(res => {
      this.accountId = (res as any).id;
    });
  }

  getBalance() {
    this.api.getBalance(this.accountId).subscribe(b => {
      this.balance = b;
    });
  }

  credit() {
    this.api.credit(this.accountId, this.amount).subscribe();
  }

  debit() {
    this.api.debit(this.accountId, this.amount).subscribe();
  }
}

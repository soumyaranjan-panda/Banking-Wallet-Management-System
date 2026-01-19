import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaction.component.html'
})
export class TransactionComponent {

  fromAccountId = 0;
  toAccountId = 0;
  amount = 0;

  constructor(private api: ApiService) {}

  transfer() {
    console.log('TRANSFER CLICKED');

    this.api.transfer(this.fromAccountId, this.toAccountId, this.amount)
      .subscribe({
        next: res => alert('Transfer success'),
        error: err => alert(err.error || 'Transfer failed')
      });
  }

  history() {
    console.log('HISTORY CLICKED');

    this.api.history(this.fromAccountId)
      .subscribe({
        next: res => console.log('History:', res),
        error: err => alert(err.error || 'History failed')
      });
  }
}

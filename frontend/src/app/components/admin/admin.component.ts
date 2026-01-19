import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html'
})
export class AdminComponent {

  accountId = 0;

  constructor(private api: ApiService) {}

  block() {
    console.log('BLOCK CLICKED', this.accountId);

    this.api.blockAccount(this.accountId).subscribe({
      next: () => alert('Account blocked'),
      error: err => alert(err.error || 'Block failed')
    });
  }
}

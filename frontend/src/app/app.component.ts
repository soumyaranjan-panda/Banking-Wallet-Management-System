import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthComponent } from './components/auth/auth.component';
import { AccountComponent } from './components/account/account.component';
import { TransactionComponent } from './components/transaction/transaction.component';
import { AdminComponent } from './components/admin/admin.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    AuthComponent,
    AccountComponent,
    TransactionComponent,
    AdminComponent
  ],
  templateUrl: './app.component.html'
})
export class AppComponent {
  view: 'auth' | 'account' | 'transaction' | 'admin' = 'auth';
}

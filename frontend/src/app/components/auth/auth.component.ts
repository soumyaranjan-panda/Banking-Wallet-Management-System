import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html'
})
export class AuthComponent {

  username = '';
  password = '';
  message = '';

  constructor(private auth: AuthService) {}

  register() {
    this.auth.register(this.username, this.password)
      .subscribe(() => {
        this.message = 'Registered successfully';
      });
  }

  login() {
    this.auth.login(this.username, this.password)
      .subscribe(token => {
        this.auth.setToken(token);
        this.message = 'Login successful';
      });
  }
}

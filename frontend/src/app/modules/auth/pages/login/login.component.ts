import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  onSubmit() {
    if (!this.email || !this.password) {
      this.errorMessage = 'Complete todos los campos';
      return;
    }

    if (this.email === 'admin@xinyan.com' && this.password === 'admin123') {
      localStorage.setItem('token', 'fake-token');
      window.location.href = '/admin';
    } else {
      this.errorMessage = 'Usuario o contraseña incorrectos';
    }
  }

  togglePassword() {
    const input = document.getElementById('password') as HTMLInputElement;
    const icon = document.querySelector('.toggle-password') as HTMLElement;
    if (input) {
      const type = input.type === 'password' ? 'text' : 'password';
      input.type = type;
      icon?.classList.toggle('fa-eye');
      icon?.classList.toggle('fa-eye-slash');
    }
  }
}
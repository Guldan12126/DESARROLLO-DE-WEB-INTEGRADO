import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ToastService } from '../../../../shared/services/toast.service';

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
  showPassword = false;

  constructor(
    private router: Router,
    private http: HttpClient,
    private toastService: ToastService
  ) {}

  onSubmit() {
    if (!this.email || !this.password) {
      this.errorMessage = 'Complete todos los campos';
      return;
    }

    const loginData = { email: this.email, password: this.password };

    this.http.post<any>('http://localhost:8080/api/auth/login', loginData).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        this.toastService.success(`¡Bienvenido, ${res.nombre}!`);
        this.router.navigate(['/admin']);
      },
      error: () => {
        // El error ya es manejado por el ErrorInterceptor mostrando Toasts
      }
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }
}
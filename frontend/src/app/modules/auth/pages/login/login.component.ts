import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: '../../../../../scss/_login.scss'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  showPassword = false;
  isLoading = false;

  constructor(
    private router: Router,
    private http: HttpClient,
    private toastService: ToastService
  ) {}

  onSubmit() {
    this.errorMessage = '';

    if (!this.email || !this.password) {
      this.errorMessage = 'Complete todos los campos';
      this.toastService.error('Por favor, rellene los campos obligatorios');
      return;
    }

    const loginData = { email: this.email, password: this.password };
    this.isLoading = true;

    this.http.post<any>('http://localhost:8080/api/auth/login', loginData).subscribe({
      next: (res) => {
        console.log('Respuesta del servidor:', res); // 👈 Añade esto para depurar
        this.isLoading = false;
        localStorage.setItem('token', res.token);
        const userRole = res.rol || res.role; // 👈 Soporta ambos nombres de campo
        localStorage.setItem('role', userRole); 
        this.toastService.success(`¡Bienvenido, ${res.nombre}!`);
        
        if (userRole === 'ADMIN' || userRole === 'CAJERO') {
          this.router.navigate(['/admin/dashboard']);
        } else {
          this.router.navigate(['/dashboard-mozo']);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.password = ''; // Limpiar contraseña por seguridad
        this.errorMessage = 'Credenciales inválidas. Intente nuevamente.';
      }
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }
}
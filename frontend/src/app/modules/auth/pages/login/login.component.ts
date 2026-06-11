import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastService } from '../../../../shared/services/toast.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: '../../../../../scss/_login.scss',
})
export class LoginComponent {
  errorMessage: string = '';
  showPassword = false;
  isLoading = false;
  readonly loginForm;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private toastService: ToastService,
  ) {
    this.loginForm = this.formBuilder.nonNullable.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  get emailControl() {
    return this.loginForm.controls.email;
  }

  get passwordControl() {
    return this.loginForm.controls.password;
  }

  onSubmit(): void {
    this.errorMessage = '';

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.errorMessage = 'Revise el correo y la contraseña.';
      this.toastService.error('Corrija los datos del formulario antes de continuar.');
      return;
    }

    this.isLoading = true;

    this.authService.login(this.loginForm.getRawValue()).subscribe({
      next: (session) => {
        this.isLoading = false;
        this.toastService.success(`¡Bienvenido, ${session.nombre}!`);
        this.router.navigateByUrl(this.authService.getHomeRouteByRole(session.rol));
      },
      error: () => {
        this.isLoading = false;
        this.loginForm.patchValue({ password: '' });
        this.errorMessage = 'Credenciales inválidas. Intente nuevamente.';
      },
    });
  }

  usarCredencialDemo(email: string, password: string): void {
    this.loginForm.setValue({ email, password });
    this.errorMessage = '';
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }
}
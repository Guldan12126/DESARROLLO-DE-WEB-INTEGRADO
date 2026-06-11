import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { ToastService } from '../shared/services/toast.service';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../core/services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocurrió un error inesperado';

      if (error.status === 0) {
        errorMessage = 'No se pudo conectar con el backend en http://localhost:8080.';
        toastService.error(errorMessage);
      } else if (error.status === 400 && error.error?.errors) {
        const validationErrors = error.error.errors;
        Object.keys(validationErrors).forEach((field) => {
          toastService.error(`${validationErrors[field]}`);
        });
        errorMessage = error.error.message || 'Hay errores de validación en el formulario.';
      } else if (error.status === 401) {
        errorMessage = 'Tu sesión expiró o no tienes autorización para continuar.';
        authService.logout();
        toastService.error(errorMessage);
        router.navigate(['/login']);
      } else if (error.error && error.error.message) {
        errorMessage = error.error.message;
        toastService.error(errorMessage);
      }

      return throwError(() => new Error(errorMessage));
    })
  );
};

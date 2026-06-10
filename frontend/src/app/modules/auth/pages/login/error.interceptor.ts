import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastService } from '../../shared/services/toast.service';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocurrió un error inesperado';

      if (error.status === 400 && error.error.errors) {
        // Captura el mapa de errores de Bean Validation (@Valid) del backend
        const validationErrors = error.error.errors;
        Object.keys(validationErrors).forEach((field) => {
          // Muestra un Toast por cada campo que falló la validación
          toastService.error(`${validationErrors[field]}`);
        });
        errorMessage = error.error.message;
      } else if (error.error && error.error.message) {
        // Otros errores controlados por RuntimeException
        errorMessage = error.error.message;
        toastService.error(errorMessage);
      }

      return throwError(() => new Error(errorMessage));
    })
  );
};
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastService } from '../shared/services/toast.service';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocurrió un error inesperado';

      if (error.status === 400 && error.error.errors) {
        // Captura errores de validación (@Valid)
        const validationErrors = error.error.errors;
        Object.keys(validationErrors).forEach((field) => {
          toastService.error(`${validationErrors[field]}`);
        });
        errorMessage = error.error.message;
      } else if (error.error && error.error.message) {
        // Errores controlados por el Backend
        errorMessage = error.error.message;
        toastService.error(errorMessage);
      }

      return throwError(() => new Error(errorMessage));
    })
  );
};

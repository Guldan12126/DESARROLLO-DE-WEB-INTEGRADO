import { Component } from '@angular/core';
import { ToastService, Toast } from './app/shared/services/toast.service';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-toast',
  template: `
    <div class="toast-container">
      @for (toast of (toasts$ | async); track toast.id) {
        <div class="alert alert-{{ toast.type }}" (click)="remove(toast.id)">
          <i [class]="toast.icon"></i>
          <span>{{ toast.message }}</span>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 9999;
      width: 100%;
      max-width: 400px;
      pointer-events: none;
    }
    .alert {
      pointer-events: auto;
      cursor: pointer;
    }
  `],
  standalone: true,
  imports: [AsyncPipe] // Añadir AsyncPipe aquí
})
export class ToastComponent {
  toasts$: Observable<Toast[]>;

  constructor(private toastService: ToastService) {
    this.toasts$ = this.toastService.toasts$;
  }

  remove(id: number) {
    this.toastService.remove(id);
  }
}
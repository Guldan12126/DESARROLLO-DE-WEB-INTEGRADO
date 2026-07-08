import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'danger' | 'warning' | 'info';
  icon: string;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toasts: Toast[] = [];
  private toastsSubject = new BehaviorSubject<Toast[]>([]);

  get toasts$(): Observable<Toast[]> {
    return this.toastsSubject.asObservable();
  }

  show(message: string, type: Toast['type'] = 'info', duration: number = 5000) {
    const id = Date.now();
    const icon = this.getIcon(type);
    
    const toast: Toast = { id, message, type, icon };
    this.toasts.push(toast);
    this.toastsSubject.next([...this.toasts]);

    setTimeout(() => {
      this.remove(id);
    }, duration);
  }

  success(message: string) { this.show(message, 'success'); }
  error(message: string) { this.show(message, 'danger'); }
  warn(message: string) { this.show(message, 'warning'); }
  info(message: string) { this.show(message, 'info'); }

  remove(id: number) {
    this.toasts = this.toasts.filter(t => t.id !== id);
    this.toastsSubject.next([...this.toasts]);
  }

  private getIcon(type: Toast['type']): string {
    switch (type) {
      case 'success': return 'fas fa-check-circle';
      case 'danger': return 'fas fa-exclamation-circle';
      case 'warning': return 'fas fa-exclamation-triangle';
      case 'info': return 'fas fa-info-circle';
      default: return 'fas fa-bell';
    }
  }
}
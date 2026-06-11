import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = 'http://localhost:8080/api/notificaciones';
  
  // Usamos BehaviorSubject para que cualquier componente se entere cuando cambien las notificaciones
  private unreadCountSubject = new BehaviorSubject<number>(0);
  unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private http: HttpClient) {} // No llamar actualizarConteo aquí, se llama desde el componente con el rol

  getNotificaciones(rolDestino: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}?rol=${rolDestino}`);
  }

  actualizarConteo(rolDestino: string): void {
    this.http.get<any[]>(`${this.apiUrl}/no-leidas?rol=${rolDestino}`).subscribe({
      next: (notifs) => this.unreadCountSubject.next(notifs.length),
      error: (err) => console.error('Error al obtener notificaciones', err)
    });
  }

  marcarComoLeidas(): Observable<any> {
    return this.http.put(`${this.apiUrl}/marcar-leidas`, {});
  }
}
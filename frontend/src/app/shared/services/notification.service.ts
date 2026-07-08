import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface NotificationItem {
  id: string;
  titulo: string;
  mensaje: string;
  tipo: 'info' | 'success' | 'warning' | 'danger' | string;
  rolDestino: string;
  entidad?: string;
  entidadId?: number;
  fecha: string;
  leida: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private readonly apiUrl = `${environment.apiUrl}/notificaciones`;
  private readonly notificationsSubject = new BehaviorSubject<NotificationItem[]>([]);
  private readonly unreadCountSubject = new BehaviorSubject<number>(0);

  readonly notifications$ = this.notificationsSubject.asObservable();
  readonly unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadNotifications(rolDestino: string): Observable<NotificationItem[]> {
    return this.http.get<NotificationItem[]>(`${this.apiUrl}?rol=${rolDestino}`).pipe(
      map((notifications) =>
        [...notifications].sort(
          (a, b) => new Date(b.fecha).getTime() - new Date(a.fecha).getTime(),
        ),
      ),
      tap((notifications) => this.updateState(notifications)),
      catchError(() => {
        this.updateState([]);
        return of([]);
      }),
    );
  }

  getNotificaciones(rolDestino: string): Observable<NotificationItem[]> {
    return this.loadNotifications(rolDestino);
  }

  actualizarConteo(rolDestino: string): Observable<number> {
    return this.http
      .get<NotificationItem[]>(`${this.apiUrl}/no-leidas?rol=${rolDestino}`)
      .pipe(
        map((notifications) => notifications.length),
        tap((count) => this.unreadCountSubject.next(count)),
        catchError(() => {
          this.unreadCountSubject.next(0);
          return of(0);
        }),
      );
  }

  marcarComoLeidas(rolDestino: string): Observable<void> {
    return this.http
      .put<void>(`${this.apiUrl}/marcar-leidas?rol=${encodeURIComponent(rolDestino)}`, {});
  }

  private updateState(notifications: NotificationItem[]): void {
    this.notificationsSubject.next(notifications);
    this.unreadCountSubject.next(notifications.filter((item) => !item.leida).length);
  }
}

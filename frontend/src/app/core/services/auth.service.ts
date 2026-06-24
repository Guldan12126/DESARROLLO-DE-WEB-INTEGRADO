import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, map, Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  id: number;
  nombre: string;
  rol?: string;
  role?: string;
}

export interface UserSession {
  token: string;
  id: number;
  nombre: string;
  rol: string;
  email: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private readonly tokenKey = 'token';
  private readonly idKey = 'idUsuario';
  private readonly roleKey = 'role';
  private readonly nameKey = 'nombreUsuario';
  private readonly emailKey = 'emailUsuario';

  private readonly sessionSubject = new BehaviorSubject<UserSession | null>(
    this.getStoredSession(),
  );

  readonly session$ = this.sessionSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<UserSession> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      map((response) => this.buildSession(response, credentials.email)),
      tap((session) => this.persistSession(session)),
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.idKey);
    localStorage.removeItem(this.roleKey);
    localStorage.removeItem(this.nameKey);
    localStorage.removeItem(this.emailKey);
    this.sessionSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  hasRole(expectedRole: string): boolean {
    return this.getRole() === expectedRole;
  }

  getToken(): string | null {
    return this.sessionSubject.value?.token ?? localStorage.getItem(this.tokenKey);
  }

  getRole(): string {
    return this.sessionSubject.value?.rol ?? localStorage.getItem(this.roleKey) ?? '';
  }

  getUserId(): number | null {
    return this.sessionSubject.value?.id ?? (Number(localStorage.getItem(this.idKey)) || null);
  }

  getHomeRouteByRole(role?: string): string {
    switch ((role ?? this.getRole()).toUpperCase()) {
      case 'ADMIN':
        return '/admin/dashboard';
      case 'CAJERO':
        return '/cajero/dashboard';
      case 'COCINA':
        return '/cocina/dashboard';
      case 'MOZO':
        return '/mozo/dashboard';
      default:
        return '/login';
    }
  }

  private buildSession(response: LoginResponse, email: string): UserSession {
    return {
      token: response.token,
      id: response.id,
      nombre: response.nombre || 'Usuario',
      rol: (response.rol || response.role || '').toUpperCase(),
      email,
    };
  }

  private persistSession(session: UserSession): void {
    localStorage.setItem(this.tokenKey, session.token);
    localStorage.setItem(this.idKey, session.id.toString());
    localStorage.setItem(this.roleKey, session.rol);
    localStorage.setItem(this.nameKey, session.nombre);
    localStorage.setItem(this.emailKey, session.email);
    this.sessionSubject.next(session);
  }

  private getStoredSession(): UserSession | null {
    const token = localStorage.getItem(this.tokenKey);
    const id = localStorage.getItem(this.idKey);
    const rol = localStorage.getItem(this.roleKey);
    const nombre = localStorage.getItem(this.nameKey);
    const email = localStorage.getItem(this.emailKey) ?? '';

    if (!token || !id || !rol || !nombre) {
      return null;
    }

    return {
      token,
      id: Number(id),
      rol,
      nombre,
      email,
    };
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${environment.apiUrl}/reportes/dashboard`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene los datos del dashboard.
   * @param inicio Fecha de inicio (Date). Se convierte a ISO 8601 con hora requerido por el backend.
   * @param fin Fecha de fin (Date). Se convierte a ISO 8601 con hora requerido por el backend.
   */
  getStats(inicio?: Date | string, fin?: Date | string): Observable<any> {
    // ✅ Corregido: el backend usa @DateTimeFormat(iso = ISO.DATE_TIME)
    // que requiere formato completo: "2026-06-23T00:00:00"
    // Si recibimos un string tipo "2026-06-23" (de input type=date), lo completamos con la hora.
    const params: any = {};

    if (inicio) {
      const inicioStr = typeof inicio === 'string'
        ? (inicio.includes('T') ? inicio : `${inicio}T00:00:00`)
        : inicio.toISOString().split('.')[0];
      params['inicio'] = inicioStr;
    }

    if (fin) {
      const finStr = typeof fin === 'string'
        ? (fin.includes('T') ? fin : `${fin}T23:59:59`)
        : fin.toISOString().split('.')[0];
      params['fin'] = finStr;
    }

    return this.http.get<any>(this.apiUrl, { params });
  }
}

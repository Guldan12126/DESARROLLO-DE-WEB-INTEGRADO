import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CajaService {
  private readonly apiUrl = `${environment.apiUrl}/cajas`;

  constructor(private http: HttpClient) {}

  // GET /api/cajas
  listarCajas(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // GET /api/cajas/abierta
  obtenerCajaAbierta(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/abierta`);
  }

  // GET /api/cajas/movimientos — todos los movimientos de todas las cajas
  obtenerMovimientos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/movimientos`);
  }

  // GET /api/cajas/{id}/movimientos — movimientos de una caja específica
  obtenerMovimientosPorCaja(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/movimientos`);
  }

  // POST /api/cajas/abrir?usuarioId=&montoApertura=
  abrirCaja(usuarioId: number, montoApertura: number): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/abrir?usuarioId=${usuarioId}&montoApertura=${montoApertura}`,
      {}
    );
  }

  // POST /api/cajas/{id}/cerrar?montoCierre=
  cerrarCaja(id: number, montoCierre: number): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/${id}/cerrar?montoCierre=${montoCierre}`,
      {}
    );
  }

  // POST /api/cajas/{id}/movimiento?tipo=&monto=&descripcion=
  registrarMovimiento(id: number, tipo: string, monto: number, descripcion: string): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/${id}/movimiento?tipo=${tipo}&monto=${monto}&descripcion=${encodeURIComponent(descripcion)}`,
      {}
    );
  }
}
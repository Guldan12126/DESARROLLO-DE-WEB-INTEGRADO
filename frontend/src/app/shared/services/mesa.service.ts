import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MesaService {
  private apiUrl = `${environment.apiUrl}/mesas`;

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  listarActivas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activas`);
  }

  listarDisponibles(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/disponibles`);
  }

  listarOcupadas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/ocupadas`);
  }

  listarPendientes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/pendientes`);
  }

  obtenerPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  crearMesa(mesa: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, mesa);
  }

  actualizarMesa(id: number, mesa: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, mesa);
  }

  ocuparMesa(id: number, pedidoId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/ocupar?pedidoId=${pedidoId}`, {});
  }

  marcarPendientePago(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/pendiente-pago`, {});
  }

  liberarMesa(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/liberar`, {});
  }

  eliminarMesa(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}

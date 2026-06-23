import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class VentaService {
  private apiUrl = `${environment.apiUrl}/ventas`;

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  listarPorFecha(fecha: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/fecha?fecha=${fecha}`);
  }

  obtenerPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  registrarVenta(pedidoId: number, metodoPago: string, montoRecibido: number, cajeroId: number): Observable<any> {
    const params = new HttpParams()
      .set('pedidoId', pedidoId.toString())
      .set('metodoPago', metodoPago)
      .set('montoRecibido', montoRecibido.toString())
      .set('cajeroId', cajeroId.toString());
    
    return this.http.post<any>(`${this.apiUrl}/registrar`, null, { params });
  }

  obtenerVentasDelDia(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/total/dia`);
  }

  anularVenta(id: number, cajeroId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}?cajeroId=${cajeroId}`);
  }
}

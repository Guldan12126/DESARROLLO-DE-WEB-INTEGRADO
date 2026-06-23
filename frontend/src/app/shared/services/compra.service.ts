import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CompraService {
  private apiUrl = `${environment.apiUrl}/compras`;

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  registrarCompra(compra: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, compra);
  }
}

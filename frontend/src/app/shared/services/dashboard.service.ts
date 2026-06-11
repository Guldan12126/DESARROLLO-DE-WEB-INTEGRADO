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

  getStats(inicio?: string, fin?: string): Observable<any> {
    let params = {};
    
    if (inicio && fin) {
      params = { inicio, fin };
    }

    return this.http.get<any>(this.apiUrl, { params: params });
  }
}

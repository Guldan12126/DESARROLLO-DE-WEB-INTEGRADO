import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private apiUrl = `${environment.apiUrl}/reportes`;

  constructor(private http: HttpClient) {}

  /**
   * Descarga el reporte de ventas en PDF.
   * El backend devuelve un archivo binario PDF.
   */
  descargarVentasPdf(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/ventas-pdf`, {
      responseType: 'blob'
    });
  }

  /** Descarga el PDF y lo abre en una nueva pestaña del navegador */
  abrirPdfEnNuevaPestana(): void {
    this.descargarVentasPdf().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
      },
      error: (err) => {
        console.error('Error al descargar el reporte PDF:', err);
      }
    });
  }
}

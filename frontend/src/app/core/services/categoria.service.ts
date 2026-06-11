import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {
  // Ajusta esta URL según la configuración de tu backend
  private apiUrl = 'http://localhost:8080/api/categorias';

  constructor(private http: HttpClient) { }

  /** Obtiene la lista completa de categorías */
  obtenerCategorias(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  /** Obtiene una categoría por su ID */
  obtenerCategoriaPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  /** Crea una nueva categoría */
  crearCategoria(categoria: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, categoria);
  }

  /** Actualiza una categoría existente */
  actualizarCategoria(id: number, categoria: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, categoria);
  }

  /** Elimina una categoría */
  eliminarCategoria(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}
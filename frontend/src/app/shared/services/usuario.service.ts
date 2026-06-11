import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root' // Esto hace que el servicio sea un singleton y esté disponible en toda la aplicación
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios'; 
  private rolesApiUrl = 'http://localhost:8080/api/roles'; 
  constructor(private http: HttpClient) { }

  /**
   * Registra un nuevo usuario en el backend.
   * @param usuarioData Los datos del usuario a registrar.
   * @returns Un Observable con la respuesta del servidor.
   */
  registrarUsuario(usuarioData: any): Observable<any> {
    return this.http.post(this.apiUrl, usuarioData);
  }

  /**
   * Obtiene la lista de todos los usuarios del backend.
   * @returns Un Observable con la lista de usuarios.
   */
  obtenerUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtiene la lista de roles desde el backend.
   * @returns Un Observable con la lista de roles.
   */
  obtenerRoles(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/roles');
  }

  /**
   * Actualiza la descripción de un rol en el backend.
   * @param nombreRol El nombre del rol a actualizar.
   * @param nuevaDescripcion La nueva descripción del rol.
   * @returns Un Observable con la respuesta del servidor.
   */
  actualizarRol(nombreRol: string, nuevaDescripcion: string): Observable<any> {
    // Asumiendo que el backend espera un objeto con la descripción
    // y que el nombre del rol es parte de la URL para la actualización (PUT /api/roles/{nombreRol})
    return this.http.put(`${this.rolesApiUrl}/${nombreRol}`, { descripcion: nuevaDescripcion });
  }
}
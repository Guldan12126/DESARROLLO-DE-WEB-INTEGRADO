import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root' 
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuarios`;
  private rolesApiUrl = `${environment.apiUrl}/roles`;
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

  // Agrega este método dentro de la clase UsuarioService en usuario.service.ts

/**
 * Actualiza los datos de un usuario existente en la base de datos.
 * @param id ID del usuario a actualizar
 * @param usuario Objeto con los nuevos datos (nombre, email, rol)
 * @returns Observable con la respuesta del servidor
 */
actualizarUsuario(id: number, usuario: any): Observable<any> {
  // Ajusta la ruta si tu API usa algo distinto a /usuarios
  return this.http.put(`${this.apiUrl}/${id}`, usuario);
}

  /**
   * Obtiene la lista de roles desde el backend.
   * @returns Un Observable con la lista de roles.
   */
  obtenerRoles(): Observable<any[]> {
    return this.http.get<any[]>(this.rolesApiUrl);
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

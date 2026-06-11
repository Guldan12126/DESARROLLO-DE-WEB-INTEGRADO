import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { ToastService } from '../../../../shared/services/toast.service';
import { UsuarioService } from '../../../../shared/services/usuario.service';

interface Usuario {
  id: number;
  nombre: string;
  email: string;
  rol: string;
}

@Component({
  selector: 'app-usuarios-lista',
  standalone: false,
  templateUrl: './usuarios-lista.component.html',
  styleUrl: '../../../../../scss/_usuarios.scss', 
  encapsulation: ViewEncapsulation.None
})
export class UsuariosListaComponent implements OnInit {
  usuarios: Usuario[] = [];
  searchTerm: string = '';
  isLoading: boolean = false;
  
  // Variables para el modal de confirmación
  showDeleteModal: boolean = false;
  userIdToDelete: number | null = null;

  constructor(
    @Inject(UsuarioService) private usuarioService: UsuarioService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.isLoading = true;
    this.usuarioService.obtenerUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar usuarios:', err);
        this.toastService.error('Error al cargar la lista de usuarios.');
        this.isLoading = false;
      }
    });
  }

  buscarUsuarios(): void {
    // Implementar lógica de búsqueda aquí, o filtrar la lista actual
    // Por ahora, solo recargamos si el término de búsqueda está vacío
    if (this.searchTerm.trim() === '') {
      this.cargarUsuarios();
    } else {
      this.usuarios = this.usuarios.filter(user =>
        user.nombre.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.rol.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }

  editarUsuario(id: number): void {
    this.toastService.info(`Editar usuario con ID: ${id}`);
    // Lógica para navegar a la página de edición
  }

  eliminarUsuario(id: number): void {
    // En lugar de eliminar directamente, preparamos el modal
    this.userIdToDelete = id;
    this.showDeleteModal = true;
  }

  confirmarEliminacion(): void {
    if (this.userIdToDelete === null) return;

    this.usuarioService.eliminarUsuario(this.userIdToDelete).subscribe({
      next: () => {
        this.toastService.success('Usuario eliminado exitosamente.');
        this.cargarUsuarios();
        this.cerrarModal();
      },
      error: (err) => {
        console.error('Error al eliminar usuario:', err);
        this.toastService.error('Error al eliminar el usuario.');
        this.cerrarModal();
      }
    });
  }

  cerrarModal(): void {
    this.showDeleteModal = false;
    this.userIdToDelete = null;
  }
}
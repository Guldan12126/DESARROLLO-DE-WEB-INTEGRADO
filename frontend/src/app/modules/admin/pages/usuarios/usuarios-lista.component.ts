import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UsuarioService } from '../../../../shared/services/usuario.service'; // Asegura esta ruta
import { ToastService } from '../../../../shared/services/toast.service';
import { ConfirmModalComponent } from '../../../../shared/components/modal/confirm-modal.component';

@Component({
  selector: 'app-usuarios-lista',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ConfirmModalComponent],
  templateUrl: './usuarios-lista.component.html',
  styleUrl: '../../../../../scss/_usuarios.scss', 
})
export class UsuariosListaComponent implements OnInit {
  usuarios: any[] = [];
  usuariosFiltrados: any[] = []; 
  searchTerm: string = '';
  isLoading: boolean = false;
  
  // Estado para el modal de eliminación
  showDeleteModal: boolean = false;
  usuarioParaEliminar: number | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private toastService: ToastService,
    private router: Router
  ) {}

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
      error: () => {
        this.toastService.error('Error al cargar la lista de personal.');
        this.isLoading = false;
      }
    });
  }

  buscarUsuarios(): void {
    console.log('Buscando:', this.searchTerm);
  }

  editarUsuario(id: number): void {
    this.router.navigate(['/admin/usuarios/editar', id]);
  }

  // Métodos para el Modal
  eliminarUsuario(id: number): void {
    this.usuarioParaEliminar = id;
    this.showDeleteModal = true;
  }

  confirmarEliminacion(): void {
    if (this.usuarioParaEliminar) {
      this.usuarioService.eliminarUsuario(this.usuarioParaEliminar).subscribe({
        next: () => {
          this.toastService.success('Usuario eliminado correctamente.');
          this.cargarUsuarios();
          this.cerrarModal();
        },
        error: () => this.toastService.error('No se pudo eliminar el usuario.')
      });
    }
  }

  cerrarModal(): void {
    this.showDeleteModal = false;
    this.usuarioParaEliminar = null;
  }
}

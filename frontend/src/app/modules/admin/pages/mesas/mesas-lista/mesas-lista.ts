import { Component, OnInit, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { MesaService } from '../../../../../shared/services/mesa.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-mesas-lista',
  standalone: false,
  templateUrl: './mesas-lista.html',
  styleUrl: '../../../../../../scss/_mesas-lista.scss' // Compartiremos estilos generales de tablas
})
export class MesasLista implements OnInit {
  mesas: any[] = [];
  mesasFiltradas: any[] = [];
  searchTerm: string = '';
  isLoading: boolean = false;

  // Modal Eliminar
  showDeleteModal: boolean = false;
  mesaIdToDelete: number | null = null;
  mesaNumeroToDelete: number | null = null;

  // Modal Editar
  showEditModal: boolean = false;
  mesaEditando: any = null;
  editNumero: number | null = null;
  editCapacidad: number = 4;
  editUbicacion: string = 'SALON_PRINCIPAL';
  editActivo: boolean = true;
  isSaving: boolean = false;
  editErrors: any = {};

  constructor(
    @Inject(MesaService) private mesaService: MesaService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarMesas();
  }

  cargarMesas(): void {
    this.isLoading = true;
    this.mesaService.listarTodas().subscribe({
      next: (data) => {
        this.mesas = data;
        this.aplicarFiltros();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar mesas:', err);
        this.toastService.error('No se pudo cargar la lista de mesas.');
        this.isLoading = false;
      }
    });
  }

  aplicarFiltros(): void {
    let res = [...this.mesas];
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      res = res.filter(m => 
        m.numero?.toString().includes(term) || 
        m.ubicacion?.toLowerCase().includes(term) ||
        m.estado?.toLowerCase().includes(term)
      );
    }
    this.mesasFiltradas = res;
  }

  // ==== ELIMINAR ====
  confirmarEliminar(mesa: any): void {
    this.mesaIdToDelete = mesa.id;
    this.mesaNumeroToDelete = mesa.numero;
    this.showDeleteModal = true;
  }

  cerrarDeleteModal(): void {
    this.showDeleteModal = false;
    this.mesaIdToDelete = null;
    this.mesaNumeroToDelete = null;
  }

  eliminarMesa(): void {
    if (!this.mesaIdToDelete) return;
    this.mesaService.eliminarMesa(this.mesaIdToDelete).subscribe({
      next: () => {
        this.toastService.success(`Mesa #${this.mesaNumeroToDelete} eliminada.`);
        this.cerrarDeleteModal();
        this.cargarMesas();
      },
      error: () => {
        this.toastService.error('Error al eliminar la mesa. Puede que tenga pedidos asociados.');
        this.cerrarDeleteModal();
      }
    });
  }

  // ==== EDITAR ====
  abrirEditar(mesa: any): void {
    this.mesaEditando = { ...mesa };
    this.editNumero = mesa.numero;
    this.editCapacidad = mesa.capacidad;
    this.editUbicacion = mesa.ubicacion;
    this.editActivo = mesa.activo;
    this.editErrors = {};
    this.showEditModal = true;
  }

  cerrarEditModal(): void {
    this.showEditModal = false;
    this.mesaEditando = null;
  }

  validarEdicion(): boolean {
    this.editErrors = {};
    if (!this.editNumero || this.editNumero <= 0) this.editErrors['numero'] = 'Número de mesa inválido.';
    if (!this.editCapacidad || this.editCapacidad <= 0) this.editErrors['capacidad'] = 'Capacidad debe ser mayor a 0.';
    if (!this.editUbicacion) this.editErrors['ubicacion'] = 'La ubicación es obligatoria.';
    return Object.keys(this.editErrors).length === 0;
  }

  guardarEdicion(): void {
    if (!this.validarEdicion()) return;
    
    this.isSaving = true;
    const datos = {
      numero: this.editNumero,
      capacidad: this.editCapacidad,
      ubicacion: this.editUbicacion,
      activo: this.editActivo
    };

    this.mesaService.actualizarMesa(this.mesaEditando.id, datos).subscribe({
      next: () => {
        this.toastService.success(`Mesa #${this.editNumero} actualizada correctamente.`);
        this.cerrarEditModal();
        this.cargarMesas();
        this.isSaving = false;
      },
      error: () => {
        this.toastService.error('Error al actualizar la mesa.');
        this.isSaving = false;
      }
    });
  }

  getEstadoClase(estado: string): string {
    switch(estado) {
      case 'DISPONIBLE': return 'activo'; // Verde
      case 'OCUPADA': return 'inactivo'; // Rojo
      case 'PENDIENTE_PAGO': return 'role-cajero'; // Amarillo/Naranja
      default: return '';
    }
  }
}

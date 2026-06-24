import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-pedidos',
  standalone: false,
  templateUrl: './pedidos.html',
  styleUrl: './pedidos.scss'
})
export class Pedidos implements OnInit {
  pedidos: any[] = [];
  pedidosFiltrados: any[] = [];
  searchTerm: string = '';
  isLoading: boolean = false;

  constructor(
    private pedidoService: PedidoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.isLoading = true;
    this.pedidoService.listarActivos().subscribe({
      next: (data: any[]) => {
        this.pedidos = data;
        this.aplicarFiltros();
        this.isLoading = false;
      },
      error: (err: any) => {
        console.error(err);
        this.toastService.error('Error al cargar la lista de pedidos activos.');
        this.isLoading = false;
      }
    });
  }

  aplicarFiltros(): void {
    let res = [...this.pedidos];
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      res = res.filter(p => 
        p.id?.toString().includes(term) || 
        p.mesa?.numero?.toString().includes(term) ||
        p.estado?.toLowerCase().includes(term)
      );
    }
    this.pedidosFiltrados = res;
  }

  actualizarEstado(pedido: any, nuevoEstado: string): void {
    this.pedidoService.actualizarEstado(pedido.id, nuevoEstado).subscribe({
      next: () => {
        this.toastService.success(`Pedido #${pedido.id} cambiado a ${nuevoEstado}.`);
        this.cargarPedidos(); // Recargar para mostrar los cambios
      },
      error: (err: any) => {
        console.error(err);
        this.toastService.error('Error al actualizar el estado del pedido.');
      }
    });
  }

  getEstadoClase(estado: string): string {
    switch(estado) {
      case 'PENDIENTE': return 'role-cajero'; // Amarillo
      case 'EN_PREPARACION': return 'role-mesero'; // Azul
      case 'LISTO': return 'activo'; // Verde
      case 'ENTREGADO': return 'role-admin'; // Gris oscuro
      case 'PAGADO': return 'inactivo'; // Oculto/Gris claro
      case 'ANULADO': return 'inactivo';
      default: return '';
    }
  }

  calcularTotal(pedido: any): number {
    if (!pedido.detalles || pedido.detalles.length === 0) return 0;
    return pedido.detalles.reduce((total: number, d: any) => total + (d.precioUnitario * d.cantidad), 0);
  }
}

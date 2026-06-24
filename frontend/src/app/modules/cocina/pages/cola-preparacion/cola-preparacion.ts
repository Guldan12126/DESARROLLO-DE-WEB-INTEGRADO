import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-cola-preparacion',
  templateUrl: './cola-preparacion.html',
  styleUrl: '../../../../../scss/_dashboard.scss',
  standalone: false
})
export class ColaPreparacionComponent implements OnInit {
  pedidosPendientes: any[] = [];
  pedidosPreparando: any[] = [];
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
      next: (pedidos) => {
        // Separar por estado
        this.pedidosPendientes = pedidos.filter(p => p.estado === 'PENDIENTE');
        this.pedidosPreparando = pedidos.filter(p => p.estado === 'PREPARANDO');
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar pedidos en cocina:', err);
        this.toastService.error('Error al conectar con el servidor.');
        this.isLoading = false;
      }
    });
  }

  iniciarPreparacion(id: number): void {
    this.pedidoService.actualizarEstado(id, 'PREPARANDO').subscribe({
      next: () => {
        this.toastService.success('¡Pedido en preparación!');
        this.cargarPedidos();
      },
      error: (err) => {
        console.error('Error al iniciar preparación:', err);
        this.toastService.error('No se pudo actualizar el estado del pedido.');
      }
    });
  }

  terminarPreparacion(id: number): void {
    this.pedidoService.actualizarEstado(id, 'LISTO').subscribe({
      next: () => {
        this.toastService.success('¡Pedido listo para servir!');
        this.cargarPedidos();
      },
      error: (err) => {
        console.error('Error al terminar preparación:', err);
        this.toastService.error('No se pudo actualizar el estado del pedido.');
      }
    });
  }
}

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
  
  now: Date = new Date();
  intervalTimer: any;

  constructor(
    private pedidoService: PedidoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarPedidos();
    this.intervalTimer = setInterval(() => {
      this.now = new Date();
    }, 30000); // Actualizar cada 30 segundos
  }

  ngOnDestroy(): void {
    if (this.intervalTimer) {
      clearInterval(this.intervalTimer);
    }
  }

  cargarPedidos(): void {
    this.isLoading = true;
    
    this.pedidoService.listarActivos().subscribe({
      next: (pedidos) => {
        // Separar por estado
        this.pedidosPendientes = pedidos.filter(p => p.estado === 'PENDIENTE');
        this.pedidosPreparando = pedidos.filter(p => p.estado === 'EN_PREPARACION');
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
    this.pedidoService.actualizarEstado(id, 'EN_PREPARACION').subscribe({
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

  getTiempoTranscurrido(fecha: string): string {
    if (!fecha) return '0m';
    const start = new Date(fecha).getTime();
    const current = this.now.getTime();
    const diffMs = current - start;
    const diffMins = Math.floor(diffMs / 60000);
    
    if (diffMins < 0) return '0m';
    if (diffMins < 60) return `${diffMins}m`;
    const hours = Math.floor(diffMins / 60);
    const mins = diffMins % 60;
    return `${hours}h ${mins}m`;
  }

  cancelarPedido(id: number): void {
    if(confirm('¿Está seguro de cancelar este pedido por falta de insumos?')) {
      this.pedidoService.actualizarEstado(id, 'ANULADO').subscribe({
        next: () => {
          this.toastService.success('Pedido anulado.');
          this.cargarPedidos();
        },
        error: (err) => {
          console.error('Error al anular pedido:', err);
          this.toastService.error('No se pudo anular el pedido.');
        }
      });
    }
  }
}

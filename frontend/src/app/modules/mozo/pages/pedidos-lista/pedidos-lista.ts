import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-pedidos-lista',
  templateUrl: './pedidos-lista.html',
  styleUrl: './pedidos-lista.scss',
  standalone: false
})
export class PedidosListaComponent implements OnInit {
  pedidosActivos: any[] = [];
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
        // Mostrar solo los que importan al mozo ahora mismo: PENDIENTE, EN_PREPARACION, LISTO
        // NOTA: En un caso real, filtraríamos por ID de Mozo. Por ahora mostramos todos los activos.
        this.pedidosActivos = pedidos.filter(p => ['PENDIENTE', 'EN_PREPARACION', 'LISTO'].includes(p.estado))
                                     .sort((a, b) => new Date(b.fechaPedido).getTime() - new Date(a.fechaPedido).getTime());
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar pedidos activos:', err);
        this.toastService.error('Error al conectar con el servidor.');
        this.isLoading = false;
      }
    });
  }

  entregarPedido(id: number): void {
    this.pedidoService.actualizarEstado(id, 'ENTREGADO').subscribe({
      next: () => {
        this.toastService.success('¡Pedido marcado como ENTREGADO!');
        this.cargarPedidos();
      },
      error: (err) => {
        console.error('Error al entregar pedido:', err);
        this.toastService.error('No se pudo actualizar el estado.');
      }
    });
  }
}

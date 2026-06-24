import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-pedidos-historial',
  templateUrl: './pedidos-historial.html',
  styleUrl: './pedidos-historial.scss',
  standalone: false
})
export class PedidosHistorialComponent implements OnInit {
  historialPedidos: any[] = [];
  isLoading: boolean = false;

  constructor(
    private pedidoService: PedidoService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.isLoading = true;
    
    this.pedidoService.listarTodos().subscribe({
      next: (pedidos) => {
        // Historial: pedidos que ya fueron entregados o pagados.
        this.historialPedidos = pedidos.filter(p => ['ENTREGADO', 'PAGADO'].includes(p.estado))
                                       .sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime());
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar historial:', err);
        this.toastService.error('Error al conectar con el servidor.');
        this.isLoading = false;
      }
    });
  }
}

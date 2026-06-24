import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../shared/services/pedido.service';

@Component({
  selector: 'app-pedidos-completados',
  templateUrl: './pedidos-completados.html',
  styleUrl: './pedidos-completados.scss',
  standalone: false
})
export class PedidosCompletadosComponent implements OnInit {
  pedidosCompletados: any[] = [];
  isLoading: boolean = false;

  constructor(private pedidoService: PedidoService) {}

  ngOnInit(): void {
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.isLoading = true;
    this.pedidoService.listarTodos().subscribe({
      next: (pedidos) => {
        // Para la cocina, los completados son los que ya terminaron (LISTO, ENTREGADO, PAGADO)
        this.pedidosCompletados = pedidos
          .filter(p => ['LISTO', 'ENTREGADO', 'PAGADO'].includes(p.estado))
          .sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime());
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar pedidos completados', err);
        this.isLoading = false;
      }
    });
  }
}

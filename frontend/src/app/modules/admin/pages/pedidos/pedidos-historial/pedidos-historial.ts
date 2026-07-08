import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../../../shared/services/pedido.service';

@Component({
  selector: 'app-pedidos-historial',
  standalone: false,
  templateUrl: './pedidos-historial.html',
  styleUrl: './pedidos-historial.scss'
})
export class PedidosHistorial implements OnInit {
  pedidos: any[] = [];
  pedidosFiltrados: any[] = [];
  searchTerm: string = '';
  isLoading: boolean = false;

  constructor(private pedidoService: PedidoService) {}

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.isLoading = true;
    this.pedidoService.listarTodos().subscribe({
      next: (data: any[]) => {
        // Ordenar del más reciente al más antiguo
        this.pedidos = data.sort((a, b) => b.id - a.id);
        this.aplicarFiltros();
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  aplicarFiltros(): void {
    let res = [...this.pedidos];
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      res = res.filter(p => 
        p.id?.toString().includes(term) || 
        p.estado?.toLowerCase().includes(term)
      );
    }
    this.pedidosFiltrados = res;
  }

  getEstadoClase(estado: string): string {
    switch(estado) {
      case 'PENDIENTE': return 'role-cajero';
      case 'EN_PREPARACION': return 'role-mesero';
      case 'LISTO': return 'activo';
      case 'ENTREGADO': return 'role-admin';
      case 'PAGADO': return 'inactivo';
      default: return '';
    }
  }

  calcularTotal(pedido: any): number {
    if (!pedido.detalles || pedido.detalles.length === 0) return 0;
    return pedido.detalles.reduce((total: number, d: any) => total + (d.precioUnitario * d.cantidad), 0);
  }
}

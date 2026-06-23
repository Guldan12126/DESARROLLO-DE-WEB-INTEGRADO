import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../shared/services/venta.service';

@Component({
  selector: 'app-ventas',
  standalone: false,
  templateUrl: './ventas.html',
})
export class Ventas implements OnInit {
  ventas: any[] = [];
  ventasFiltradas: any[] = [];
  searchTerm: string = '';
  isLoading: boolean = false;

  constructor(private ventaService: VentaService) {}

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas(): void {
    this.isLoading = true;
    this.ventaService.listarTodas().subscribe({
      next: (data: any[]) => {
        // Ordenar de la más reciente a la más antigua
        this.ventas = data.sort((a, b) => b.id - a.id);
        this.aplicarFiltros();
        this.isLoading = false;
      },
      error: (err: any) => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  aplicarFiltros(): void {
    let res = [...this.ventas];
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      res = res.filter(v => 
        v.id?.toString().includes(term) || 
        v.metodoPago?.toLowerCase().includes(term) ||
        v.pedido?.mesa?.numero?.toString().includes(term)
      );
    }
    this.ventasFiltradas = res;
  }
}

import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../shared/services/venta.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-ventas-historial',
  templateUrl: './ventas-historial.html',
  standalone: false
})
export class VentasHistorialComponent implements OnInit {
  ventas: any[] = [];
  ventasFiltradas: any[] = [];
  isLoading: boolean = false;
  searchTerm: string = '';

  constructor(
    private ventaService: VentaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas(): void {
    this.isLoading = true;
    this.ventaService.listarTodas().subscribe({
      next: (ventas) => {
        // Ordenar por fecha descendente
        this.ventas = ventas.sort((a, b) => new Date(b.fechaHora).getTime() - new Date(a.fechaHora).getTime());
        this.ventasFiltradas = [...this.ventas];
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar ventas:', err);
        this.toastService.error('Error al cargar historial de ventas');
        this.isLoading = false;
      }
    });
  }

  aplicarFiltros(): void {
    if (!this.searchTerm.trim()) {
      this.ventasFiltradas = [...this.ventas];
      return;
    }
    
    const term = this.searchTerm.toLowerCase();
    this.ventasFiltradas = this.ventas.filter(v => 
      v.numeroComprobante?.toLowerCase().includes(term) ||
      v.metodoPago?.toLowerCase().includes(term) ||
      v.pedido?.id.toString().includes(term)
    );
  }
}

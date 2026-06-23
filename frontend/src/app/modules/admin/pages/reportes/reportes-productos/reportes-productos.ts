import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../../shared/services/venta.service';
import { ReporteService } from '../../../../../shared/services/reporte.service';

@Component({
  selector: 'app-reportes-productos',
  standalone: false,
  templateUrl: './reportes-productos.html',
})
export class ReportesProductos implements OnInit {
  isLoading: boolean = false;
  productos: any[] = [];

  constructor(
    private ventaService: VentaService,
    private reporteService: ReporteService
  ) {}

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.isLoading = true;
    this.ventaService.listarTodas().subscribe({
      next: (ventas: any[]) => {
        this.procesarTopProductos(ventas);
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  procesarTopProductos(ventas: any[]): void {
    const agrupado: any = {};

    ventas.forEach(v => {
      if (v.pedido && v.pedido.detalles) {
        v.pedido.detalles.forEach((d: any) => {
          const prodId = d.producto.id;
          if (!agrupado[prodId]) {
            agrupado[prodId] = {
              producto: d.producto.nombre,
              categoria: d.producto.categoria?.nombre || 'General',
              cantidadVendida: 0,
              ingresosGenerados: 0
            };
          }
          agrupado[prodId].cantidadVendida += d.cantidad;
          agrupado[prodId].ingresosGenerados += (d.cantidad * d.precioUnitario);
        });
      }
    });

    // Ordenar por cantidad vendida descendente
    this.productos = Object.values(agrupado).sort((a: any, b: any) => b.cantidadVendida - a.cantidadVendida);
  }

  descargarPdf(): void {
    this.reporteService.abrirPdfEnNuevaPestana();
  }
}

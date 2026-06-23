import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../shared/services/venta.service';
import { ReporteService } from '../../../../shared/services/reporte.service';

@Component({
  selector: 'app-reportes',
  standalone: false,
  templateUrl: './reportes.html',
})
export class Reportes implements OnInit {
  isLoading: boolean = false;
  ventas: any[] = [];
  ventasPorDia: any[] = [];
  totalGeneral: number = 0;
  totalTransacciones: number = 0;

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
      next: (data: any[]) => {
        this.ventas = data;
        this.procesarReporteDiario();
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  procesarReporteDiario(): void {
    const agrupado: any = {};
    this.totalGeneral = 0;
    this.totalTransacciones = this.ventas.length;

    this.ventas.forEach(v => {
      const fecha = new Date(v.fechaHora).toISOString().split('T')[0]; // YYYY-MM-DD
      if (!agrupado[fecha]) {
        agrupado[fecha] = { fecha, cantidad: 0, total: 0 };
      }
      agrupado[fecha].cantidad++;
      agrupado[fecha].total += v.totalVenta;
      this.totalGeneral += v.totalVenta;
    });

    // Convertir objeto a arreglo y ordenar por fecha descendente
    this.ventasPorDia = Object.values(agrupado).sort((a: any, b: any) => b.fecha.localeCompare(a.fecha));
  }

  descargarPdf(): void {
    this.reporteService.abrirPdfEnNuevaPestana();
  }
}

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
      // El campo en el backend se llama 'fecha', no 'fechaHora'
      const fechaRaw = v.fecha;
      if (!fechaRaw) return; // Saltar registros sin fecha

      const fechaObj = new Date(fechaRaw);
      if (isNaN(fechaObj.getTime())) return; // Saltar fechas inválidas

      const fecha = fechaObj.toISOString().split('T')[0]; // YYYY-MM-DD
      if (!agrupado[fecha]) {
        agrupado[fecha] = { fecha, cantidad: 0, total: 0 };
      }
      agrupado[fecha].cantidad++;
      // El campo en el backend se llama 'monto', no 'totalVenta'
      agrupado[fecha].total += v.monto || 0;
      this.totalGeneral += v.monto || 0;
    });

    // Convertir objeto a arreglo y ordenar por fecha descendente
    this.ventasPorDia = Object.values(agrupado).sort((a: any, b: any) => b.fecha.localeCompare(a.fecha));
  }

  descargarPdf(): void {
    this.reporteService.abrirPdfEnNuevaPestana();
  }
}

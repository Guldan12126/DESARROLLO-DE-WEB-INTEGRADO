import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../../shared/services/venta.service';
import { ReporteService } from '../../../../../shared/services/reporte.service';

@Component({
  selector: 'app-reportes-mensuales',
  standalone: false,
  templateUrl: './reportes-mensuales.html',
})
export class ReportesMensuales implements OnInit {
  isLoading: boolean = false;
  ventasPorMes: any[] = [];
  totalGeneral: number = 0;

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
        this.procesarReporteMensual(data);
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  procesarReporteMensual(ventas: any[]): void {
    const agrupado: any = {};
    this.totalGeneral = 0;

    ventas.forEach(v => {
      // YYYY-MM — el campo del backend se llama 'fecha'
      const fechaRaw = v.fecha;
      if (!fechaRaw) return;
      const fechaObj = new Date(fechaRaw);
      if (isNaN(fechaObj.getTime())) return;
      const mes = fechaObj.toISOString().substring(0, 7);
      
      if (!agrupado[mes]) {
        agrupado[mes] = { mes, cantidad: 0, total: 0 };
      }
      agrupado[mes].cantidad++;
      agrupado[mes].total += v.monto || 0;
      this.totalGeneral += v.monto || 0;
    });

    this.ventasPorMes = Object.values(agrupado).sort((a: any, b: any) => b.mes.localeCompare(a.mes));
  }

  descargarPdf(): void {
    this.reporteService.abrirPdfEnNuevaPestana();
  }
}

import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../../shared/services/venta.service';
import { ReporteService } from '../../../../../shared/services/reporte.service';

@Component({
  selector: 'app-reportes-semanales',
  standalone: false,
  templateUrl: './reportes-semanales.html',
})
export class ReportesSemanales implements OnInit {
  isLoading: boolean = false;
  ventas: any[] = [];
  ventasPorSemana: any[] = [];
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
        this.ventas = data;
        this.procesarReporteSemanal();
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  getWeekNumber(d: Date): [number, number] {
    // Retorna [Año, Número de semana ISO]
    const date = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
    const dayNum = date.getUTCDay() || 7;
    date.setUTCDate(date.getUTCDate() + 4 - dayNum);
    const yearStart = new Date(Date.UTC(date.getUTCFullYear(),0,1));
    const weekNo = Math.ceil((((date.getTime() - yearStart.getTime()) / 86400000) + 1)/7);
    return [date.getUTCFullYear(), weekNo];
  }

  procesarReporteSemanal(): void {
    const agrupado: any = {};
    this.totalGeneral = 0;

    this.ventas.forEach(v => {
      // El campo del backend se llama 'fecha', no 'fechaHora'
      const fechaRaw = v.fecha;
      if (!fechaRaw) return;
      const d = new Date(fechaRaw);
      if (isNaN(d.getTime())) return;

      const [year, week] = this.getWeekNumber(d);
      const key = `${year}-W${week.toString().padStart(2, '0')}`;
      
      if (!agrupado[key]) {
        agrupado[key] = { semana: key, year, week, cantidad: 0, total: 0 };
      }
      agrupado[key].cantidad++;
      agrupado[key].total += v.monto || 0;
      this.totalGeneral += v.monto || 0;
    });

    // Ordenar de la semana más reciente a la más antigua
    this.ventasPorSemana = Object.values(agrupado).sort((a: any, b: any) => b.semana.localeCompare(a.semana));
  }

  descargarPdf(): void {
    this.reporteService.abrirPdfEnNuevaPestana();
  }
}

import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../../shared/services/venta.service';
import { ReporteService } from '../../../../../shared/services/reporte.service';

@Component({
  selector: 'app-reportes-mensuales',
  standalone: false,
  templateUrl: './reportes-mensuales.html',
  styleUrls: ['../../../../../../scss/_reportes-mensuales.scss']
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
      error: () => {
        this.isLoading = false;
      }
    });
  }

  private obtenerMesFiscal(fechaRaw: unknown): string | null {
    if (typeof fechaRaw !== 'string' || !fechaRaw) return null;

    // Si ya viene como YYYY-MM o YYYY-MM-DD, evita convertir con toISOString (problema de zona horaria)
    const soloMes = fechaRaw.match(/^\d{4}-\d{2}$/);
    if (soloMes) return fechaRaw;

    const soloDia = fechaRaw.match(/^\d{4}-\d{2}-\d{2}/);
    if (soloDia) return fechaRaw.substring(0, 7);

    // Fallback: intenta parsear, pero normaliza en local (en vez de toISOString) para minimizar desplazamientos
    const d = new Date(fechaRaw);
    if (isNaN(d.getTime())) return null;
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    return `${y}-${m}`;
  }

  procesarReporteMensual(ventas: any[]): void {
    const agrupado: Record<string, { mes: string; cantidad: number; total: number }> = {};
    this.totalGeneral = 0;

    ventas.forEach(v => {
      const mes = this.obtenerMesFiscal(v?.fecha);
      if (!mes) return;

      const monto = typeof v?.monto === 'number' ? v.monto : Number(v?.monto) || 0;

      if (!agrupado[mes]) {
        agrupado[mes] = { mes, cantidad: 0, total: 0 };
      }

      agrupado[mes].cantidad++;
      agrupado[mes].total += monto;
      this.totalGeneral += monto;
    });

    this.ventasPorMes = Object.values(agrupado).sort((a, b) => b.mes.localeCompare(a.mes));
  }


  descargarPdf(): void {
    this.reporteService.abrirPdfEnNuevaPestana();
  }
}

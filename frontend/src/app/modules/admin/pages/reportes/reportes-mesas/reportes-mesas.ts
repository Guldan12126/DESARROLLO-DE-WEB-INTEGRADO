import { Component, OnInit } from '@angular/core';
import { VentaService } from '../../../../../shared/services/venta.service';
import { ReporteService } from '../../../../../shared/services/reporte.service';

@Component({
  selector: 'app-reportes-mesas',
  standalone: false,
  templateUrl: './reportes-mesas.html',
})
export class ReportesMesas implements OnInit {
  isLoading: boolean = false;
  mesas: any[] = [];

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
        this.procesarTopMesas(ventas);
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  procesarTopMesas(ventas: any[]): void {
    const agrupado: any = {};

    ventas.forEach(v => {
      if (v.pedido && v.pedido.mesa) {
        const mesaNum = v.pedido.mesa.numero;
        if (!agrupado[mesaNum]) {
          agrupado[mesaNum] = {
            numero: mesaNum,
            capacidad: v.pedido.mesa.capacidad,
            visitas: 0,
            ingresos: 0
          };
        }
        agrupado[mesaNum].visitas++;
        agrupado[mesaNum].ingresos += v.monto || 0;
      } else {
        // Pedidos para llevar
        if (!agrupado['Llevar']) {
          agrupado['Llevar'] = { numero: 'Llevar', capacidad: '-', visitas: 0, ingresos: 0 };
        }
        agrupado['Llevar'].visitas++;
        agrupado['Llevar'].ingresos += v.monto || 0;
      }
    });

    // Ordenar por ingresos descendente
    this.mesas = Object.values(agrupado).sort((a: any, b: any) => b.ingresos - a.ingresos);
  }

  descargarPdf(): void {
    this.reporteService.abrirPdfEnNuevaPestana();
  }
}

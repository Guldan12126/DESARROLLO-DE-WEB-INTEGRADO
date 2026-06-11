import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { DashboardService } from '../../../../shared/services/dashboard.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard-admin.html',
  styleUrl: '../../../../../scss/_dashboard.scss',
  standalone: false
})
export class DashboardComponent implements OnInit {
  @ViewChild('salesChart') salesChartCanvas!: ElementRef;
  @ViewChild('categoryChart') categoryChartCanvas!: ElementRef;

  stats: any = {};
  
  // Almacenamos las instancias de los gráficos
  salesChartInstRef?: Chart;
  categoryChartInstRef?: Chart;

  // Fechas del filtro
  fechaInicio: string = '';
  fechaFin: string = '';

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    // Cargar con valores por defecto al inicio
    this.aplicarFiltro();
  }

  aplicarFiltro() {
    // Convertimos las fechas a formato ISO para el backend si el usuario las seleccionó
    const inicioStr = this.fechaInicio ? `${this.fechaInicio}T00:00:00` : undefined;
    const finStr = this.fechaFin ? `${this.fechaFin}T23:59:59` : undefined;

    this.dashboardService.getStats(inicioStr, finStr).subscribe(data => {
      this.stats = data;
      this.initCharts(data);
    });
  }

  initCharts(data: any) {
    // Destruir gráficos anteriores si existen
    if (this.salesChartInstRef) this.salesChartInstRef.destroy();
    if (this.categoryChartInstRef) this.categoryChartInstRef.destroy();

    // Gráfico de Ventas (Línea)
    const salesCtx = this.salesChartCanvas.nativeElement.getContext('2d');
    this.salesChartInstRef = new Chart(salesCtx, {
      type: 'line',
      data: {
        labels: data.ventasSemanalLabels,
        datasets: [{
          label: 'Ventas Diarias (S/)',
          data: data.ventasSemanalData,
          borderColor: '#8b0000',
          backgroundColor: 'rgba(139, 0, 0, 0.1)',
          fill: true,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } }
      }
    });

    // Gráfico de Categorías (Dona)
    const catCtx = this.categoryChartCanvas.nativeElement.getContext('2d');
    this.categoryChartInstRef = new Chart(catCtx, {
      type: 'doughnut',
      data: {
        labels: data.categoriasLabels,
        datasets: [{
          data: data.categoriasData,
          backgroundColor: ['#8b0000', '#ffd700', '#4a0000', '#ffec8b'],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom', labels: { usePointStyle: true } }
        }
      }
    });
  }
}
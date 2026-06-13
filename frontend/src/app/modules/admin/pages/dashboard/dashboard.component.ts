import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { DashboardService } from '../../../../shared/services/dashboard.service';
import { Chart, registerables } from 'chart.js';
import { ToastService } from '../../../../shared/services/toast.service'; 
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
  
  salesChartInstRef?: Chart;
  categoryChartInstRef?: Chart;

  // Fechas del filtro
  fechaInicio: string = '';
  fechaFin: string = '';
  rangoActivo: string = '7dias';
  isLoading: boolean = false; 

  constructor(
    private dashboardService: DashboardService,
    private toastService: ToastService 
  ) {}

  ngOnInit(): void {
    this.fechaInicio = this.getFormattedDate(new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)); // Hace 7 días
    this.fechaFin = this.getFormattedDate(new Date()); 
    this.aplicarFiltro();
  }

  seleccionarRango(rango: string) {
    if (this.isLoading) return; 

    this.rangoActivo = rango;
    const hoy = new Date();
    let inicio = new Date();

    switch (rango) {
      case 'hoy':
        inicio = new Date(hoy);
        break;
      case '7dias':
        inicio.setDate(hoy.getDate() - 7);
        break;
      case '30dias':
        inicio.setDate(hoy.getDate() - 30);
        break;
      case 'mes':
        inicio = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
        break;
      case 'anio':
        inicio = new Date(hoy.getFullYear(), 0, 1);
        break;
    }

    this.fechaInicio = this.getFormattedDate(inicio);
    this.fechaFin = this.getFormattedDate(hoy);
    this.aplicarFiltro();
  }
  
  aplicarFiltro() {
    const inicioStr = this.fechaInicio ? `${this.fechaInicio}T00:00:00` : undefined;
    const finStr = this.fechaFin ? `${this.fechaFin}T23:59:59` : undefined;

    this.isLoading = true; 

    this.dashboardService.getStats(inicioStr, finStr).subscribe({
      next: (data) => {
        // Primero quitamos el cargando para que el HTML renderice los canvas
        this.isLoading = false; 

        if (data) {
          this.stats = data;
          this.stats.totalMesas = 15; 
          // Usamos un timeout de 0ms para esperar al siguiente ciclo de renderizado
          setTimeout(() => this.initCharts(data), 0);
        } else {
          this.cargarDatosPrueba(); 
        }
      },
      error: (err) => {
        console.error('Error al actualizar Dashboard:', err);
        this.toastService.error('Conectando con servidor local...');
        this.cargarDatosPrueba(); 
        this.isLoading = false;
      }
    });
  }

  // Si en caso que si la base de datos falla se hace prueba de datos para demostrar
  private cargarDatosPrueba() {
    this.stats = {
      totalVentas: 1250.50,
      totalPedidos: 45,
      insumosCriticos: 3,
      mesasOcupadas: 8,
      totalMesas: 15,
      ventasSemanalLabels: ['Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab', 'Dom'],
      ventasSemanalData: [400, 700, 500, 900, 1100, 2000, 1500],
      categoriasLabels: ['Chaufas', 'Sopas', 'Entradas', 'Bebidas'],
      categoriasData: [40, 20, 25, 15]
    };
    setTimeout(() => this.initCharts(this.stats), 100);
  }

  private getFormattedDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  initCharts(data: any) {
    if (!this.salesChartCanvas || !this.categoryChartCanvas) {
      console.warn('Canvas elements not yet available for chart initialization.');
      return;
    }

    // Destruir gráficos anteriores si existen
    if (this.salesChartInstRef) this.salesChartInstRef.destroy();
    if (this.categoryChartInstRef) this.categoryChartInstRef.destroy();

    // Gráfico de Ventas (Línea)
    if (this.salesChartCanvas && this.salesChartCanvas.nativeElement) {
      const salesCtx = this.salesChartCanvas.nativeElement.getContext('2d');
      this.salesChartInstRef = new Chart(salesCtx, {
        type: 'bar', 
        data: {
          labels: data.ventasSemanalLabels,
          datasets: [{
            label: 'Ventas Diarias (S/)',
            data: data.ventasSemanalData,
            // Personalización de colores
            backgroundColor: 'rgba(139, 0, 0, 0.7)', // Color de las barras con transparencia
            borderColor: '#8b0000',                 // Color del borde de las barras
            borderWidth: 1                          // Grosor del borde
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
      });
    }

    // Gráfico de Categorías (Dona)
    if (this.categoryChartCanvas && this.categoryChartCanvas.nativeElement) {
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
        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom', labels: { usePointStyle: true } } } }
      });
    }
  }
}
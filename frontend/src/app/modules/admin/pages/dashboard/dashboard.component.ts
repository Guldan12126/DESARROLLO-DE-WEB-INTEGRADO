import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { ToastService } from '../../../../shared/services/toast.service';
import { VentaService } from '../../../../shared/services/venta.service';
import { ProductoService } from '../../../../shared/services/producto.service';
import { MesaService } from '../../../../shared/services/mesa.service';
import { forkJoin } from 'rxjs';

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
    private ventaService: VentaService,
    private productoService: ProductoService,
    private mesaService: MesaService,
    private toastService: ToastService 
  ) {}

  ngOnInit(): void {
    this.seleccionarRango('7dias');
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
    this.isLoading = true; 

    forkJoin({
      ventas: this.ventaService.listarTodas(),
      productos: this.productoService.listarTodos(),
      mesas: this.mesaService.listarActivas()
    }).subscribe({
      next: (res: any) => {
        this.procesarDatos(res.ventas, res.productos, res.mesas);
        this.isLoading = false;
        setTimeout(() => this.initCharts(this.stats), 0);
      },
      error: (err) => {
        console.error('Error al actualizar Dashboard:', err);
        this.toastService.error('Error al obtener datos del servidor.');
        this.isLoading = false;
      }
    });
  }

  procesarDatos(ventas: any[], productos: any[], mesas: any[]) {
    // Filtrar ventas por el rango seleccionado
    const fInicio = new Date(this.fechaInicio + 'T00:00:00').getTime();
    const fFin = new Date(this.fechaFin + 'T23:59:59').getTime();

    const ventasFiltradas = ventas.filter(v => {
      const vDate = new Date(v.fechaHora).getTime();
      return vDate >= fInicio && vDate <= fFin;
    });

    // 1. Total Ventas & Pedidos
    const totalVentas = ventasFiltradas.reduce((acc, v) => acc + v.totalVenta, 0);
    const totalPedidos = ventasFiltradas.length;

    // 2. Insumos Críticos (Stock <= 5)
    const insumosCriticos = productos.filter(p => p.stock <= 5).length;

    // 3. Mesas
    const mesasOcupadas = mesas.filter(m => m.estado === 'OCUPADA' || m.estado === 'PENDIENTE_PAGO').length;
    const totalMesas = mesas.length;

    // 4. Gráfico Ventas (Agrupar por Fecha YYYY-MM-DD)
    const ventasPorDia: any = {};
    ventasFiltradas.forEach(v => {
      const fecha = new Date(v.fechaHora).toISOString().split('T')[0];
      if (!ventasPorDia[fecha]) ventasPorDia[fecha] = 0;
      ventasPorDia[fecha] += v.totalVenta;
    });

    // Ordenar fechas para el gráfico
    const labelsVentas = Object.keys(ventasPorDia).sort();
    const dataVentas = labelsVentas.map(date => ventasPorDia[date]);

    // 5. Gráfico Categorías
    const categorias: any = {};
    ventasFiltradas.forEach(v => {
      if (v.pedido && v.pedido.detalles) {
        v.pedido.detalles.forEach((det: any) => {
          const cat = det.producto?.categoria?.nombre || 'General';
          if (!categorias[cat]) categorias[cat] = 0;
          categorias[cat] += (det.cantidad * det.precioUnitario); // Sumar ingresos por categoría
        });
      }
    });

    const labelsCategorias = Object.keys(categorias);
    const dataCategorias = labelsCategorias.map(cat => categorias[cat]);

    this.stats = {
      totalVentas,
      totalPedidos,
      insumosCriticos,
      mesasOcupadas,
      totalMesas,
      ventasSemanalLabels: labelsVentas.length > 0 ? labelsVentas : ['Sin datos'],
      ventasSemanalData: dataVentas.length > 0 ? dataVentas : [0],
      categoriasLabels: labelsCategorias.length > 0 ? labelsCategorias : ['Sin datos'],
      categoriasData: dataCategorias.length > 0 ? dataCategorias : [0]
    };
  }

  private getFormattedDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  initCharts(data: any) {
    if (!this.salesChartCanvas || !this.categoryChartCanvas) return;

    if (this.salesChartInstRef) this.salesChartInstRef.destroy();
    if (this.categoryChartInstRef) this.categoryChartInstRef.destroy();

    const salesCtx = this.salesChartCanvas.nativeElement.getContext('2d');
    this.salesChartInstRef = new Chart(salesCtx, {
      type: 'bar', 
      data: {
        labels: data.ventasSemanalLabels,
        datasets: [{
          label: 'Ventas Diarias (S/)',
          data: data.ventasSemanalData,
          backgroundColor: 'rgba(139, 0, 0, 0.7)',
          borderColor: '#8b0000',
          borderWidth: 1
        }]
      },
      options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
    });

    const catCtx = this.categoryChartCanvas.nativeElement.getContext('2d');
    this.categoryChartInstRef = new Chart(catCtx, {
      type: 'doughnut',
      data: {
        labels: data.categoriasLabels,
        datasets: [{
          data: data.categoriasData,
          backgroundColor: ['#8b0000', '#ffd700', '#4a0000', '#ffec8b', '#dc3545', '#ffc107'],
          borderWidth: 0
        }]
      },
      options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom', labels: { usePointStyle: true } } } }
    });
  }
}
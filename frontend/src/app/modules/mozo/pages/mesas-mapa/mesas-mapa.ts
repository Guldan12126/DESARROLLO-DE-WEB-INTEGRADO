import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MesaService } from '../../../../shared/services/mesa.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-mesas-mapa',
  templateUrl: './mesas-mapa.html',
  styleUrl: './mesas-mapa.scss',
  standalone: false
})
export class MesasMapaComponent implements OnInit {
  mesas: any[] = [];
  isLoading: boolean = false;

  constructor(
    private mesaService: MesaService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarMesas();
  }

  cargarMesas(): void {
    this.isLoading = true;
    this.mesaService.listarTodas().subscribe({
      next: (data) => {
        // Ordenar mesas por número para que el mapa tenga sentido visual
        this.mesas = data.sort((a, b) => a.numero - b.numero);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar mesas:', err);
        this.toastService.error('Error al conectar con el servidor.');
        this.isLoading = false;
      }
    });
  }

  tomarPedido(mesa: any): void {
    if (mesa.estado === 'DISPONIBLE') {
      this.router.navigate(['/mozo/pedidos/nuevo'], { queryParams: { mesaId: mesa.id } });
    } else if (mesa.estado === 'OCUPADA' && mesa.pedidoActualId) {
      this.router.navigate(['/mozo/pedidos/nuevo'], { queryParams: { mesaId: mesa.id, pedidoId: mesa.pedidoActualId } });
    } else {
      this.toastService.info('La mesa no puede recibir pedidos en este momento.');
    }
  }
}

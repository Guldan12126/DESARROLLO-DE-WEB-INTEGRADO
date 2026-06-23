import { Component, OnInit, Inject } from '@angular/core';
import { MesaService } from '../../../../../shared/services/mesa.service';
import { ToastService } from '../../../../../shared/services/toast.service';

@Component({
  selector: 'app-mesas-mapa',
  standalone: false,
  templateUrl: './mesas-mapa.html',
  styleUrl: './mesas-mapa.scss'
})
export class MesasMapa implements OnInit {
  mesas: any[] = [];
  mesasSalon: any[] = [];
  mesasTerraza: any[] = [];
  mesasVIP: any[] = [];
  isLoading: boolean = false;

  constructor(
    @Inject(MesaService) private mesaService: MesaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarMesas();
  }

  cargarMesas(): void {
    this.isLoading = true;
    this.mesaService.listarActivas().subscribe({
      next: (data) => {
        this.mesas = data;
        this.agruparPorUbicacion();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar mesas:', err);
        this.toastService.error('Error al cargar el mapa de mesas.');
        this.isLoading = false;
      }
    });
  }

  agruparPorUbicacion(): void {
    this.mesasSalon = this.mesas.filter(m => m.ubicacion === 'SALON_PRINCIPAL');
    this.mesasTerraza = this.mesas.filter(m => m.ubicacion === 'TERRAZA');
    this.mesasVIP = this.mesas.filter(m => m.ubicacion === 'VIP');
  }

  // Permite simular un cambio de estado desde el admin
  cambiarEstado(mesa: any, nuevoEstado: string): void {
    // Si se pasa a disponible, la liberamos
    if (nuevoEstado === 'DISPONIBLE') {
      this.mesaService.liberarMesa(mesa.id).subscribe({
        next: () => {
          this.toastService.success(`Mesa ${mesa.numero} liberada.`);
          this.cargarMesas();
        },
        error: () => this.toastService.error('Error al liberar la mesa.')
      });
    } 
    // Si se pasa a pendiente de pago
    else if (nuevoEstado === 'PENDIENTE_PAGO') {
      this.mesaService.marcarPendientePago(mesa.id).subscribe({
        next: () => {
          this.toastService.success(`Mesa ${mesa.numero} marcada como pendiente de pago.`);
          this.cargarMesas();
        },
        error: () => this.toastService.error('Error al actualizar mesa.')
      });
    }
    // Si se pasa a ocupada (requiere un pedido ID en la realidad, aquí forzamos o notificamos)
    else if (nuevoEstado === 'OCUPADA') {
      this.toastService.info('Para ocupar una mesa, registre un pedido asignado a ella.');
    }
  }
}

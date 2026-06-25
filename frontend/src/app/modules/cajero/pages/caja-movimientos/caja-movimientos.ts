import { Component, OnInit } from '@angular/core';
import { CajaService } from '../../../../shared/services/caja.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
  selector: 'app-caja-movimientos',
  templateUrl: './caja-movimientos.html',
  styleUrls: ['../../../../../scss/_caja-movimientos.scss'],
  standalone: false
})
export class CajaMovimientosComponent implements OnInit {
  movimientos: any[] = [];
  isLoading: boolean = false;

  constructor(
    private cajaService: CajaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarMovimientos();
  }

  cargarMovimientos(): void {
    this.isLoading = true;
    this.cajaService.obtenerMovimientos().subscribe({
      next: (data) => {
        // Ordenamos los movimientos de más reciente a más antiguo si es necesario
        this.movimientos = data.sort((a, b) => {
          return new Date(b.fecha).getTime() - new Date(a.fecha).getTime();
        });
        this.isLoading = false;
      },
      error: (err) => {
        this.toastService.error('Error al cargar los movimientos');
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  getMontoClase(tipo: string): string {
    return tipo === 'INGRESO' ? 'text-success' : 'text-danger';
  }
}

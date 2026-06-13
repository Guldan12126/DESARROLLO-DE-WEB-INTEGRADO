import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { MesaService } from '../../../../../shared/services/mesa.service';
import { ToastService } from '../../../../../shared/services/toast.service';
import { interval, Subscription } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-mesas-mapa',
  standalone: false,
  templateUrl: './mesas-mapa.html',
  styleUrl: '../../../../../../scss/_mesas-mapa.scss',
})
export class MesasMapa implements OnInit, OnDestroy {
  mesas: any[] = [];
  private pollingSubscription!: Subscription;
  isLoading: boolean = true;

  constructor(
    @Inject(MesaService) private mesaService: MesaService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    // Configuramos el polling: consulta cada 5 segundos para ver cambios de estado
    this.pollingSubscription = interval(5000)
      .pipe(
        startWith(0),
        switchMap(() => this.mesaService.listarTodas())
      )
      .subscribe({
        next: (data) => {
          this.mesas = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error al actualizar mesas:', err);
          this.isLoading = false;
        }
      });
  }

  ngOnDestroy(): void {
    if (this.pollingSubscription) this.pollingSubscription.unsubscribe();
  }

  verDetalleMesa(mesa: any): void {
    this.toastService.info(`Mesa ${mesa.numero}: ${mesa.estado}`);
  }
}

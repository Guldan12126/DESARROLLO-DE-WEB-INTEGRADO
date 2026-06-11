import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CajeroComponent } from './cajero.component';
import { DashboardCajeroComponent } from './pages/dashboard-cajero/dashboard-cajero.component';

const routes: Routes = [
  {
    path: '',
    component: CajeroComponent,
    children: [
      { path: 'dashboard', component: DashboardCajeroComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CajeroRoutingModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MozoComponent } from './mozo.component';
import { DashboardMozoComponent } from './pages/dashboard-mozo/dashboard-mozo.component';

const routes: Routes = [
  {
    path: '',
    component: MozoComponent,
    children: [
      { path: 'dashboard', component: DashboardMozoComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MozoRoutingModule { }

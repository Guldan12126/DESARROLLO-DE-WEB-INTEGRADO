import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardMozoComponent } from './pages/dashboard-mozo/dashboard-mozo.component';


const routes: Routes = [
  { path: '', component: DashboardMozoComponent },
  { path: 'dashboard', component: DashboardMozoComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MozoRoutingModule { }

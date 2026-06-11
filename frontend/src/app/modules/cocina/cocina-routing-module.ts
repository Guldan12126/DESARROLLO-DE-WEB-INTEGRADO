import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CocinaComponent } from './cocina.component';
import { DashboardCocinaComponent } from './pages/dashboard-cocina/dashboard-cocina.component';

const routes: Routes = [
  {
    path: '',
    component: CocinaComponent,
    children: [
      { path: 'dashboard', component: DashboardCocinaComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CocinaRoutingModule { }

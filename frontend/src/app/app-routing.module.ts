// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './modules/auth/pages/login/login.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { 
    path: 'admin', 
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule)
  },
  {
    path: 'mozo',
    loadChildren: () => import('./modules/mozo/mozo-module').then(m => m.MozoModule)
  },
  {
    path: 'cocina',
    loadChildren: () => import('./modules/cocina/cocina-module').then(m => m.CocinaModule)
  },
  {
    path: 'cajero',
    loadChildren: () => import('./modules/cajero/cajero-module').then(m => m.CajeroModule)
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

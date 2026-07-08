// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard, guestGuard, roleGuard } from './core/guards/auth.guard';

const routes: Routes = [
  {
    path: 'login',
    canMatch: [guestGuard],
    loadChildren: () => import('./modules/auth/auth.module').then((m) => m.AuthModule),
  },
  {
    path: 'admin',
    canMatch: [authGuard, roleGuard],
    loadChildren: () => import('./modules/admin/admin.module').then((m) => m.AdminModule),
  },
  {
    path: 'mozo',
    canMatch: [authGuard, roleGuard],
    loadChildren: () => import('./modules/mozo/mozo-module').then((m) => m.MozoModule),
  },
  {
    path: 'cocina',
    canMatch: [authGuard, roleGuard],
    loadChildren: () => import('./modules/cocina/cocina-module').then((m) => m.CocinaModule),
  },
  {
    path: 'cajero',
    canMatch: [authGuard, roleGuard],
    loadChildren: () => import('./modules/cajero/cajero-module').then((m) => m.CajeroModule),
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

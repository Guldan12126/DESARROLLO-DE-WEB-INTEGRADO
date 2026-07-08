import { inject } from '@angular/core';
import { CanMatchFn, Router, UrlSegment } from '@angular/router';

import { AuthService } from '../services/auth.service';

function getRequestedRole(segments: UrlSegment[]): string {
  return segments[0]?.path?.toUpperCase() ?? '';
}

export const authGuard: CanMatchFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  return router.createUrlTree(['/login']);
};

export const guestGuard: CanMatchFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    return true;
  }

  return router.parseUrl(authService.getHomeRouteByRole());
};

export const roleGuard: CanMatchFn = (_route, segments) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const expectedRole = getRequestedRole(segments);

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  if (!expectedRole || authService.hasRole(expectedRole)) {
    return true;
  }

  return router.parseUrl(authService.getHomeRouteByRole());
};

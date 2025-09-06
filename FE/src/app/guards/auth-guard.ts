import { inject, Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserStore } from '../store/user.store';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  private userStore = inject(UserStore);
  private router = inject(Router);
  constructor() {}

  canActivate(): boolean {
    if (this.userStore.isLoggedIn) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}

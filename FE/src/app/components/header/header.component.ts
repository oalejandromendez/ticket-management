import { Component, inject } from '@angular/core';
import { UserStore } from '../../store/user.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  template: `
    <header class="w-full bg-[#2268a2] text-white shadow-md">
      <div class="max-w-8xl mx-auto px-4 py-3 flex items-center justify-between">
        <h1 class="text-2xl font-bold">Gestión de Tickets</h1>
        @if (_userStore.isLoggedIn) {
          <div>
            <button
              (click)="logout()"
              class="bg-white text-[#2268a2] px-4 py-2 rounded hover:bg-gray-200"
            >
              Cerrar sesión
            </button>
          </div>
        }
      </div>
    </header>
  `,
})
export class HeaderComponent {
  public _userStore = inject(UserStore);
  private router = inject(Router);
  constructor() {}

  logout() {
    this._userStore.clearUser();
    this.router.navigate(['/login']);
  }
}

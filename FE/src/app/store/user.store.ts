import { computed, Injectable, signal } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserStore {
  private _user = signal<User | null>(null);

  user = computed(() => this._user());

  setUser(user: User) {
    this._user.set(user);
  }

  clearUser() {
    this._user.set(null);
  }

  get isLoggedIn(): boolean {
    const token = this._user()?.token;
    return token !== undefined && token !== null && token !== '';
  }
}

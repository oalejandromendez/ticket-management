import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { Login } from '../models/login.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  url = environment.apiUrl;

  constructor() {}

  login(user: Login): Observable<User> {
    return this.http.post<User>(`${this.url}/auth/login`, user);
  }
}

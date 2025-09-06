import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { UserStore } from '../../store/user.store';

@Component({
  selector: 'app-auth.component',
  imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss',
})
export class AuthComponent {
  private _authService = inject(AuthService);
  private _userStore = inject(UserStore);
  private router = inject(Router);

  form: FormGroup;

  constructor() {
    this.form = this.loadForm();
  }

  loadForm() {
    return new FormGroup({
      username: new FormControl('', [Validators.required, Validators.maxLength(16)]),
      password: new FormControl('', [Validators.required, Validators.maxLength(16)]),
    });
  }

  onSubmit() {
    if (!this.form.valid) {
      return;
    }

    Swal.fire({
      allowOutsideClick: false,
      icon: 'info',
      text: 'Iniciando Sesión',
    });

    Swal.showLoading();

    this._authService.login(this.form.value).subscribe({
      next: (user) => {
        Swal.close();
        this._userStore.setUser(user);
        this.router.navigate(['/tickets']);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text:
            err.status === 403
              ? 'Las credenciales son incorrectas.'
              : err.error.msg || 'Error al iniciar sesión',
        });
      },
    });
  }
}

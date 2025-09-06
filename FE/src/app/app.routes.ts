import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { TicketComponent } from './components/ticket/ticket.component';
import { AuthGuard } from './guards/auth-guard';

export const routes: Routes = [
  {
    path: 'tickets',
    component: TicketComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'login',
    component: AuthComponent,
  },
  {
    path: '**',
    redirectTo: 'login',
    pathMatch: 'full',
  },
];

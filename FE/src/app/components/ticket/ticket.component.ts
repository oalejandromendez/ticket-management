import { Component, inject, OnInit, signal } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TicketStore } from '../../store/ticket.store';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';
import { TicketFilterComponent } from '../ticket-filter/ticket-filter.component';
import { TicketService } from '../../services/ticket.service';
import { ToastrService } from 'ngx-toastr';
import { TicketPage } from '../../models/ticket-page.model';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { TicketCreate } from '../../models/ticket-create.model';
import { TicketFormComponent } from '../ticket-form/ticket-form.component';
import { TicketResponse } from '../../models/ticket-response.model';
import { UserStore } from '../../store/user.store';
import Swal from 'sweetalert2';
import { TicketDetailComponent } from '../ticket-detail/ticket-detail.component';
import { TicketFilter } from '../../models/ticket-filter.model';

@Component({
  selector: 'app-ticket.component',
  standalone: true,
  imports: [
    MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    HeaderComponent,
    TicketFilterComponent,
    MatPaginatorModule,
    CommonModule,
    TicketDetailComponent,
  ],
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.scss',
})
export class TicketComponent implements OnInit {
  private readonly _ticketStore = inject(TicketStore);
  private _ticketService = inject(TicketService);
  private _toastrService = inject(ToastrService);
  private dialog = inject(MatDialog);
  public readonly _userStore = inject(UserStore);
  displayedColumns: string[] = ['title', 'description', 'priority', 'status', 'tags', 'actions'];

  get tickets() {
    return this._ticketStore.tickets;
  }

  showFilters = false;
  filters: { status?: string; priority?: string; q?: string } = {};

  totalElements = 0;
  pageSize = 10;
  pageNumber = 0;
  loading = signal(false);

  skeletons = [1, 2];

  selectedTicket: TicketResponse | null = null;
  detailOpen = false;

  constructor() {}

  ngOnInit(): void {
    this.loadTickets(this.pageNumber, this.pageSize);
  }

  /**
   * Metodo encargado de consultar la lista de tickets
   */
  private loadTickets(page: number, size: number) {
    const params: TicketFilter = {};

    if (this.filters.status) {
      params.status = this.filters.status;
    }

    if (this.filters.priority) {
      params.priority = this.filters.priority;
    }

    if (this.filters.q) {
      params.q = this.filters.q;
    }

    params.page = page;
    params.limit = size;

    this.loading.set(true);
    this._ticketService.get(params).subscribe({
      next: (response: TicketPage) => {
        this._ticketStore.setTickets(response.content);
        this.totalElements = response.totalElements;
        this.pageNumber = response.number;
        this.pageSize = response.size;
        this.loading.set(false);
      },
      error: () => {
        this._toastrService.error('Error consultando los tickets');
        this.loading.set(false);
      },
    });
  }

  onSearch(id: number) {
    this.loading.set(true);
    this._ticketService.getById(id).subscribe({
      next: (ticket: TicketResponse) => {
        this.selectedTicket = ticket;
        this.detailOpen = true;
        this.loading.set(false);
      },
      error: (err) => {
        this._toastrService.error(err);
        this.loading.set(false);
      },
    });
  }

  onCreate() {
    const dialogRef = this.dialog.open(TicketFormComponent, {
      width: '400px',
      data: {},
    });

    dialogRef.afterClosed().subscribe((result: TicketCreate | undefined) => {
      if (result) {
        this.loading.set(true);
        this._ticketService.create(result).subscribe({
          next: () => {
            this.resetTable();
            this.loading.set(false);
            this._toastrService.success('Ticket creado exitosamente');
          },
          error: (err) => {
            this._toastrService.error(err);
            this.loading.set(false);
          },
        });
      }
    });
  }

  onUpdate(ticket: TicketResponse) {
    const dialogRef = this.dialog.open(TicketFormComponent, {
      width: '400px',
      data: { ticket },
    });

    dialogRef.afterClosed().subscribe((result: TicketCreate | undefined) => {
      if (result) {
        this.loading.set(true);
        this._ticketService.update(ticket.id, result).subscribe({
          next: () => {
            this.resetTable();
            this.loading.set(false);
            this._toastrService.success('Ticket modificado exitosamente');
          },
          error: (err) => {
            this._toastrService.error(err);
            this.loading.set(false);
          },
        });
      }
    });
  }

  onDelete(id: number) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'No podrás revertir esta acción',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this._ticketService.delete(id).subscribe({
          next: () => {
            Swal.fire('Eliminado', 'El ticket fue eliminado correctamente.', 'success');
            this.resetTable();
          },
          error: () => {
            Swal.fire('Error', 'Ocurrió un problema al eliminar el ticket.', 'error');
          },
        });
      }
    });
  }

  resetTable() {
    this.pageSize = 10;
    this.pageNumber = 0;
    this.loadTickets(this.pageNumber, this.pageSize);
  }

  onFiltersChanged(filters: { status?: string; priority?: string; q?: string }) {
    this.filters = filters;
    this.resetTable();
  }

  onPageChange(event: PageEvent) {
    this.loadTickets(event.pageIndex, event.pageSize);
  }
}

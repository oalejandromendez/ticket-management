import { Injectable, signal } from '@angular/core';
import { TicketResponse } from '../models/ticket-response.model';

@Injectable({
  providedIn: 'root',
})
export class TicketStore {
  tickets = signal<TicketResponse[]>([]);

  constructor() {}

  setTickets(data: TicketResponse[]) {
    this.tickets.set(data);
  }
}

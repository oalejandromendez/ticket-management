import { TicketResponse } from './ticket-response.model';

export interface TicketPage {
  content: TicketResponse[];
  totalElements: number;
  number: number;
  size: number;
}

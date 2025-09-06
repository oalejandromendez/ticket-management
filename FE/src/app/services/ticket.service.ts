import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TicketResponse } from '../models/ticket-response.model';
import { TicketPage } from '../models/ticket-page.model';
import { TicketCreate } from '../models/ticket-create.model';
import { TicketFilter } from '../models/ticket-filter.model';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/tickets`;

  constructor() {}

  /**
   * Método encargado de consultar la lista de tickets a partir de ciertos parametros
   * @returns La lista de tickets
   */
  get(params: TicketFilter): Observable<TicketPage> {
    let httpParams = new HttpParams();

    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        httpParams = httpParams.set(key, value.toString());
      }
    });

    return this.http.get<TicketPage>(this.apiUrl, { params: httpParams });
  }

  getById(id: number): Observable<TicketResponse> {
    return this.http.get<TicketResponse>(`${this.apiUrl}/${id}`);
  }

  create(ticket: TicketCreate): Observable<TicketCreate> {
    return this.http.post<TicketCreate>(this.apiUrl, ticket);
  }

  update(id: number, ticket: TicketCreate): Observable<TicketCreate> {
    return this.http.patch<TicketCreate>(`${this.apiUrl}/${id}`, ticket);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

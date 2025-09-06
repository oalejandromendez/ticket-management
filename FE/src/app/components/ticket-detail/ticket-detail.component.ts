import { Component, Input, Output, EventEmitter } from '@angular/core';
import { TicketResponse } from '../../models/ticket-response.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ticket-detail',
  imports: [CommonModule],
  templateUrl: './ticket-detail.component.html',
  styleUrl: './ticket-detail.component.scss',
})
export class TicketDetailComponent {
  @Input() ticket?: TicketResponse | null;
  @Input() open = false;
  @Output() closeModal = new EventEmitter<void>();

  onClose() {
    this.closeModal.emit();
  }
}

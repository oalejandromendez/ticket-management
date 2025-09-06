import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-ticket-filter',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
  ],
  templateUrl: './ticket-filter.component.html',
  styleUrl: './ticket-filter.component.scss',
})
export class TicketFilterComponent {
  private fb = inject(FormBuilder);
  @Output() filtersChanged = new EventEmitter<{ status?: string; priority?: string; q?: string }>();

  form: FormGroup;

  statusOptions = ['OPEN', 'IN_PROGRESS', 'CLOSED'];
  priorityOptions = ['LOW', 'MEDIUM', 'HIGH'];

  constructor() {
    this.form = this.fb.group({
      status: [''],
      priority: [''],
      q: [''],
    });
  }

  applyFilters() {
    this.filtersChanged.emit(this.form.value);
  }

  clearFilters() {
    this.form.reset();
    this.filtersChanged.emit({});
  }
}

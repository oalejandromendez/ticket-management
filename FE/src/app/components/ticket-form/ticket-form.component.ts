import { Component, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { TicketResponse } from '../../models/ticket-response.model';
import { MatChipsModule, MatChipInputEvent } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

@Component({
  selector: 'app-ticket-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDialogModule,
    MatChipsModule,
    MatIconModule,
  ],
  templateUrl: './ticket-form.component.html',
  styleUrl: './ticket-form.component.scss',
})
export class TicketFormComponent {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<TicketFormComponent>);
  public data = inject(MAT_DIALOG_DATA) as { ticket?: TicketResponse };
  form: FormGroup;

  readonly separatorKeysCodes = [ENTER, COMMA] as const;

  priorities = [
    { label: 'Baja', value: 'LOW' },
    { label: 'Media', value: 'MEDIUM' },
    { label: 'Alta', value: 'HIGH' },
  ];

  statuses = [
    { label: 'Abierto', value: 'OPEN' },
    { label: 'En progreso', value: 'IN_PROGRESS' },
    { label: 'Cerrado', value: 'CLOSED' },
  ];

  constructor() {
    this.form = this.loadForm(this.data);
  }

  get tags(): FormArray {
    return this.form.get('tags') as FormArray;
  }

  loadForm(data: { ticket?: TicketResponse }) {
    return this.fb.group({
      title: [data?.ticket?.title ?? '', Validators.required],
      description: [data?.ticket?.description ?? '', Validators.required],
      assignee: [data?.ticket?.assignee ?? ''],
      tags: this.fb.array(data?.ticket?.tags?.split(',') ?? []),
      priority: [data?.ticket?.priority ?? 'LOW', Validators.required],
      status: [
        { value: data?.ticket?.status ?? 'OPEN', disabled: !data?.ticket },
        Validators.required,
      ],
    });
  }

  addTag(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value) {
      this.tags.push(this.fb.control(value));
    }
    event.chipInput.clear();
  }

  removeTag(tag: string): void {
    const index = this.tags.controls.findIndex((c) => c.value === tag);
    if (index >= 0) {
      this.tags.removeAt(index);
    }
  }

  onSave() {
    if (this.form.valid) {
      const ticket: TicketResponse = {
        ...this.form.value,
        id: this.data.ticket?.id,
        tags: this.tags.value.join(','),
      };
      this.dialogRef.close(ticket);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}

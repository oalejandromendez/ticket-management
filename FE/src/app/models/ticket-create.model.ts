export interface TicketCreate {
  id: number;
  title: string;
  description: string;
  priority: string;
  status: string;
  assignee: string;
  tags: string;
}

import { IDepartment } from 'app/entities/department/department.model';

export interface IResponsiblePerson {
  id?: number;
  code?: string;
  firstName?: string;
  secondName?: string | null;
  lastName?: string;
  position?: string | null;
  room?: string | null;
  department?: IDepartment | null;
}

export class ResponsiblePerson implements IResponsiblePerson {
  constructor(
    public id?: number,
    public code?: string,
    public firstName?: string,
    public secondName?: string | null,
    public lastName?: string,
    public position?: string | null,
    public room?: string | null,
    public department?: IDepartment | null
  ) {}
}

export function getResponsiblePersonIdentifier(responsiblePerson: IResponsiblePerson): number | undefined {
  return responsiblePerson.id;
}

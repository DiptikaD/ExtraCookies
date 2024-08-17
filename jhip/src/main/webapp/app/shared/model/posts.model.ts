import dayjs from 'dayjs';
import { ICustomers } from 'app/shared/model/customers.model';
import { Tags } from 'app/shared/model/enumerations/tags.model';

export interface IPosts {
  id?: number;
  postId?: number;
  price?: number | null;
  title?: string | null;
  location?: string | null;
  availability?: dayjs.Dayjs | null;
  rating?: number | null;
  tag?: keyof typeof Tags | null;
  customers?: ICustomers | null;
}

export const defaultValue: Readonly<IPosts> = {};

import dayjs from 'dayjs';
import { Tags } from 'app/shared/model/enumerations/tags.model';

export interface IPosts {
  id?: number;
  price?: number | null;
  title?: string | null;
  location?: string;
  availability?: dayjs.Dayjs | null;
  tag?: keyof typeof Tags | null;
  image?: string | null;
}

export const defaultValue: Readonly<IPosts> = {};

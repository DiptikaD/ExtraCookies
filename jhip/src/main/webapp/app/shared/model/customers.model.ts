export interface ICustomers {
  id?: number;
  email?: string | null;
  userName?: string;
  password?: string;
  profilePicIUrl?: string | null;
}

export const defaultValue: Readonly<ICustomers> = {};

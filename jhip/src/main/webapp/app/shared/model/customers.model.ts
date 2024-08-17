export interface ICustomers {
  id?: number;
  uid?: number;
  email?: string | null;
  userName?: string;
  password?: string;
  profilePicIUrl?: string | null;
}

export const defaultValue: Readonly<ICustomers> = {};

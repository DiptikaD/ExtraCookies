export interface IUsers {
  id?: number;
  uid?: number;
  email?: string | null;
  userName?: string;
  passWord?: string;
  profilePicIUrl?: string | null;
}

export const defaultValue: Readonly<IUsers> = {};

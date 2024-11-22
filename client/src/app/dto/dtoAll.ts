export interface GetUserDto {
  email: string;
  name: string;
}

export interface LoginResponse {
  token: string;
  expiresIn: number;
}

export interface LoginUserDto {
  email: string;
  password: string;
}

export interface UpdateUserRequestDto {
  name: string;
  email: string;
  oldPassword: string;
  newPassword: string;
}

export interface RegisterUserDto {
  email: string;
  password: string;
  name: string;
}

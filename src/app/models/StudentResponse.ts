import {Level} from "./StudentRequest";

export interface StudentResponse {
  id: number;
  username: string;
  level?: Level;
 
}

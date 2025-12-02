export enum Level {
  BEGINNER = 'BEGINNER',
  INTERMEDIATE = 'INTERMEDIATE',
  ADVANCED = 'ADVANCED',
  EXPERT ='EXPERT'
}


export interface StudentRequest {
  username: string;
  level?: Level;
}

import { UserBasic } from "./user-response";

export type QuestionResponse = {
  id: number;
  sentBy: UserBasic;
  body: string;
  timeCreation: string;
}

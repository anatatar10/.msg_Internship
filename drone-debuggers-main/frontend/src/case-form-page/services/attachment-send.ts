import {Attachment} from "../models/attachment";

export interface AttachmentSend{
  systemCaseId: string,
  attachments: Attachment[]
}

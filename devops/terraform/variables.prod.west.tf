variable "location" {
  description = "region"
  type        = string
  default     = "westus"
}

variable "planname" {
  type = string
  default     = "-Prod-West"
}

variable "resource_group" {
  description = "resource group"
  type        = string
  default     = "ct-p-zweus-receipts-rg"
}

variable "appname" {
  type = string
  default     = "receipts-ms"
}

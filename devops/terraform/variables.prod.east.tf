variable "location" {
  description = "region"
  type        = string
  default     = "eastus"
}

variable "planname" {
  type = string
  default     = "-Prod-East"
}

variable "resource_group" {
  description = "resource group"
  type        = string
  default     = "ct-p-zeaus-receipts-rg"
}

variable "appname" {
  type = string
  default     = "receipts-ms"
}

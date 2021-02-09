variable "location" {
  description = "region"
  type        = string
  default     = "eastus"
}

variable "planname" {
  type = string
  default     = "6da57503-404d-4638"
}

variable "resource_group" {
  description = "resource group"
  type        = string
  default     = "ct-n-zeaus-receipts-rg"
}

variable "appname" {
  type = string
  default     = "receipts-ms-dev"
}

# remote states
  variable "subscription_id" {
    type        = string
    description = "Azure Subscription ID"
  }
  variable "videocore-backend-resource-group" {
    type        = string
    description = "Nome do resource group onde o backend está armazenado"
  }

  variable "videocore-backend-storage-account" {
    type        = string
    description = "Nome da conta de armazenamento onde o backend está armazenado"
  }

  variable "videocore-backend-container" {
    type        = string
    description = "Nome do contêiner onde o backend está armazenado"
  }

  variable "videocore-backend-infra-key" {
    type        = string
    description = "Chave do arquivo tfstate do videocore-infra"
  }

variable "release_name" {
  type        = string
  description = "Nome do release do Helm."
}

variable "repository_url" {
  type        = string
  description = "URL do repositório Helm onde o chart está hospedado."
}

variable "chart_name" {
  type        = string
  description = "Nome do chart Helm a ser instalado."
}

variable "chart_version" {
  type        = string
  description = "Versão do chart Helm."
}

variable "release_namespace" {
  type        = string
  description = "Namespace Kubernetes onde o release será instalado."
}

variable "docker_image_uri" {
  type        = string
  description = "URI da imagem Docker a ser utilizada."
}

variable "docker_image_tag" {
  type        = string
  description = "Tag da imagem Docker a ser utilizada."
}
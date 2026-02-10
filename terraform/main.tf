module "helm" {
  source = "./modules/helm"

  subscription_id                   = var.subscription_id
  videocore-backend-container        = var.videocore-backend-container
  videocore-backend-infra-key        = var.videocore-backend-infra-key
  videocore-backend-resource-group   = var.videocore-backend-resource-group
  videocore-backend-storage-account  = var.videocore-backend-storage-account
  release_name                      = var.release_name
  repository_url                    = local.repository_url
  chart_name                        = var.chart_name
  chart_version                     = var.chart_version
  release_namespace                 = var.release_namespace
  docker_image_uri                  = var.docker_image_uri
  docker_image_tag                  = var.docker_image_tag
}
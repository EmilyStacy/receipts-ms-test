resource "azurerm_app_service_plan" "appserviceplan" {
  name                = "ServicePlan${var.planname}"
  location            = "${var.location}"
  resource_group_name = "${var.resource_group}"
}

resource "azurerm_app_service" "appservice" {
  name                = "${var.appname}"
  location            = "${var.location}"
  resource_group_name = "${var.resource_group}"
  app_service_plan_id = "${azurerm_app_service_plan.appserviceplan.id}"
  enabled             = "true"

  site_config {
    always_on              = "true"
    java_version           = "1.8"
    java_container         = "JAVA"
    java_container_version = "1.8"
    app_command_line 	   = "java -jar /home/site/wwwroot/*.jar"
    default_documents = [
      "HomePage.html",
      "Default.htm",
      "Default.html",
      "Default.asp",
      "index.htm",
      "index.html",
      "iisstart.htm",
      "default.aspx",
      "index.php",
      "hostingstart.html"
    ]
  }
}

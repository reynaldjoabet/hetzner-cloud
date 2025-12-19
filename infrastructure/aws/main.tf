terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "> 4.16"
    }
  }
}

provider "aws" {
  region = "eu-north-1"
}

# Create a Custom VPC
resource "aws_vpc" "vpc_c9" {
  cidr_block = "10.0.0.0/16"
  tags       = { Name = "vpc_c9" }
}
# Create a Subnet inside the VPC
resource "aws_subnet" "sub_c9" {
  vpc_id                  = aws_vpc.vpc_c9.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "eu-north-1b"
  tags                    = { Name = "subnet_c9" }
  map_public_ip_on_launch = true
}
# Create a Internet Gateway for the VPC
resource "aws_internet_gateway" "ig_c9" {
  vpc_id     = aws_vpc.vpc_c9.id
  depends_on = [aws_vpc.vpc_c9]
  tags       = { Name = "gateway_c9" }
}
# Create a Route Table for the VPC
resource "aws_route_table" "rt_c9" {
  vpc_id = aws_vpc.vpc_c9.id
  tags   = { Name = "route_table_c9" }
}
# Associate the route table to the devices inside the Subnet
resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.sub_c9.id
  route_table_id = aws_route_table.rt_c9.id
}
# Create a route to the Internet by pointing to the Internet Gateway
resource "aws_route" "rt_internet" {
  destination_cidr_block = "0.0.0.0/0"
  route_table_id         = aws_route_table.rt_c9.id
  gateway_id             = aws_internet_gateway.ig_c9.id
}

# Create security group to allow traffic for EC2 instances
# Custom VPC's block all incoming/outgoing traffic by default
resource "aws_security_group" "sg_c9" {
  name        = "sg_c9vpc"
  description = "Security group to allow traffic to ec2 instances"
  vpc_id      = aws_vpc.vpc_c9.id
  ingress {
    from_port   = "80"
    to_port     = "80"
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = "22"
    to_port     = "22"
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = "0"
    to_port     = "0"
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = { Name = "security_grp_c9" }
}

# Create instance to act as a web server using UBUNTU AMI
resource "aws_instance" "test_server_ubulnx" {
  ami                    = "ami-0914547665e6a707c"
  instance_type          = "t3.micro"
  vpc_security_group_ids = [aws_security_group.sg_c9.id]
  subnet_id              = aws_subnet.sub_c9.id
  key_name               = "terraform-demo"
  user_data = file("${path.module}/ubuntu_script.sh")
  tags = {
    Name = "Ubuntu_web_server"
  }
}

# Create instance to act as a web server using AMAZON LINUX AMI
resource "aws_instance" "test_server_amzlnx" {
  ami                    = "ami-01dad638e8f31ab9a"
  instance_type          = "t3.micro"
  vpc_security_group_ids = [aws_security_group.sg_c9.id]
  subnet_id              = aws_subnet.sub_c9.id
  key_name               = "terraform-demo"
  user_data = file("${path.module}/amzlnx_script.sh")
  tags = {
    Name = "AmzLnx_web_server"
  }
}

# Output values for identifying the resources
output "ec2info_amzlnx" {
  description = " Amazon Linux Web Server Instance ID"
  value       = aws_instance.test_server_amzlnx.id
}
output "ec2ipaddr_amzlnx" {
  description = "Amazon Linux Web Server public IP address"
  value       = aws_instance.test_server_amzlnx.public_ip
}
output "ec2info_ubulnx" {
  description = " Ubuntu Web Server Instance ID"
  value       = aws_instance.test_server_ubulnx.id
}
output "ec2ipaddr_ubulnx" {
  description = "Ubuntu Web Server public IP address"
  value       = aws_instance.test_server_ubulnx.public_ip
}
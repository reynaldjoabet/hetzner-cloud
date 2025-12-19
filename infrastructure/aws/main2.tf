provider "aws" {
  region = "us-east-1"
}
resource "aws_vpc" "my_vpc" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "MyVPC"
  }
}

resource "aws_internet_gateway" "my_igw" {
  vpc_id = aws_vpc.my_vpc.id
  tags = {
    Name = "MyIGW"
  }
}
resource "aws_route_table" "my_rt" {
  vpc_id = aws_vpc.my_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.my_igw.id
  }
  tags = {
    Name = "MyRouteTable"
  }
}
resource "aws_route_table_association" "my_rta" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.my_rt.id
}
resource "aws_main_route_table_association" "my_main_rta" {
  vpc_id         = aws_vpc.my_vpc.id
  route_table_id = aws_route_table.my_rt.id
}

resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.my_vpc.id
  cidr_block              = "10.0.0.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true
  tags = {
    Name = "MySubnet"
  }
}
resource "aws_instance" "my_instance" {
  ami           = "ami-0fa3fe0fa7920f68e" # Amazon Linux 2023 AMI 2023
  instance_type = "t2.micro"
  subnet_id     = aws_subnet.public_subnet.id
  associate_public_ip_address = true
  key_name      = aws_key_pair.deployer_key.key_name
  vpc_security_group_ids = [aws_security_group.instance_sg.id]
  tags = {
    Name = "MyInstance"
  }
}
resource "tls_private_key" "deployer_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "deployer_key" {
  key_name   = "deployer_key"
  public_key = tls_private_key.deployer_key.public_key_openssh
}

output "private_key" {
  value     = tls_private_key.deployer_key.private_key_pem
  sensitive = true
}

output "key_pair_name" {
  value = aws_key_pair.deployer_key.key_name
}
resource "aws_security_group" "instance_sg" {
  name        = "instance_sg"
  description = "Allow SSH and HTTP"
  vpc_id      = aws_vpc.my_vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "InstanceSecurityGroup"
  }
}
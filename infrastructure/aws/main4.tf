terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

#vpc
resource "aws_vpc" "main_vpc" {
  cidr_block = "10.0.0.0/16"
}
#subnet
resource "aws_subnet" "public_subnet_a" {
  vpc_id     = aws_vpc.main_vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true
  tags = {
    Name = "Public-Subnet_a"
  }
}

resource "aws_subnet" "public_subnet_b" {
  vpc_id                  = aws_vpc.main_vpc.id
  cidr_block              = "10.0.4.0/24"
  availability_zone       = "us-east-1b"
  map_public_ip_on_launch = true

  tags = {
    Name = "public_subnet_b"
  }
}


resource "aws_subnet" "front_end_private_subnet" {
  vpc_id     = aws_vpc.main_vpc.id
  cidr_block = "10.0.2.0/24"
  availability_zone = "us-east-1a"
  tags = {
    Name = "Frontend-Private-Subnet"
  }
}

resource "aws_subnet" "back_end_private_subnet_a" {
  vpc_id     = aws_vpc.main_vpc.id
  availability_zone = "us-east-1a"
  cidr_block = "10.0.6.0/24"
  tags = {
    Name = "Backend-Private-Subnet_a"
  }
}


resource "aws_subnet" "back_end_private_subnet_b" {
  vpc_id     = aws_vpc.main_vpc.id
  availability_zone = "us-east-1b"
  cidr_block = "10.0.5.0/24"
  tags = {
    Name = "Backend-Private-Subnet_b"
  }
}

#internet_gateway
resource "aws_internet_gateway" "internet_gw" {
  vpc_id = aws_vpc.main_vpc.id
  tags = {
    Name = "Internet-Gateway"
  }
}

#NAT Gateway
resource "aws_eip" "nat_eip" {
  #vpc = true

    tags = {
        Name = "NAT-EIP"
    }


}

resource "aws_nat_gateway" "nat_gw" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.public_subnet_a.id

  tags = {
    Name = "NAT-Gateway"
  }
}

#public_route_table
resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.main_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet_gw.id
  }

  tags = {
    Name = "Public-Route-Table"
  }
}

#private_route_table
resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.main_vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gw.id
  }

  tags = {
    Name = "Private-Route-Table"
  }
}

#association
resource "aws_route_table_association" "frontend_private_route_table_assoc" {
  subnet_id      = aws_subnet.front_end_private_subnet.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "backend_private_route_table_assoc_a" {
  subnet_id      = aws_subnet.back_end_private_subnet_a.id
  route_table_id = aws_route_table.private_route_table.id
}
resource "aws_route_table_association" "backend_private_route_table_assoc_b" {
  subnet_id      = aws_subnet.back_end_private_subnet_b.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "public_route_table_assoc_a" {
  subnet_id      = aws_subnet.public_subnet_a.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "public_route_table_assoc_b" {
  subnet_id      = aws_subnet.public_subnet_b.id
  route_table_id = aws_route_table.public_route_table.id
}

#aws_security_group

resource "aws_security_group" "allow-ssh" {
  name = "allow-ssh"
  description = "Allow SSH inbound traffic"
  vpc_id = aws_vpc.main_vpc.id
  ingress {
    description = "SSH to EC2"
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    description = "All outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
  Name = "allow-ssh"
  }
}

# Security Group for Web Servers
resource "aws_security_group" "web-server-sg" {
  name        = "web-server-sg"
  description = "Allow HTTP and SQL inbound traffic from Bastion Server"
  vpc_id      = aws_vpc.main_vpc.id

  ingress {
    description = "Allow SSH from Bastion"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    security_groups = [aws_security_group.allow-ssh.id]
  }

  ingress {
    description = "Allow HTTP from anywhere"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    security_groups = [aws_security_group.allow-http.id]
  }

  ingress {
    description = "Allow SQL from Bastion Server"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = [aws_security_group.allow-ssh.id,
      aws_security_group.allow-sql.id,
      aws_security_group.allow-sql-from-ec2.id]  # Allow SQL traffic from Bastion Server's security group
  }

  ingress {
    description = "Allow Web traffic on port 8080"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    security_groups = [aws_security_group.allow-8080.id]
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "web-server-sg"
  }
}

resource "aws_security_group" "allow-http" {
  name        = "allow-http"
  description = "Allow HTTP inbound traffic"
  vpc_id      = aws_vpc.main_vpc.id

  ingress {
    description = "HTTP to Load Balancer"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "All outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow-http"
  }
}

resource "aws_security_group" "allow-all-outbound" {
  name = "allow-all-outbound"
  description = "Allow all outbound traffic"
  vpc_id = aws_vpc.main_vpc.id
  egress {
    description = "All outbound"
    from_port = 0
    to_port = 0
    protocol = "-1"
  cidr_blocks = ["0.0.0.0/0"]
}
  tags = {
    Name = "allow-all-outbound"
  }
}

resource "aws_security_group" "allow-sql" {
  name = "allow-sql"
  description = "Allow SQL inbound traffic"
  vpc_id = aws_vpc.main_vpc.id
  ingress {
    description = "SQL to EC2"
    from_port = 3306
    to_port = 3306
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    security_groups = [aws_security_group.allow-ssh.id]
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "allow-sql"
  }
}

resource "aws_security_group" "allow-sql-from-ec2" {
  name        = "allow-sql-from-ec2"
  description = "Allow SQL inbound traffic from EC2 instances"
  vpc_id      = aws_vpc.main_vpc.id

  ingress {
    description = "Allow SQL traffic from EC2 instances"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = [
      aws_security_group.allow-ssh.id,
      aws_security_group.allow-8080.id,
    ]
  }

  tags = {
    Name = "allow-sql-from-ec2"
  }
}
resource "aws_security_group" "allow-8080" {
  name = "allow-8080"
  description = "Allow Web inbound traffic"
  vpc_id = aws_vpc.main_vpc.id
  ingress {
    description = "Web to EC2"
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "allow-8080"
  }
}
resource "aws_security_group" "allow-ping" {
  name = "allow-ping"
  description = "Allow ping"
  vpc_id = aws_vpc.main_vpc.id
  ingress {
    description = "Ping"
    from_port = -1
    to_port = -1
    protocol = "icmp"
  cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "allow-ping"
  }
}

#Launch Web Servers
resource "aws_instance" "bastion-server" {
  depends_on                  = [aws_key_pair.ec2-key-pair]
  subnet_id                   = aws_subnet.public_subnet_a.id
  ami                         = "ami-014d544cfef21b42d"
  instance_type               = "t2.micro"
  associate_public_ip_address = true
  key_name                    = aws_key_pair.ec2-key-pair.key_name
  vpc_security_group_ids      = [aws_security_group.allow-ssh.id, aws_security_group.allow-all-outbound.id]

  tags = {
    Name    = "Bastion Server"
  }
}

resource "aws_instance" "web-server" {
  count                       = 2
  depends_on                  = [aws_key_pair.ec2-key-pair, aws_db_instance.mysql_db]
  subnet_id                   = aws_subnet.front_end_private_subnet.id
  ami                         = "ami-014d544cfef21b42d"
  instance_type               = "t2.micro"
  associate_public_ip_address = false
  key_name                    = aws_key_pair.ec2-key-pair.key_name
  vpc_security_group_ids      = [
    aws_security_group.web-server-sg.id
  ]
  user_data = <<-EOF
                  #!/bin/bash
                sudo su
                yum update -y
                yum install httpd -y
                service httpd start
                chkconfig httpd on
# Create a simple HTML page to test load balancing
                echo "<html><body><h1>Hello from Web Server ${count.index + 1}</h1></body></html>" > /var/www/html/index.html
              EOF
  tags = {
    Name ="instance-${count.index}"
  }
}
  #key pair
resource "aws_key_pair" "ec2-key-pair" {
  key_name   = "key_pair_lana"
  public_key = file("C:/Users/User/my-key.pub")
}

# Load Balancer
resource "aws_lb" "app_lb" {
  name               = "app-lb"
  depends_on         = [aws_instance.web-server]
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.allow-http.id, aws_security_group.allow-all-outbound.id]
  subnets            = [
    aws_subnet.public_subnet_a.id,
    aws_subnet.public_subnet_b.id
  ]

  tags = {
    Name = "App-Load-Balancer"
  }
}

resource "aws_lb_target_group" "tg1" {
  name     = "tg"
  port     = 80
  protocol = "HTTP"
  target_type = "instance"
  vpc_id   = aws_vpc.main_vpc.id

  health_check {
    enabled             = true
    interval            = 10
    path                = "/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 6
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }

  tags = {
    Name = "App-Target-Group"
  }
}

resource "aws_lb_listener" "listener" {
  load_balancer_arn = aws_lb.app_lb.arn
  port              = 80
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tg1.arn
  }
}

#Register EC2 Instances with Target Group

resource "aws_lb_target_group_attachment" "ec2-web-attachment" {
  count            = length(aws_instance.web-server)
  target_group_arn = aws_lb_target_group.tg1.arn
  target_id        = aws_instance.web-server[count.index].id
}

#Create an RDS MySQL Database
resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "db_subnet_group"
  subnet_ids = [
    aws_subnet.back_end_private_subnet_a.id,
    aws_subnet.back_end_private_subnet_b.id,
  ]

  tags = {
    Name = "DB-Subnet-Group"
  }
}

resource "aws_db_instance" "mysql_db" {
  allocated_storage    = 20
  storage_type             = "gp2"
  engine               = "mysql"
  engine_version       = "8.0.33"
  instance_class       = "db.t3.micro"
  username             = "admin"
  password             = "password"
  parameter_group_name = "default.mysql8.0"
  skip_final_snapshot  = true
  vpc_security_group_ids = [aws_security_group.allow-sql.id, aws_security_group.allow-all-outbound.id,aws_security_group.allow-sql-from-ec2.id]
  db_subnet_group_name = aws_db_subnet_group.db_subnet_group.id
  db_name = "lana_db"

  tags = {
    Name = "MySQL-DB"
  }
}
#console output
output "load_balancer_url" {
  description = "The URL of the Load Balancer"
  value       = "http://${aws_lb.app_lb.dns_name}"
}

output "rds_endpoint" {
  description = "The endpoint of the RDS instance"
  value       = aws_db_instance.mysql_db.endpoint
}

output "bastion-public-ip" {
  description="bastion public ip"
  value = aws_instance.bastion-server.public_ip
}

output "web-apps-private-ips" {
  description = "The private IP for web app instances"
  value = aws_instance.web-server.*.private_ip
}



#Select Region
provider "aws" {
    region = "ap-south-1"
}

#Creating VPC
resource "aws_vpc" "cloudhats" {
    cidr_block = "10.0.0.0/16"

    tags = {
        Name = "cloudhats"
    }
}

#Creating Public Subnet
resource "aws_subnet" "public" {
    vpc_id = aws_vpc.cloudhats.id 
    cidr_block = "10.0.0.0/24"

    tags = {
        Name = "public subnet"
    }
}

#Creating Private Subnet
resource "aws_subnet" "private" {
    vpc_id     = aws_vpc.cloudhats.id
    cidr_block = "10.0.1.0/24"

    tags = {
        Name = "private subnet"
    }
}

#Creating Intenrnet Gatway To Access Internet
resource "aws_internet_gateway" "igw" {
    vpc_id = aws_vpc.cloudhats.id
    
    tags = {
        Name = "igw"
    }
}

#Allocate Elastic Ip For Nat GAtway
resource "aws_eip" "nat_eip" {
    domain = "vpc"
    depends_on = [aws_internet_gateway.igw]

    tags = {
        Name = "nat_eip"
    }
}

#Creating Nat Gatway
resource "aws_nat_gateway" "nat" {
    allocation_id = aws_eip.nat_eip.id
    subnet_id     = aws_subnet.public.id

    tags = {
        Name = "nat"
    }

}

#Creating Route internet Gatway to Public Subnet
resource "aws_route_table" "public" {
    vpc_id = aws_vpc.cloudhats.id

    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.igw.id
    }

    tags = {
        Name = "public route"
    }
}

resource "aws_route_table_association" "public" {
    subnet_id      = aws_subnet.public.id
    route_table_id = aws_route_table.public.id
}

#Creating Route Public Subnet to Private Subnet For Getting Internet Access
resource "aws_route_table" "private" {
    vpc_id =aws_vpc.cloudhats.id

    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_nat_gateway.nat.id
    }

    tags = {
        Name = "private route"
    }
}

resource "aws_route_table_association" "private" {
    subnet_id      = aws_subnet.private.id
    route_table_id = aws_route_table.private.id
}
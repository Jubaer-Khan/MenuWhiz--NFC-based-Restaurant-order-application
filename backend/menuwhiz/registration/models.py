from django.db import models

# Create your models here.

class Restaurant(models.Model):
    restaurant_id = models.AutoField(primary_key = True)
    name = models.CharField(max_length = 200)
    email = models.EmailField(max_length = 254, unique= True)
    address = models.TextField()
    phone = models.CharField(max_length = 200)
    password = models.CharField(max_length=100)
    NFC_Tag = models.CharField(max_length=200, null = True, blank = True) 
    

class Customer(models.Model):
    customer_id = models.AutoField(primary_key = True)
    name = models.CharField(max_length = 200)
    email = models.EmailField(max_length = 254, unique= True)
    phone = models.CharField(max_length = 200)
    password = models.CharField(max_length=100, null=True, blank = True)
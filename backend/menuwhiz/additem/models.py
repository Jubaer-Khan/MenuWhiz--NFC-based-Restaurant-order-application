from django.db import models

# Create your models here.

class Item(models.Model):
    restaurant = models.ForeignKey('registration.Restaurant', on_delete=models.CASCADE)
    item_id = models.AutoField(primary_key = True)
    name = models.CharField(max_length = 200)
    quantity = models.IntegerField()
    price = models.DecimalField(max_digits=5, decimal_places=2)

class TempItem(models.Model):
    restaurant = models.ForeignKey('registration.Restaurant', on_delete=models.CASCADE)
    customer = models.ForeignKey('registration.Customer', on_delete=models.CASCADE)
    item = models.ForeignKey('additem.Item', on_delete=models.CASCADE)
    name = models.CharField(max_length = 200)
    quantity = models.IntegerField()
    price = models.DecimalField(max_digits=5, decimal_places=2)
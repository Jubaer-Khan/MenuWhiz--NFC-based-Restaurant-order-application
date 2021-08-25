from django.db import models

# Create your models here.



class Order(models.Model):
    PLACED = 'P'
    COMPLETED = 'C'

    STATUS = [
        (PLACED, 'placed'),
        (COMPLETED, 'completed'),
        
    ]
    
    order_id = models.AutoField(primary_key = True)
    status = models.CharField(max_length=2,
        choices=STATUS,
        default=PLACED,)

class Order_content(models.Model):
    restaurant = models.ForeignKey('registration.Restaurant', on_delete=models.CASCADE)
    order = models.ForeignKey('Order', on_delete=models.CASCADE)
    item = models.ForeignKey('additem.Item', on_delete=models.CASCADE)
    quantity = models.IntegerField()
    total_price = models.DecimalField(max_digits=5, decimal_places=2)
    
class Placed_order(models.Model):
    customer = models.ForeignKey('registration.Customer', on_delete=models.CASCADE)
    restaurant = models.ForeignKey('registration.Restaurant', on_delete=models.CASCADE)
    order = models.ForeignKey('Order', on_delete=models.CASCADE)
    date = models.DateField(auto_now=False, auto_now_add=True )


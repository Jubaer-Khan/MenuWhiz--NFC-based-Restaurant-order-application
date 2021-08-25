from django.contrib import admin

# Register your models here.

from .models import Order, Order_content, Placed_order

admin.site.register(Order)
admin.site.register(Order_content)
admin.site.register(Placed_order)

from django.contrib import admin

# Register your models here.

from .models import Restaurant,Customer

admin.site.register(Restaurant)
admin.site.register(Customer)
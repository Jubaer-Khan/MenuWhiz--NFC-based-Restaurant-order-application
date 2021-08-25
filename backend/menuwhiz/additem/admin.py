from django.contrib import admin

# Register your models here.

from .models import Item,TempItem

admin.site.register(Item)
admin.site.register(TempItem)
from rest_framework import serializers
from additem.models import Item, TempItem


class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields='__all__'
        
class TempItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = TempItem
        fields='__all__'

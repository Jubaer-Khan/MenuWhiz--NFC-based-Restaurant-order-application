from rest_framework import serializers
from orders.models import Order, Placed_order, Order_content


class OrderSerializer(serializers.ModelSerializer):
    class Meta:
        model = Order
        fields='__all__'

class PlacedOrderSerializer(serializers.ModelSerializer):
    class Meta:
        model = Placed_order
        fields='__all__'

class OrderContentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Order_content
        fields='__all__'

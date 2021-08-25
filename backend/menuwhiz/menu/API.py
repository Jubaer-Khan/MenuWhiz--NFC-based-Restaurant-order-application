from django.http import  JsonResponse
from rest_framework.parsers import JSONParser
from django.views.decorators.csrf import csrf_exempt
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

from additem.models import Item, TempItem
from registration.models import Restaurant, Customer
from orders.models import Order, Placed_order, Order_content
from registration.serializers import RestaurantSerializer, CustomerSerializer
from additem.serializers import ItemSerializer, TempItemSerializer

@api_view(['POST'])
def PlaceOrder(request):

#API to place and order. Gets the appropriate customer and restaurant object using the ids, then creates an order object and updates the placed_order table too. then each item is added to the order_content one by one. finally the items in the tempItem table are deleted using the restaurant and customer object as they are no longer needed.
    if request.method == 'POST':
    
        c = request.data.get('customer')
        customer= Customer.objects.get(customer_id=c)
        
        r = request.data.get('restaurant')
        restaurant= Restaurant.objects.get(restaurant_id=r)
        
        order = Order(status='P')      
        order.save()
        
        placed_order = Placed_order(customer = customer,restaurant = restaurant, order = order)
        placed_order.save()
        
        tempItems = TempItem.objects.filter(customer=customer, restaurant=restaurant)
        
        totalprice = 0
        for i in range(len(tempItems)):
            item = tempItems[i]
            
            index= item.item.item_id
            originalItem = Item.objects.get(item_id= index, restaurant= restaurant)
            
            price=item.price
            totalprice+=price
            
            orderqty=item.quantity
            
            oldqty = originalItem.quantity
            
            newqty = oldqty-orderqty
            
            if(newqty>=0):
                originalItem.quantity=newqty
                originalItem.save()
                
                
                order_content = Order_content(restaurant = restaurant, order = order, item = originalItem, quantity = orderqty, total_price = price)
                order_content.save()
                
                TempItem.objects.filter(restaurant = restaurant, customer = customer, item = originalItem).delete()
                
                
                
                
            
            else:
                TempItem.objects.filter(restaurant = restaurant, customer = customer, item = originalItem).delete()


        response = {
            'price' : totalprice
            }
        
        return Response(response, status.HTTP_200_OK)
        
            

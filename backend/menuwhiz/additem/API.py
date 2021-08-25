from django.http import  JsonResponse
from rest_framework.parsers import JSONParser
from django.views.decorators.csrf import csrf_exempt
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

from .models import Item, TempItem
from registration.models import Restaurant,Customer
from registration.serializers import RestaurantSerializer,CustomerSerializer
from .serializers import ItemSerializer, TempItemSerializer

@api_view(['POST'])
def RetrieveMenu(request):

#API to retrieve menu items of a restaurant. uses the nfc tag to get the restaurant object, then uses the restaurant object to filter records in item table. returns all the items found
    if request.method == 'POST':
        
        nfc = request.data.get('nfc')
        
        try:
            restaurant = Restaurant.objects.get(NFC_Tag=nfc)
            
            items = Item.objects.filter(restaurant=restaurant)
            serializer = ItemSerializer(items, many=True)
            
            response = {
            #'status': status.HTTP_200_OK,
            'itemlist' : serializer.data
            }
            
            return Response(response, status=status.HTTP_200_OK)
        
        except Restaurant.DoesNotExist:
            
            return Response(status.HTTP_400_BAD_REQUEST)
            
@api_view(['POST'])           
def RetrieveRestaurant(request):

#API to retrieve a restaurant. uses the nfc tag to get the restaurant object, returns restaurant if it exists
    if request.method == 'POST':
        
        nfc = request.data.get('nfc')
        
        try:
            restaurant = Restaurant.objects.get(NFC_Tag=nfc)
            
            serializer = RestaurantSerializer(restaurant)
            
            return Response(serializer.data, status=status.HTTP_200_OK)
        
        except Restaurant.DoesNotExist:
            
            return Response(status.HTTP_400_BAD_REQUEST)
            


@api_view(['POST'])           
def AddtempItem(request):

#API to add an item to cart. serializes the tempItem object and adds it to the tempItem table 
    if request.method == 'POST':
        
        serializer= TempItemSerializer(data=request.data)
        
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        
        return Response(serializer.errors, status = status.HTTP_400_BAD_REQUEST)
        

@api_view(['POST'])           
def DeletetempItem(request):

#API to delete an item from cart. uses the customer_id to find customer object, restaurant_id to find restaurant object and item_id to find item object, then filters the tempItem table with the objects to find the record to delet 
    if request.method == 'POST':
        
        c= request.data.get('customer')
        customer=Customer.objects.get(customer_id=c)
        
        r=request.data.get('restaurant')
        restaurant= Restaurant.objects.get(restaurant_id=r)
        
        i= request.data.get('item')
        item= Item.objects.get(item_id=i)
        
        delitem=TempItem.objects.filter(customer=customer, restaurant=restaurant, item=item).delete()
        
        response={
            'status':'deleted'
        }
        
        return Response(response, status=status.HTTP_200_OK)
        
    return Response(serializer.errors, status = status.HTTP_400_BAD_REQUEST)
        
        


@api_view(['POST'])
def RetrieveTempItem(request):

#API to retrieves items added to cart. takes the restaurant_id to find restaurant object and customer_id to find customer object, then filters the tempItem table to find all the records that match the objects. 
    if request.method == 'POST':
        
        restaurant = request.data.get('restaurant')
        customer = request.data.get('customer')
        
        try:
            tempitems = TempItem.objects.filter(restaurant=restaurant,customer=customer)

            serializer = TempItemSerializer(tempitems, many=True)
            
            response = {
            #'status': status.HTTP_200_OK,
            'itemlist' : serializer.data
            }
            
            return Response(response, status=status.HTTP_200_OK)
        
        except TempItem.DoesNotExist:
            
            return Response(status.HTTP_400_BAD_REQUEST)
           


from django.http import  JsonResponse
from rest_framework.parsers import JSONParser
from django.views.decorators.csrf import csrf_exempt
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

from .models import Restaurant, Customer
from .serializers import RestaurantSerializer, CustomerSerializer

@api_view(['POST'])
def CustomerRegistration(request):

#API to create a customer object and save it in the customer table 
    if request.method == 'POST':
        
        serializer= CustomerSerializer(data=request.data)
        
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        
        return Response(serializer.errors, status = status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
def GoogleCustomerRegistration(request):

#API to create a customer object and save it in the customer table using information received from the Google API. This is different from CustomerRegistration as it first checks whether the google user already exists in the local database and then saves the record if its not.
    if request.method == 'POST':
        
        email=request.data.get('email')
        
        try:
            customer = Customer.objects.get(email=email)
            serializer = CustomerSerializer(customer)
            
            return Response(serializer.data, status=status.HTTP_200_OK)
        
        except Customer.DoesNotExist:
            
            serializer= CustomerSerializer(data=request.data)
            
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data, status=status.HTTP_201_CREATED)
        

@api_view(['POST'])
def CustomerLogin(request):

#API to check if login credentials are correct
 
    if request.method == 'POST':
        email=request.data.get('email')
        password = request.data.get('password')
        
        try:
            customer = Customer.objects.get(email=email)
            
        
        except Customer.DoesNotExist:
            return Response(status.HTTP_400_BAD_REQUEST)
            
        if(password==customer.password):
            serializer= CustomerSerializer(customer)
        
            return Response(serializer.data, status=status.HTTP_200_OK)
        
        else:
            
            return Response(status.HTTP_400_BAD_REQUEST)
        
        
@api_view(['POST'])
def RetrieveCustomer(request):

#API to check if a customer exists in the database
    if request.method == 'POST':
        email=request.data.get('email')
        
        try:
            customer = Customer.objects.get(email=email)
            serializer= CustomerSerializer(customer)
            
        
            return Response(serializer.data, status=status.HTTP_200_OK)
            
        
        except Customer.DoesNotExist:
            response = {
            'status' : 'new user'
            }
            return Response(response, status.HTTP_200_OK)
            
        

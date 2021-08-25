from django.shortcuts import render, redirect
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse
from rest_framework.parsers import JSONParser
from django.views.decorators.csrf import csrf_exempt

from .models import Restaurant 
from .serializers import RestaurantSerializer



# Create your views here.

def registration(request):
    if request.method == 'POST':
        if request.POST.get('name') and request.POST.get('email') and request.POST.get('address') and request.POST.get('phone') and request.POST.get('password'):
            
            saverecord = Restaurant()
            saverecord.name = request.POST.get('name')
            saverecord.email = request.POST.get('email')
            saverecord.address = request.POST.get('address')
            saverecord.phone = request.POST.get('phone')
            saverecord.password = request.POST.get('password')
            
            if Restaurant.objects.filter(email= request.POST.get('email')).exists():
                context = {'msg': 'Email already registered'}
                return render(request, 'signup.html',context)
            else:
                saverecord.save()
                
                obj= Restaurant.objects.get(email= saverecord.email)
                request.session['restaurant_ID'] = obj.restaurant_id   
           
                return HttpResponseRedirect('/nfc/')
    else:
            return render(request, 'signup.html')        
            

def NFCconfig(request):
    
    if request.method == 'POST':
    
        restaurant_id= request.session['restaurant_ID']
        
        obj= Restaurant.objects.get(restaurant_id=restaurant_id)
        
        obj.NFC_Tag= request.POST.get('NfcID')
        obj.save()
        return HttpResponseRedirect('/menu/')
    
    return render(request,'NFC.html')
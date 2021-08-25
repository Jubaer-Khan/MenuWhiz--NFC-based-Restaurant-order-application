from django.shortcuts import render, redirect
from django.views.generic import ListView
from django.http import HttpResponse, HttpResponseRedirect


from registration.models import Restaurant



# Create your views here.

def profile(request):
    
    restaurant_id= request.session['restaurant_ID']
    
    rest= Restaurant.objects.get(restaurant_id=restaurant_id)
    
    context= {
        'restaurant':rest
        }
    
    
    
    return render(request,'profile.html', context)
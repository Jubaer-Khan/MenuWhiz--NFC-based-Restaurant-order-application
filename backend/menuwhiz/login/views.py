from django.shortcuts import render,redirect
from django.http import HttpResponse, HttpResponseRedirect

from registration.models import Restaurant
from additem.models import Item


# Create your views here.

def signin(request):
    if request.method == 'POST':
        record_email= request.POST.get('email')
        record_password= request.POST.get('password')
        if Restaurant.objects.filter(email=record_email,password=record_password).exists():
            obj= Restaurant.objects.get(email= record_email)
            
            request.session['restaurant_ID'] = obj.restaurant_id   
           
            return HttpResponseRedirect('/menu/')
        else:
            context = {'msg': 'Invalid email or password'}
            return render(request, 'signin.html',context)
    else:
        return render(request, 'signin.html') 
    
       


def signout(request):
    
    del request.session['restaurant_ID']
    return HttpResponseRedirect('/signin/')


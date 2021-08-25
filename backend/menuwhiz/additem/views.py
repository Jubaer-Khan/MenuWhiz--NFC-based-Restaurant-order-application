from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect


from .models import Item
from registration.models import Restaurant
# Create your views here.

def additem(request):

    if request.method == 'POST':
        
  
        
        obj= Restaurant.objects.get(restaurant_id=request.session['restaurant_ID'])
        
        
        if request.POST.get('name') and request.POST.get('quantity') and request.POST.get('price') :
            
            saverecord = Item()
            saverecord.restaurant = obj
            saverecord.name = request.POST.get('name')
            saverecord.quantity = request.POST.get('quantity')
            saverecord.price = request.POST.get('price')
            
            saverecord.save()
            
            return HttpResponseRedirect('/menu/')
    
    return render(request,'additem.html')
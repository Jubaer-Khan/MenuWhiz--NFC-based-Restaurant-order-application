from django.shortcuts import render, redirect
from django.views.generic import ListView
from django.http import HttpResponse, HttpResponseRedirect


from registration.models import Restaurant
from additem.models import Item


# Create your views here.

def menu(request): 

    restaurant_id= request.session['restaurant_ID']

    if request.method=='POST':
        if '_update' in request.POST:
            itemid = request.POST.get('id')
            
            item = Item.objects.get(item_id=itemid,restaurant=restaurant_id)
            item.quantity= request.POST.get('quantity')
            item.price=request.POST.get('price')
            item.save()
        
        elif '_delete' in request.POST:
            itemid = request.POST.get('id')
            Item.objects.get(item_id=itemid,restaurant=restaurant_id).delete()
            

    
    items= Item.objects.filter(restaurant_id= restaurant_id)
    context = {
        'items':items,
    }
    return render(request,'menu.html', context)

    
    

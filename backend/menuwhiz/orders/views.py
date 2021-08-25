from django.shortcuts import render,redirect
from django.http import HttpResponse, HttpResponseRedirect

from registration.models import Restaurant,Customer
from additem.models import Item
from .models import Order, Order_content, Placed_order


# Create your views here.

def orders(request):

    rest= request.session['restaurant_ID']
    
    if request.method=='POST':
        if '_view' in request.POST:
            orderid = request.POST.get('orderid')
        
        elif '_update' in request.POST:
            order_id = request.POST.get('orderid')
            c = request.POST.get('customerid')
            customer= Customer.objects.get(customer_id=c)
            
            order = Order.objects.get(order_id=order_id)
            
            order.status='C'
            order.save()
  
            
            
            placed_order= Placed_order.objects.filter(customer=customer, order= order).delete()
            
    
    restaurant=Restaurant.objects.get(restaurant_id=rest)
    
    placed_orders=Placed_order.objects.filter(restaurant = restaurant)
    
    context={
        'orders': placed_orders,
        }
    
    return render(request, 'orders.html', context)
  
       


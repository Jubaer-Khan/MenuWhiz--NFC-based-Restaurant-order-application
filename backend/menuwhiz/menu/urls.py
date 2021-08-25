from django.urls import path

from . import views,API

urlpatterns = [
    path('menu/', views.menu, name= 'menu' ), 
    path('PlaceOrder/', API.PlaceOrder),
    ]
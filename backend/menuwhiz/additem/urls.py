from django.urls import path

from . import views,API

urlpatterns = [
    path('additem/', views.additem, name= 'add item' ),
    path('RetrieveMenu/',API.RetrieveMenu), 
    path('RetrieveRestaurant/',API.RetrieveRestaurant),
    path('AddtempItem/',API.AddtempItem),
    path('RetrieveTempItem/',API.RetrieveTempItem),
    path('DeletetempItem/',API.DeletetempItem),
    ]
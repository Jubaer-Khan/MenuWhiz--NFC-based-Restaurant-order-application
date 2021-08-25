from django.urls import path

from . import views,API

urlpatterns = [
    path('registration/', views.registration, name= 'registration' ),
    path('nfc/',views.NFCconfig,name='NFC configuration'),
    path('CustomerRegistration/',API.CustomerRegistration),
    path('CustomerLogin/',API.CustomerLogin),
    path('GoogleCustomerRegistration/', API.GoogleCustomerRegistration),
    path('RetrieveCustomer/',API.RetrieveCustomer),
    ]
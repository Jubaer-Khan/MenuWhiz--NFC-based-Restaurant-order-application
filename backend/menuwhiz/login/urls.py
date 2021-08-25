from django.urls import path

from . import views

urlpatterns = [
    path('signin/', views.signin, name= 'sign in' ),
    path('signout/',views.signout,name='sign out'),
    ]
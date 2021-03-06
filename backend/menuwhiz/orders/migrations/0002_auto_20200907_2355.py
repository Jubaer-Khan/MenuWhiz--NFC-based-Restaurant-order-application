# Generated by Django 3.1 on 2020-09-07 17:55

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('additem', '0001_initial'),
        ('registration', '0003_customer'),
        ('orders', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='order',
            name='status',
            field=models.CharField(choices=[('P', 'placed'), ('C', 'completed')], default='P', max_length=2),
        ),
        migrations.CreateModel(
            name='Placed_order',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateField(auto_now_add=True)),
                ('customer', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='registration.customer')),
                ('order', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='orders.order')),
                ('restaurant', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='registration.restaurant')),
            ],
        ),
        migrations.CreateModel(
            name='Order_content',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('quantity', models.IntegerField()),
                ('total_price', models.DecimalField(decimal_places=2, max_digits=5)),
                ('item', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='additem.item')),
                ('order', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='orders.order')),
                ('restaurant', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='registration.restaurant')),
            ],
        ),
    ]

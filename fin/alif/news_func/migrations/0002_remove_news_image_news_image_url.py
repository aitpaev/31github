# Generated by Django 4.1.3 on 2023-10-25 13:50

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('news_func', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='news',
            name='image',
        ),
        migrations.AddField(
            model_name='news',
            name='image_url',
            field=models.URLField(blank=True, null=True),
        ),
    ]
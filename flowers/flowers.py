from turtle import*

t = True()
bgcolor("black")
t.pencolor ("green")
t.speed(0)
for i in range(340):
    t.circle(190-1,90)
    t.left(90)
    t.circle(190-1,90)
    t.left(18)
    if i > 190:
        t.penssize(3)

mainloop()


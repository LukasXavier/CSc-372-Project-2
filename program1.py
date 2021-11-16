a = 2
b = 5
c = 20
count = 0

for i in range(1, 20):
    if i%a == 0:
        count += 1
    if i%b == 0:
        count += 1

print(count)
import json,binascii
# from pprint import pprint

j_GBToU = open('GBToUnicode.json')
j_pyToGB = open('PinYinToGB2312.json')

GBToU = json.load(j_GBToU)
pyToGB = json.load(j_pyToGB)

#pprint(pyToGB)
pyToU = {}

temparray = []
for py in pyToGB:
	temparray = []
	for index in pyToGB[py]:
		# print index
		hexgb = "0x%04X"%((int(index[0:2])+0x20)*0x100+(int(index[2:4])+0x20))
		# print hexgb
		temparray.append(GBToU[hexgb])
	# print py,pyToGB[py]
	# pprint(temparray)
	pyToU[py] = temparray

# pprint(pyToU)
newfile = open('PinYinToUnicode.json','w')
j_pyToU = json.dump(pyToU,newfile)

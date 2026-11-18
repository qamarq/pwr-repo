import sys
from PIL import Image, ImageChops
for f in sys.argv[1:]:
    im = Image.open(f).convert('RGB')
    bg = Image.new('RGB', im.size, (255, 255, 255))
    box = ImageChops.difference(im, bg).getbbox()
    if box:
        m = 20
        im.crop((max(0, box[0]-m), max(0, box[1]-m), min(im.width, box[2]+m), min(im.height, box[3]+m))).save(f)
    print(f, Image.open(f).size)

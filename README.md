# Assignment_Two

## GetAll beers without Providing parameters for pagination

 - http://localhost:8888/beers/GetAll

## GetAll beers With providing a size and offset
* http://localhost:8888/beers/GetAll?size=5&offset=1

## Get Beer By ID
* http://localhost:8888/beers/GetById/1

## Get Beer description by following link in beer all beers or by 
- http://localhost:8888/beers/GetBeerDetailsId/1

## Get Beer image (By default will produce the large image unless specified in the url)
*  http://localhost:8888/beers/GetImage/1
*  http://localhost:8888/beers/GetImage/1?thumbnail=1

## Get Brewery Google Map
* http://localhost:8888/brewery/map/2

## Get Brewery QRCode (When Scanned will try to add the VCard)
* http://localhost:8888/brewery/QRCode/2

## Get Beers PDF Brochoure
* http://localhost:8888/beers/GetBeerPDF/1

## Get Images Zip file (Needs more work)
* http://localhost:8888/beers/GetImagesZipFile

## Load Swagger Documentation 
* http://localhost:8888/swagger-ui/index.html

// BV Ue3 WS2019/20 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15
package bv_ws1920;

public class MorphologicFilter {
	
	public enum FilterType { 
		DILATION("Dilation"),
		EROSION("Erosion");
		
		private final String name;       
	    private FilterType(String s) { name = s; }
	    public String toString() { return this.name; }
	};
	
	// filter implementations go here:
	
	public void copy(RasterImage src, RasterImage dst) {
		// TODO: just copy the image
		for(int y=0; y<src.height; y++){
			for(int x=0; x<src.width; x++){
				int pos = y * src.width + x;
				dst.argb[pos] = src.argb[pos];
			}
		}
	}
	
	public void dilation(RasterImage src, RasterImage dst, double radius) {
		// TODO: dilate the image using a structure element that is a neighborhood with the given radius
		int r = (int)(Math.round(radius));
		
		copy(src,dst); //damit Hintergrund nicht grau
		for(int y=0; y<src.height; y++){
			for(int x=0; x<src.width; x++){
				int pos = y * src.width + x;
				
				if(src.argb[pos]==0xFF000000){ //Wenn pixel src = schwarz
					
					//Kernel
					int x0 = x-r;
					int x1 = x+r;												//    y0
					int y0 = y-r;												// x0 HS x1
					int y1 = y+r;												//    y1
					
					//Randbehandlung
					if(x0 < 0){
						x0 = 0;
					} if(x1 > src.width-1){
						x1 = src.width-1;
					} if(y0 < 0){
						y0 = 0;
					} if(y1 > src.height-1){
						y1 = src.height-1;
					}
					
					//Verlauf im Kernel (Nachbarschaft)
					for(int y_n=y0; y_n<=y1; y_n++){  //- bis +
						for(int x_n=x0; x_n<=x1; x_n++){
							double distance = Math.sqrt((x_n-x)*(x_n-x)+(y_n-y)*(y_n-y));
							
							if(distance<=radius){
								int pos_n = y_n * dst.width + x_n; //src.width juga bisa soalnya ga ada berubah Ursprung
								dst.argb[pos_n] = src.argb[pos];
							}
						}
					}
				}
			}
		}
	}
	
	public void erosion(RasterImage src, RasterImage dst, double radius) {
		// TODO: erode the image using a structure element that is a neighborhood with the given radius
		src.invert();
		dilation(src,dst,radius);
		dst.invert();
		src.invert(); //supaya si gambar di src balik lagi ke warna awal
	}
}

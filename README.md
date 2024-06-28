# KotlinCurve
This is an exercise of the following.
- Canvas Path cubic
- Cubic Spline algorithm
- Bisection search
- Median filter for de-noising
- ComposeUI & xml Layout

### Smoothing
1st exercise: Canvas.Path.cubicTo() February 20, 2020 \
<img width="420" alt="Screen Shot 2024-05-29 at 5 20 24 PM" src="https://github.com/yeuchi/KotlinCurve/assets/1282659/31c94b3d-7ef7-49f6-8d73-34efd3d78767">

Curve by calculating tangent from each point on curve. \
Control points are blue in diagram. <sup>[1]</sup> \
<img width="250" src="https://user-images.githubusercontent.com/1282659/154866555-d2af3d03-322e-4343-89ea-3a693e4ff14e.png"> 

### De-noising (Median)
2nd exercise; Median Filter February 20, 2020 \
Statistical median is a simple and effective filter for removing impulse (aka. Salt & Pepper) noise from signal.
<img width="420" alt="Screen Shot 2024-05-29 at 5 20 39 PM" src="https://github.com/yeuchi/KotlinCurve/assets/1282659/0d9a8a51-9c60-4dc5-a896-4da28d9f7b17">

### Cubic Spline
3rd exercise; Cubic spline interpolation February 27, 2020 \
Spline a popular curve smoothing alogrithm,<sup>[4]</sup> \
<img width="420" alt="Screen Shot 2024-05-29 at 5 20 54 PM" src="https://github.com/yeuchi/KotlinCurve/assets/1282659/210c4db7-1b9b-4313-9fa2-07d8481ee9bc">

### Bonus: text on curve
So cool that Android SDK provides this feature right out of the box. \
<img width="420" src="https://github.com/yeuchi/KotlinCurve/assets/1282659/218b130b-75d4-4b50-94c3-71d73a4a641d">

### Android Studio
Iguana 2023.2.1 Patch 2 April 3, 2024\
<img width="500" src="https://github.com/yeuchi/LinearRegression/assets/1282659/4faf30c4-4425-4201-846b-b5bd32c9fc42"/>

### Test Device
Google Pixel6a 

# References

1. Drawing Bezier Curves like in Google Material Rally by Chan Myae Aung, September 25, 2019 \
   https://proandroiddev.com/drawing-bezier-curve-like-in-google-material-rally-e2b38053038c

2. LinearRegression Exercise by Chi Yeung, March 31, 2020 \
   https://github.com/yeuchi/LinearRegression
   
3. Android Canvas.Path.cubicTo by Android Developers (Documentation) \
   https://developer.android.com/reference/android/graphics/Path#cubicTo(float,%20float,%20float,%20float,%20float,%20float)

4. Numerical Recipes in C 2nd Edition by Press, Vetterling, Teukolsky, Flannery
   
6. Math - Quadratic Bézier Curve: Calculate Points, StackOverflow, Sep 13, 2016 \
   https://stackoverflow.com/questions/5634460/quadratic-b%C3%A9zier-curve-calculate-points

<video src="https://github.com/yeuchi/KotlinCurve/assets/1282659/c98bf488-661d-4768-9e73-0e0b1e422f47"/>

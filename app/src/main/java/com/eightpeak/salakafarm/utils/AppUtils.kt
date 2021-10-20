package com.eightpeak.salakafarm.utils

import android.R
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.*
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

class AppUtils {
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

//    fun addWishlistSnackBar(context: Context, view: View, message: String?, action: String?) {
//        val snackbar_updated1 = Snackbar.make(view, message!!, Snackbar.LENGTH_INDEFINITE)
//        snackbar_updated1.setAction(
//            action
//        ) { context.startActivity(Intent(context, WishListActivity::class.java)) }
//        /** Snackbar message and action TextViews are placed inside a LinearLayout
//         */
//        val snackBarLayout1 = snackbar_updated1.view as SnackbarLayout
//        for (i in 0 until snackBarLayout1.childCount) {
//            val parent = snackBarLayout1.getChildAt(i)
//            if (parent is LinearLayout) {
//                parent.rotation = 180f
//                break
//            }
//        }
//        snackbar_updated1.setTextColor(Color.WHITE)
//        snackbar_updated1.setBackgroundTint(Color.BLACK)
//        snackbar_updated1.setActionTextColor(Color.parseColor("#008000"))
//        snackbar_updated1.show()
//        view.postDelayed({ snackbar_updated1.dismiss() }, 2000)
//    }

//    fun addCartSnackBar(context: Context, view: View, message: String?, action: String?) {
//        val snackbar_updated1 = Snackbar.make(view, message!!, Snackbar.LENGTH_INDEFINITE)
//        snackbar_updated1.setAction(
//            action
//        ) { context.startActivity(Intent(context, CartActivity::class.java)) }
//        /** Snackbar message and action TextViews are placed inside a LinearLayout
//         */
//        val snackBarLayout1 = snackbar_updated1.view as SnackbarLayout
//        for (i in 0 until snackBarLayout1.childCount) {
//            val parent = snackBarLayout1.getChildAt(i)
//            if (parent is LinearLayout) {
//                parent.rotation = 180f
//                break
//            }
//        }
//        snackbar_updated1.setTextColor(Color.WHITE)
//        snackbar_updated1.setBackgroundTint(Color.BLACK)
//        snackbar_updated1.setActionTextColor(Color.parseColor("#008000"))
//        snackbar_updated1.show()
//        view.postDelayed({ snackbar_updated1.dismiss() }, 2000)
//    }

//   override static fun isValidEmailId(email: String?): Boolean {
//        return Pattern.compile(
//            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
//                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\\\.([0-1]?"
//                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
//                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
//        ).matcher(email).matches()
//    }

    fun showProgressBar(showProgressBar: Boolean, progressBar: ProgressBar) {
        if (showProgressBar) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

//    fun convertErrors(response: ResponseBody): ApiError? {
//        val converter: Converter<ResponseBody, ApiError> =
//            ServiceConfig.retrofit().responseBodyConverter(
//                ApiError::class.java, arrayOfNulls<Annotation>(0)
//            )
//        var apiError: ApiError? = null
//        try {
//            apiError = converter.convert(response)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return apiError
//    }

    fun calculateDistance(
        startLat: Double?,
        startLng: Double?,
        endLat: Double?,
        endLng: Double?
    ): Float {
        val distance: Float
        val startingLocation = Location("starting point")
        startingLocation.latitude = (startLat)!!
        startingLocation.longitude = (startLng)!!

        //Get the target location
        val endingLocation = Location("ending point")
        endingLocation.latitude = (endLat)!!
        endingLocation.longitude = (endLng)!!
        distance = startingLocation.distanceTo(endingLocation)
        return distance
    }

    fun freezeUi(activity: Activity, showProgress: Boolean) {
        if (showProgress) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

//    public static boolean isDebug() {
//        return BuildConfig.BUILD_TYPE.equals("debug");
//    }

    //    public static boolean isDebug() {
    //        return BuildConfig.BUILD_TYPE.equals("debug");
    //    }
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showSnackBar(view: View?, message: String?) {
        Snackbar.make((view)!!, (message)!!, Snackbar.LENGTH_LONG).show()
    }



    //    public static boolean isNetworkAvailable() {
    //        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    //        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
    //        return netInfo != null && netInfo.isConnectedOrConnecting();
    //    }
    fun setError(editText: EditText) {
        editText.error = "Field required"
        editText.requestFocus()
    }

    fun invalidEmail(editText: EditText) {
        editText.error = "Invalid Email"
        editText.requestFocus()
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager? =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        assert(imm != null)
        imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun showDatePicker(context: Context?, listener: OnDateSetListener?) {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val dialog = DatePickerDialog(
            (context)!!,
            R.style.Theme_Holo_Light_Dialog_MinWidth,
            listener,
            year,
            month,
            day
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun checkIfSmallThanTen(value: Int): String? {
        return if (value < 10) {
            "0$value"
        } else {
            value.toString()
        }
    }


    @Throws(IOException::class)
    fun getCorrectlyOrientedGalleryImage(context: Context, photoUri: Uri, maxWidth: Int): Bitmap? {
        var `is` = context.contentResolver.openInputStream(photoUri)
        val dbo = BitmapFactory.Options()
        dbo.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, dbo)
        `is`!!.close()
        val rotatedWidth: Int
        val rotatedHeight: Int
        val orientation = getOrientation(context, photoUri)
        if (orientation == 90 || orientation == 270) {
            //  Log.d("ImageUtil", "Will be rotated");
            rotatedWidth = dbo.outHeight
            rotatedHeight = dbo.outWidth
        } else {
            rotatedWidth = dbo.outWidth
            rotatedHeight = dbo.outHeight
        }
        var srcBitmap: Bitmap?
        `is` = context.contentResolver.openInputStream(photoUri)
        //  Log.d("ImageUtil", String.format("rotatedWidth=%s, rotatedHeight=%s, maxWidth=%s",        rotatedWidth, rotatedHeight, maxWidth));
        if (rotatedWidth > maxWidth || rotatedHeight > maxWidth) {
            val widthRatio = (rotatedWidth.toFloat()) / (maxWidth.toFloat())
            val heightRatio = (rotatedHeight.toFloat()) / (maxWidth.toFloat())
            val maxRatio = Math.max(widthRatio, heightRatio)
            //Log.d("ImageUtil", String.format("Shrinking. maxRatio=%s",            maxRatio));

            // Create the bitmap from file
            val options = BitmapFactory.Options()
            options.inSampleSize = maxRatio.toInt()
            srcBitmap = BitmapFactory.decodeStream(`is`, null, options)
        } else {
            // Log.d("ImageUtil", String.format("No need for Shrinking. maxRatio=%s", 1));
            srcBitmap = BitmapFactory.decodeStream(`is`)
            //Log.d("ImageUtil", String.format("Decoded bitmap successful"));
        }
        `is`!!.close()

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */if (orientation > 0) {
            val matrix = Matrix()
            matrix.postRotate(orientation.toFloat())
            srcBitmap = Bitmap.createBitmap(
                (srcBitmap)!!, 0, 0, srcBitmap.width,
                srcBitmap.height, matrix, true
            )
        }
        return srcBitmap
    }

    private fun getOrientation(context: Context, photoUri: Uri): Int {
        val cursor = context.contentResolver.query(
            photoUri,
            arrayOf(MediaStore.Images.ImageColumns.ORIENTATION),
            null,
            null,
            null
        )
        if (cursor == null || cursor.count != 1) {
            return 90 //Assuming it was taken portrait
        }
        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    fun saveImage(context: Context, myBitmap: Bitmap): String? {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "Share")
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.path), arrayOf("image/jpeg"), null)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    fun setPic(imagePath: String, imageView: ImageView): Bitmap? {
        /* Get the size of the ImageView */
        val targetW = imageView.width
        val targetH = imageView.height

        /* Get the size of the image */
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, bmOptions)
        val cameraPhotoWidth = bmOptions.outWidth
        val cameraPhotoHeight = bmOptions.outHeight

        /* Figure out which way needs to be reduced less */
        var scaleFactor = 1
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(cameraPhotoWidth / targetW, cameraPhotoHeight / targetH)
        }

        /* Set bitmap options to scale the image decode target */bmOptions.inJustDecodeBounds =
            false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        /* Decode the JPEG file into a Bitmap */
        val bitmap = BitmapFactory.decodeFile(imagePath, bmOptions)
        return adjustImageOrientation(bitmap, imagePath)
    }

    private fun adjustImageOrientation(bitmap: Bitmap, currentPhotoPath: String): Bitmap? {
        var bitmap = bitmap
        val exif: ExifInterface
        try {
            exif = ExifInterface(currentPhotoPath)
            val exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            var rotate = 0
            when (exifOrientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            }
            if (rotate != 0) {
                val w = bitmap.width
                val h = bitmap.height
                //setting pre rotate
                val mtx = Matrix()
                mtx.preRotate(rotate.toFloat())
                //Rotating Bitmap & convert to ARGB_8888
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

//    fun saveBitmapToFile(file: File, imagePath: String?): File? {
//        try {
//            // BitmapFactory options to downsize the image
//            val o = BitmapFactory.Options()
//            o.inJustDecodeBounds = true
//            o.inSampleSize = 6
//            // factor of downsizing the image
//            var inputStream = FileInputStream(file)
//            //Bitmap selectedBitmap = null;
//            BitmapFactory.decodeStream(inputStream, null, o)
//            inputStream.close()
//
//            // The new size we want to scale to
//            val REQUIRED_SIZE = 75
//
//            // Find the correct scale value. It should be the power of 2.
//            var scale = 1
//            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
//                o.outHeight / scale / 2 >= REQUIRED_SIZE
//            ) {
//                scale *= 2
//            }
//            val o2 = BitmapFactory.Options()
//            o2.inSampleSize = scale
//            inputStream = FileInputStream(file)
//            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
//            val correctBitmap: Bitmap = AppUtils.adjustImageOrientation(selectedBitmap, imagePath)
//            inputStream.close()
//            // here i override the original image file
//            file.createNewFile()
//            val outputStream = FileOutputStream(file)
//            correctBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
//            return file
//        } catch (e: Exception) {
//            return null
//        }
//    }
//
//    fun setDrawableLeft(context: Context?, editText: EditText, image: Int) {
//        editText.setCompoundDrawablesWithIntrinsicBounds(
//            AppCompatResources.getDrawable(
//                (context)!!,
//                image
//            ), null, null, null
//        )
//    }
//
//    fun isNetworkAvailable(): Boolean {
//        val connectivityManager =
//            App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetworkInfo = connectivityManager.activeNetworkInfo
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected
//    }
//
//    fun showNoInternet(view: View?) {
//        Snackbar.make((view)!!, "Please check your internet connection", Snackbar.LENGTH_LONG)
//            .show()
//    }
//
//    fun setLocal(activity: Activity, langCode: String?) {
//        val locale = Locale(langCode)
//        Locale.setDefault(locale)
//        val resources = activity.resources
//        val config = resources.configuration
//        config.setLocale(locale)
//        resources.updateConfiguration(config, resources.displayMetrics)
//    }
}
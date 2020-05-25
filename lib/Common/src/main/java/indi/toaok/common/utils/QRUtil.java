package indi.toaok.common.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QRUtil {
    /**
     * QR_WIDETH 二维码宽度 自行设置
     * QR_HIGHT 二维码高度 自行设置
     * imgQrcode 一个ImageView
     */
    public static Bitmap createQRImage( String address,int qrSize) {
        Bitmap bitmap = null;
        try {
            //判断URL合法性
            if (address == null || "".equals(address) || address.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(address,
                    BarcodeFormat.QR_CODE, qrSize, qrSize, hints);
            int[] pixels = new int[qrSize * qrSize];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < qrSize; y++) {
                for (int x = 0; x < qrSize; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * qrSize + x] = 0xff000000;
                    } else {
                        pixels[y * qrSize + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(qrSize, qrSize, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, qrSize, 0, 0, qrSize, qrSize);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    private static int IMAGE_HALFWIDTH = 50;//宽度值，影响中间图片大小

    public static Bitmap createQRCodeWithLogo(String address, Bitmap mBitmap) {
        return createQRCodeWithLogo(address,500,mBitmap);
    }

    /**
     * 生成带logo的二维码，logo默认为二维码的1/5
     *
     * @param address 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小（）
     * @param mBitmap logo文件
     * @return bitmap
     */
    public static Bitmap createQRCodeWithLogo(String address, int size, Bitmap mBitmap) {
        try {

            //判断URL合法性
            if (address == null || "".equals(address) || address.length() < 1) {
                return null;
            }

            IMAGE_HALFWIDTH = size/10;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            /*
             * 设置容错级别，默认为ErrorCorrectionLevel.L
             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
             */
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(address,
                    BarcodeFormat.QR_CODE, size, size, hints);

            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;

            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH
                    / mBitmap.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, false);

            int[] pixels = new int[size * size];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {
                        //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixels[y * width + x] = mBitmap.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * size + x] = 0xff000000;
                        } else {
                            pixels[y * size + x] = 0xffffffff;
                        }
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
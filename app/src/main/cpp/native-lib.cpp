#include <jni.h>
#include <string>
#include <vector>

/*std::string admob_pub_id="pub-3143525559772667::";
std::string admob_app_id="ca-app-pub-3143525559772667~8737171114::";
std::string admob_banner_ad_unit="ca-app-pub-3143525559772667/5991588277::";
std::string admob_rectangle_ad_unit="ca-app-pub-3143525559772667/5545877477::";
std::string admob_interstial_ad_id="ca-app-pub-3143525559772667/4232795803::";
std::string ad_mob_native_ad_id="ca-app-pub-3143525559772667/5033729827::";*/

/*std::string admob_pub_id="pub-3940256099942544::";
std::string admob_app_id="ca-app-pub-3940256099942544~3347511713::";
std::string admob_banner_ad_unit="ca-app-pub-3940256099942544/6300978111::";
std::string admob_rectangle_ad_unit="ca-app-pub-3940256099942544/6300978111::";
std::string admob_interstial_ad_id="ca-app-pub-3940256099942544/1033173712::";
std::string ad_mob_native_ad_id="ca-app-pub-3940256099942544/2247696110::";*/

std::string admob_pub_id="pub-3940256099942544::";
std::string admob_app_id="ca-app-pub-3940256099942544~3347511713::";
std::string admob_banner_ad_unit="/6499/example/banner::";
std::string admob_rectangle_ad_unit="/6499/example/banner::";
std::string admob_interstial_ad_id="/6499/example/interstitial::";
std::string ad_mob_native_ad_id="/6499/example/native::";
std::string ad_mob_open_ad_id="/6499/example/app-open::";

/*std::string admob_pub_id="pub-3143525559772667::";
std::string admob_app_id="ca-app-pub-3143525559772667~3688049099::";
std::string admob_banner_ad_unit="/22405025169,22516821253/com.sparkapps.photocompressorconverter.yp.banner::";
std::string admob_rectangle_ad_unit="/22405025169,22516821253/com.sparkapps.photocompressorconverter.yp.banner::";
std::string admob_interstial_ad_id="/22405025169,22516821253/com.sparkapps.photocompressorconverter.yp.interstitial::";
std::string ad_mob_native_ad_id="/22405025169,22516821253/com.sparkapps.photocompressorconverter.yp.native::";
std::string ad_mob_open_ad_id="/22405025169,22516821253/com.sparkapps.photocompressorconverter.yp.app-open::";*/


std::string MERCHANT_ID="17478396524943591084::";
std::string Inapp_PublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmtey6buzi8VaZydf2thCCeYHwmHT5ljUC+MFykwoC9A/ECBTeiHTseTQC9VJxlIPBVmEVeubRAN1wwq48k1jNPwd+8FtxlXDdCJyNl4wTNUE6xgaOyooyIwXuqyxA2DBVZ/I65sa6jP8qUmiLLrCAy5fM5b5gjOuwaMacp2o7LSwzb59UxgMbsz90P6qrsD1HTe4erRVLvYG+8Z/FyxEUlM/Gudcj83tZNQ6EAWfyh9GjeWUz8yXZsGH8bnbyp28thHqi+wzrLlUe5pK+GlvwqysxcZPoof4g9zbIvZdw9aE3gjmlgl5Zev6IwULq9R1eutG2FOKEoyf/eIoIhdA7QIDAQAB::";
std::string firebase_key="AAAAyNAUt-U:APA91bGmdPNCOEkleN-fQPquEcW9A9qD6AIcCo77St-HCivK4wGqnMIqdfpxcFR_dwKXe02_yexzl9x5QK7pKjYggFMoyD-QJebl3HIdqFeZyZTdcN16R4QfQpaqPPyRc-mcWe8ubwjs::";

/*std::string fb_native_banner_id="1546007372201777_1546008895534958::";
std::string fb_native_id="1546007372201777_1546008532201661::";
 std::string fb_native_interstitial_id="356710181618841_356710748285451::";*/

std::string fb_native_banner_id="YOUR_PLACEMENT_ID::";
std::string fb_native_id="YOUR_PLACEMENT_ID::";
std::string fb_native_interstitial_id="YOUR_PLACEMENT_ID::";

extern "C" JNIEXPORT jstring
JNICALL
Java_com_brb_bluetoothscanner_c_ACApplication_StringAdmobCode(
        JNIEnv *env,
        jobject /* this */) {
    std::string final_s = admob_pub_id + admob_app_id+admob_banner_ad_unit+admob_interstial_ad_id+ad_mob_native_ad_id+MERCHANT_ID+Inapp_PublicKey+firebase_key+admob_rectangle_ad_unit+ad_mob_open_ad_id;
    return env->NewStringUTF(final_s.c_str());
}

extern "C" JNIEXPORT jstring
JNICALL
Java_com_brb_bluetoothscanner_c_ACApplication_StringFBCode(
        JNIEnv *env,
        jobject /* this */) {
    std::string final_s = admob_pub_id + admob_app_id+admob_banner_ad_unit+admob_interstial_ad_id+ad_mob_native_ad_id+MERCHANT_ID+Inapp_PublicKey+firebase_key+admob_rectangle_ad_unit+ad_mob_open_ad_id;
    return env->NewStringUTF(final_s.c_str());
}


extern "C" JNIEXPORT jstring
JNICALL
Java_com_brb_bluetoothscanner_c_ACApplication_StringAPI(
        JNIEnv *env,
        jobject /* this */) {
    /*http://ec2-18-116-59-188.us-east-2.compute.amazonaws.com/testAdCodes.txt*/
    std::string final_s = "http://ec2-18-116-59-188.us-east-2.compute.amazonaws.com/testAdCodes.txt";
    return env->NewStringUTF(final_s.c_str());
}
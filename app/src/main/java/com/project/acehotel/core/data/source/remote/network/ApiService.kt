package com.project.acehotel.core.data.source.remote.network

import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.data.source.remote.response.booking.AddBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.BookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.ListBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.PayBookingResponse
import com.project.acehotel.core.data.source.remote.response.hotel.CreateHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.HotelRecapResponse
import com.project.acehotel.core.data.source.remote.response.hotel.HotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ManageHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ManageHotelResultItem
import com.project.acehotel.core.data.source.remote.response.images.UploadImagesResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryUpdateHistoryItem
import com.project.acehotel.core.data.source.remote.response.note.NoteResponse
import com.project.acehotel.core.data.source.remote.response.room.CheckoutBody
import com.project.acehotel.core.data.source.remote.response.room.ListRoomResponse
import com.project.acehotel.core.data.source.remote.response.room.RoomResponse
import com.project.acehotel.core.data.source.remote.response.tag.AddTagResponse
import com.project.acehotel.core.data.source.remote.response.tag.ListTagsByIdResponse
import com.project.acehotel.core.data.source.remote.response.tag.ListTagsResponse
import com.project.acehotel.core.data.source.remote.response.tag.ReaderQueryResponse
import com.project.acehotel.core.data.source.remote.response.tag.ReaderResponse
import com.project.acehotel.core.data.source.remote.response.user.ListUserResponse
import com.project.acehotel.core.data.source.remote.response.user.UserResponse
import com.project.acehotel.core.data.source.remote.response.visitor.ListVisitorResponse
import com.project.acehotel.core.data.source.remote.response.visitor.VisitorResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface ApiService {

    // AUTH

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("auth/refresh-tokens")
    suspend fun refreshToken(
        @Field("refreshToken") refreshToken: String
    ): RefreshTokenResponse

    @POST("auth/forgot-password")
    @FormUrlEncoded
    suspend fun forgetPassword(
        @Field("email") email: String
    ): Response<AuthResponse>

    // AUTH


    // USER

    @GET("users")
    suspend fun getUserByHotel(
        @Query("hotel_id") hotelId: String,
    ): ListUserResponse

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String,
        @Query("hotel_id") hotelId: String,
    ): UserResponse

    @PATCH("users/{id}")
    @FormUrlEncoded
    suspend fun updateUser(
        @Path("id") id: String,
        @Query("hotel_id") hotelId: String,
        @Field("email") email: String,
    ): UserResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: String,
        @Query("hotel_id") hotelId: String,
    ): Response<UserResponse>

    // USER


    // INVENTORY

    @GET("inventory")
    suspend fun getListInventory(
        @Query("hotel_id") hotelId: String,
        @QueryMap filters: Map<String, String>
    ): InventoryListResponse

    @GET("inventory/{id}")
    suspend fun getDetailInventory(
        @Path("id") id: String,
        @Query("hotel_id") hotelId: String,
    ): InventoryDetailResponse

    @GET("inventory/{id}/history")
    suspend fun getInventoryHistoryList(
        @Path("id") id: String,
        @Query("key") key: String,
    ): List<InventoryUpdateHistoryItem?>?

    @POST("inventory")
    @FormUrlEncoded
    suspend fun addInventory(
        @Query("hotel_id") hotelId: String,
        @Field("name") name: String,
        @Field("type") type: String,
        @Field("stock") stock: Int,
    ): InventoryDetailResponse

    @PATCH("inventory/{id}")
    @FormUrlEncoded
    suspend fun updateInventory(
        @Path("id") id: String,
        @Query("hotel_id") hotelId: String,
        @Field("name") name: String,
        @Field("type") type: String,
        @Field("stock") stock: Int,
        @Field("title") title: String,
        @Field("description") description: String,
    ): InventoryDetailResponse

    @DELETE("inventory")
    suspend fun deleteInventory(
        @Query("inventory_id") inventoryId: String,
        @Query("hotel_id") hotelId: String,
    ): Response<InventoryDetailResponse>

    @GET("tag/id/{readerId}")
    suspend fun getTagById(
        @Path("readerId") readerId: String
    ): ListTagsByIdResponse

    @GET("tag")
    suspend fun getTag(): ListTagsResponse

    @FormUrlEncoded
    @POST("tag")
    suspend fun addTag(
        @Field("tid") tid: String,
        @Field("inventory_id") inventoryId: String,
    ): AddTagResponse

    @POST("tag/{id}/query")
    suspend fun setQueryTag(
        @Path("id") readerId: String,

        @Query("state") state: Boolean,
    ): ReaderQueryResponse

    @FormUrlEncoded
    @PATCH("Reader/{id}")
    suspend fun updateReader(
        @Path("id") readerId: String,

        @Field("tag_expired") powerGain: String,
        @Field("read_interval") readInterval: String
    ): ReaderResponse

    @GET("Reader/{id}")
    suspend fun getReader(
        @Path("id") readerId: String,
    ): ReaderResponse

    // INVENTORY

    // HOTELS

    @FormUrlEncoded
    @POST("hotels")
    suspend fun addHotel(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("contact") contact: String,

        @Field("regular_room_count") regularRoomCount: Int,
        @Field("regular_room_image_path") regularRoomImage: String,
        @Field("exclusive_room_count") exclusiveRoomCount: Int,
        @Field("exclusive_room_image_path") exclusiveRoomImage: String,
        @Field("regular_room_price") regularRoomPrice: Int,
        @Field("exclusive_room_price") exclusiveRoomPrice: Int,
        @Field("extra_bed_price") extraBedPrice: Int,

        @Field("owner_name") ownerName: String,
        @Field("owner_email") ownerEmail: String,
        @Field("owner_password") ownerPassword: String,

        @Field("receptionist_name") receptionistName: String,
        @Field("receptionist_email") receptionistEmail: String,
        @Field("receptionist_password") receptionistPassword: String,

        @Field("inventory_staff_name") inventoryName: String,
        @Field("inventory_staff_email") inventoryEmail: String,
        @Field("inventory_staff_password") inventoryPassword: String,
    ): CreateHotelResponse

    @GET("hotels")
    suspend fun getListHotel(): ManageHotelResponse

    @GET("hotels/{id}")
    suspend fun getHotel(
        @Path("id") id: String
    ): HotelResponse

    @FormUrlEncoded
    @PATCH("hotels/{id}")
    suspend fun updateHotel(
        @Path("id") id: String,

        @Field("name") name: String,
        @Field("address") address: String,
        @Field("contact") contact: String,

        @Field("regular_room_count") regularRoomCount: Int,
        @Field("regular_room_image_path") regularRoomImage: String,
        @Field("exclusive_room_count") exclusiveRoomCount: Int,
        @Field("exclusive_room_image_path") exclusiveRoomImage: String,
        @Field("regular_room_price") regularRoomPrice: Int,
        @Field("exclusive_room_price") exclusiveRoomPrice: Int,
        @Field("extra_bed_price") extraBedPrice: Int,
    ): HotelResponse

    @DELETE("hotels/{id}")
    suspend fun deleteHotel(
        @Path("id") id: String
    ): Response<ManageHotelResultItem>

    @FormUrlEncoded
    @PATCH("hotels/{id}")
    suspend fun updateHotelPrice(
        @Path("id") id: String,
        @Field("discount_code") discountCode: String,
        @Field("discount_amount") discountAmount: Int,
        @Field("regular_room_price") regularRoomPrice: Int,
        @Field("exclusive_room_price") exclusiveRoomPrice: Int,
        @Field("extra_bed_price") extraBedPrice: Int,
    ): HotelResponse

    @GET("recap")
    suspend fun getHotelRecap(
        @Query("checkin_date") checkinDate: String
    ): HotelRecapResponse

    // HOTELS


    // VISITOR

    @GET("visitors")
    suspend fun getListVisitor(
        @QueryMap filters: Map<String, String>
    ): ListVisitorResponse

    @GET("visitors/{id}")
    suspend fun getDetailVisitor(
        @Path("id") id: String,
    ): VisitorResponse

    @POST("visitors")
    @FormUrlEncoded
    suspend fun addVisitor(
        @Field("hotel_id") hotelId: String,

        @Field("name") name: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("identity_num") identityNum: String,
        @Field("path_identity_image") pathIdentityImage: String,
    ): VisitorResponse

    @PATCH("visitors/{id}")
    @FormUrlEncoded
    suspend fun updateVisitor(
        @Path("id") id: String,
        @Query("hotel_id") hotelId: String,

        @Field("name") name: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("identity_num") identityNum: String,
        @Field("path_identity_image") pathIdentityImage: String,
    ): VisitorResponse

    @DELETE("visitors/{id}")
    suspend fun deleteVisitor(
        @Path("id") id: String,
        @Query("hotel_id") hotelId: String,
    ): Response<VisitorResponse>

    // VISITOR


    // BOOKING

    @POST("bookings")
    @FormUrlEncoded
    suspend fun addBooking(
        @Field("hotel_id") hotelId: String,
        @Field("visitor_id") visitorId: String,
        @Field("checkin_date") checkinDate: String,
        @Field("duration") duration: Int,
        @Field("room_count") roomCount: Int,
        @Field("extra_bed") extraBed: Int,
        @Field("type") type: String,
    ): AddBookingResponse

    @GET("bookings/hotel/{id}?limit=999")
    suspend fun getListBookingByHotel(
        @Path("id") id: String,
        @Query("checkin_date") filterDate: String,
        @Query("visitor_name") visitorName: String,
    ): ListBookingResponse

    @GET("bookings/hotel/{id}")
    suspend fun getPagingListBookingByHotel(
        @Path("id") id: String,
        @Query("checkin_date") filterDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("visitor_name") visitorName: String
    ): ListBookingResponse

    @GET("bookings/room/{id}")
    suspend fun getListBookingByRoom(
        @Path("id") id: String,
        @Query("checkin_date") filterDate: String,
    ): ListBookingResponse

    @GET("bookings/visitor/{id}")
    suspend fun getListBookingByVisitor(
        @Path("id") id: String
    ): ListBookingResponse

    @GET("bookings/visitor/{id}")
    suspend fun getPagingListBookingByVisitor(
        @Path("id") id: String,
        @Query("checkin_date") filterDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ListBookingResponse

    @GET("bookings/room/{id}")
    suspend fun getPagingListBookingByRoom(
        @Path("id") id: String,
        @Query("checkin_date") filterDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ListBookingResponse

    @GET("bookings/{id}")
    suspend fun getDetailBooking(
        @Path("id") id: String
    ): BookingResponse

    @POST("bookings/pay/{id}")
    @FormUrlEncoded
    suspend fun payBooking(
        @Path("id") id: String,
        @Field("path_transaction_proof") pathTransactionProof: String,
    ): PayBookingResponse

    @POST("bookings/discount/{id}")
    @FormUrlEncoded
    suspend fun addDiscount(
        @Path("id") id: String,
        @Field("discount_code") discountCode: String,
    ): PayBookingResponse

    @DELETE("bookings/{id}")
    suspend fun deleteBooking(
        @Path("id") id: String
    ): Response<BookingResponse>

    // BOOKING


    // ROOM

    @GET("rooms/hotel/{id}")
    suspend fun getListRoomByHotel(
        @Path("id") id: String,
        @Query("limit") limit: Int = 99
    ): ListRoomResponse

    @GET("rooms/{id}")
    suspend fun getRoomDetail(
        @Path("id") id: String,
    ): RoomResponse

    @POST("rooms/checkin/{id}")
    @FormUrlEncoded
    suspend fun roomCheckin(
        @Path("id") id: String,
        @Field("checkin_date") checkinDate: String,
        @Field("booking_id") bookingId: String,
        @Field("visitor_id") visitorId: String,
    ): RoomResponse

    @POST("rooms/checkout/{id}")
    suspend fun roomCheckout(
        @Path("id") id: String,
        @Body checkoutBody: CheckoutBody
    ): RoomResponse

    @DELETE("rooms/{id}")
    suspend fun deleteRoom(
        @Path("id") id: String
    ): Response<RoomResponse>

    // ROOM


    // IMAGES

    @Multipart
    @POST("uploads")
    suspend fun uploadImage(
        @Part image: List<MultipartBody.Part>
    ): UploadImagesResponse

    // IMAGES


    // IMAGES
    @GET("note/{id}")
    suspend fun getNoteDetail(
        @Path("id") id: String
    ): NoteResponse

    // IMAGES
}
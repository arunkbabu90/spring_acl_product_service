package com.estrix.productservice.repository
//
//import com.estrix.productservice.domain.Product
//import com.estrix.productservice.domain.ProductDetail
//import org.springframework.stereotype.Component

//@Component
//class ProductDumERepository {
//    private val productList = mutableListOf<Product>()
//    private val productDetailList = mutableListOf<ProductDetail>()
//
//    init {
//        val pl = listOf(
//            Product("A_PRD_1000000", "Garmin Venu Sq 2 - Music Edition", "/garmin-venu-2.jpg", 1199.00),
//            Product("A_PRD_1000001", "Apple Watch Series 8 GPS", "/apple-watch-series-8.jpg", 1039.00),
//            Product("A_PRD_1000002", "Apple Watch Series 7 GPS", "/apple-watch-series-8.jpg", 989.00),
//            Product("A_PRD_1000003", "Swiss Military Alps 2 Smartwatch", "/swiss-military-alps-2.jpg", 229.00),
//            Product("A_PRD_1000004", "Uniq Dante 44/42mm Mesh Steel", "/uniq-dante.jpg", 109.00),
//            Product("A_PRD_1000005", "Samsung Galaxy Watch6 44mm", "/samsung-galaxy-watch6.jpg", 105.00),
//            Product("A_PRD_1000006", "COROS APEX 2", "/coros-apex-2.jpg", 1699.00),
//            Product("A_PRD_1000007", "Apple Watch Series 4 GPS", "/apple-watch-series-8.jpg", 1039.00),
//            Product("A_PRD_1000008", "Apple Watch Series 5 GPS", "/apple-watch-series-8.jpg", 1039.00),
//            Product("A_PRD_1000009", "Fitbit Charge 6 Advanced Fitness", "/fitbit-charge-6.jpg", 699.00)
//        )
//
//        val pdl = listOf(
//            ProductDetail(
//                pid = "A_PRD_100000",
//                brand = "Garmin",
//                name = "Garmin Venu Sq 2 - Music Edition",
//                price = 1199.00,
//                images = listOf("/garmin-venu-2.jpg", "/garmin-venu-2-1.jpg", "/garmin-venu-2-2.jpg"),
//                description = """ CHARGE LESS: With up to 11 days of battery life in smartwatch mode and up to 26 hours in GPS mode, you won’t have to take off your watch every night to charge it �” meaning you can keep up with your well-being 24/7.
//                                        HEY, NICE WATCH: The Venu Sq 2 smartwatch has a sleek design that’s suited for every outfit and every part of your day �” and you can even customize it with Your Watch, Your Way. Its bright AMOLED display and optional always-on mode mean you can see everything with a quick glance.
//                                        MADE TO MOVE WITH YOU:From a durable Corning® Gorilla® Glass 3 lens to the lightweight aluminum bezel and comfortable silicone band, this smartwatch was made to keep up with your lifestyle.
//                                        WRIST-BASED HEART RATE: The watch samples your heart rate multiple times per second and will alert you if it stays too high or too low while you’re at rest. It also helps gauge how hard you work during activities, even while swimming.
//                                        BODY BATTERY™ ENERGY MONITORING:See your body’s energy levels throughout the day so you’ll know when your body is charged up and ready for activity or drained and needing to recharge with restful sleep.
//                                        SLEEP MONITORING AND SLEEP SCORE:Understand how your body is recovering with our improved sleep monitoring feature. After waking up, you’ll receive a sleep score as well as breakdown of your sleep stages, Pulse Ox2 and respiration alongside detailed insights for improved sleep quality.
//                                        STRESS TRACKINGFind out if you’re having a calm, balanced or stressful day with continuous tracking. Relax reminders will even prompt you to do a short breathing activity when the watch detects stress.
//                                        HEALTH SNAPSHOT™ FEATURELog a 2-minute session to record key stats, including heart rate, heart rate variability, Pulse Ox, respiration and stress. Then generate a report with those stats to share via the Garmin Connect™ app on your compatible smartphone
//                                   """
//            ),
//            ProductDetail(
//                pid = "A_PRD_1000001",
//                brand = "Apple",
//                name = "Apple Watch Series 8 GPS",
//                price = 1039.00,
//                images = listOf("/apple-watch-series-8.jpg", "/apple-watch-series-8-1.jpg"),
//                description = """
//                                    A healthy leap ahead.
//                                    Apple Watch Series 8 is a powerful health companion. Featuring advanced health sensors and apps, so you can take an ECG, measure heart rate and blood oxygen, and track temperature changes for advanced insights into your menstrual cycle.
//                                  """
//            ),
//            ProductDetail(
//                pid = "A_PRD_1000003",
//                brand = "Swiss",
//                name = "Swiss Military Alps 2 Smartwatch",
//                price = 229.00,
//                images = listOf("/swiss-military-alps-2.jpg", "/swiss-military-alps-2-1.jpg"),
//                description = """
//                                        Make and Receive Calls
//                                        Store upto 1000 songs with 4GB built in memeory
//                                        Heart Monitoring
//                                        Blood Pressure Monitering
//                                        Oxygen level monitor SPO2
//                                        Workout Detection
//                                        1.85 mm HD Display
//                                   """
//            )
//        )
//
//        productList.addAll(pl)
//        productDetailList.addAll(pdl)
//    }
//
//    fun addProduct(product: Product): Product {
//        productList.add(product)
//        return product
//    }
//
//    fun getProducts(pageNumber: Int): List<Product>? {
//        return when (pageNumber) {
//            1 -> productList.subList(0, 4)
//            2 -> productList.subList(5, 9)
//            3 -> productList.subList(10, productList.size - 1)
//            else -> null
//        }
//    }
//
//    fun getProductDetails(productId: String): ProductDetail? {
//        return productDetailList.find { productId == it.pid }
//    }
//}
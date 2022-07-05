package com.sooft_sales.local_database;



import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.DepartmentModel;
import com.sooft_sales.model.ItemCartModel;
import com.sooft_sales.model.OrdersModel;
import com.sooft_sales.model.ProductModel;

import java.util.List;

public class DataBaseInterfaces {


    public interface ProductInsertInterface {
        void onProductDataInsertedSuccess(Boolean bol);
    }

    public interface CategoryInsertInterface {
        void onCategoryDataInsertedSuccess(Boolean bol);
    }

    public interface CategoryInterface {
        void onCategoryDataSuccess(List<DepartmentModel> categoryModelList);
    }

    public interface ProductInterface {
        void onProductDataSuccess(List<ProductModel> productModelList);
    }
    public interface LastProductInterface {
        void onLastProductDataSuccess(ProductModel productModel);
    }
    public interface OrderInsertInterface {
        void onOrderDataInsertedSuccess(boolean bol);
    }
    public interface ProductOrderInsertInterface {
        void onProductORderDataInsertedSuccess(Boolean bol);
    }
    public interface SearchInterface {
        void onSearchDataSuccess(CreateOrderModel createOrderModel);
    }
    public interface OrderupdateInterface {
        void onOrderUpdateDataSuccess();
    }
    public interface ProductupdateInterface {
        void onProductUpdateDataSuccess();
    }
    public interface AllOrderInterface {
        void onAllOrderDataSuccess(List<OrdersModel> createOrderModels);
    }
    public interface AllOrderProductInterface {
        void onAllOrderProductDataSuccess(List<ItemCartModel> itemCartModelList);
    }
}

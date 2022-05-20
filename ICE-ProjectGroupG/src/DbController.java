import java.sql.*;
import java.util.*;


public class DbController
{
    public DbController()
    {
    }

    private Connection getConnection()
    {
        return DbInfo.getConnection();
    }

    private ArrayList<Map<String, Object>> getResultSet(String sqlStatement, Object[] params)
    {
        ArrayList<Map<String, Object>> table = new ArrayList<>();
        try
        {
            PreparedStatement query = getConnection().prepareStatement(sqlStatement);
            for (int i = 0; i < params.length; i++)
            {
                String s = String.valueOf(params[i]);
                if (isInt(s))
                {
                    query.setInt(i, Integer.parseInt(s));
                } else
                {
                    query.setString(i, s);
                }
            }
            ResultSet rs = query.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (rs.next())
            {
                Map<String, Object> row = new HashMap<>();
                for (int i = 0; i < columnCount; i++)
                {
                    String columnName = metaData.getColumnName(1 + i).toUpperCase();
                    String columnType = metaData.getColumnTypeName(1 + i);
                    row.put(columnName, columnType.contains("CHAR") ? rs.getString(1 + i) : rs.getInt(1 + i));
                }
                table.add(row);
            }
            query.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return table;
    }
    private ArrayList<Map<String, Object>> getResultSet(String sqlStatement)
    {
        return getResultSet(sqlStatement, new Object[]{});
    }

    private Product createProductFromResultSet(ResultSet rs)
    {
        int locationId = 0;
        String name = "";
        int price = 0;
        String imagePath = "";

        try
        {
            locationId = rs.getInt("locationId");
            name = rs.getString("name");
            price = rs.getInt("price");
            imagePath = rs.getString("imagepath");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return new Product(locationId, name, price, imagePath);
    }

    private void executeUpdate(String sqlStatement, Object[] params)
    {
        try
        {
            PreparedStatement query = getConnection().prepareStatement(sqlStatement);
            for (int i = 0; i < params.length; i++)
            {
                String s = String.valueOf(params[i]);
                if (isInt(s))
                {
                    query.setInt(1 + i, Integer.parseInt(s));
                } else
                {
                    query.setString(1 + i, s);
                }
            }
            query.executeUpdate();
            query.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void addStock(int locationId, int quantity)
    {
        executeUpdate("UPDATE products SET quantity = quantity + ? WHERE locationId = ?", new Object[]{quantity, locationId});
    }
    public void updateStock(int locationId)
    {
        try
        {
            PreparedStatement query = getConnection().prepareStatement("Call UpdateStockProductSold (?)");
            query.setInt(1, locationId);
            query.executeUpdate();
            query.close();

        } catch (SQLException e)
        {
            System.out.println("ERROR," + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean isInt(String input)
    {
        int value = 0;
        try
        {
            value = Integer.parseInt(input);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public ArrayList<Product> getProducts(String sqlStatement, Object[] params)
    {

        ArrayList<Product> products = new ArrayList<>();
        try
        {
            PreparedStatement query = getConnection().prepareStatement(sqlStatement);
            for (int i = 0; i < params.length; i++)
            {
                String s = String.valueOf(params[i]);
                if (isInt(s))
                {
                    query.setInt(1 + i, Integer.parseInt(s));
                } else
                {
                    query.setString(1 + i, s);
                }
            }
            ResultSet rs = query.executeQuery();

            while (rs.next())
            {
                Product product = createProductFromResultSet(rs);
                product.setQuantity(rs.getInt("quantity"));
                products.add(product);
            }
            query.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return products;
    }
    public ArrayList<Product> getAllProducts()
    {
        return getProducts("Call GetAllProducts", new Object[]{});
    }

    public ArrayList<Map<String, Object>> getAllProductsWithIncomeData()
    {
        return getResultSet("Call GetAllProductsWithIncomeData");
    }

    public Product getProductById(int locationId)
    {
        ArrayList<Product> products = getProducts("Call GetProductByLocationId(?)", new Object[]{locationId});
        if (products.size() == 1)
        {
            return products.get(0);
        }
        return null;
    }





//    public boolean deleteAllProducts()
//    {
//        boolean isSuccess = false;
//        try
//        {
//            PreparedStatement delete = getConnection().prepareStatement("TRUNCATE TABLE products");
//            delete.execute();
//            delete.close();
//            isSuccess = true;
//        } catch (SQLException e)
//        {
//            System.out.println("ERROR," + e.getMessage());
//            e.printStackTrace();
//        }
//        return isSuccess;
//    }


//    private String getDbString(String sqlStatement)
//    {
//        String returnValue = "";
//        try
//        {
//            PreparedStatement query = getConnection().prepareStatement(sqlStatement);
//            ResultSet rs = query.executeQuery();
//            if (rs.next()) returnValue = rs.getString(1);
//            query.close();
//        } catch (SQLException e)
//        {
//        }
//        return returnValue;
//    }
//
//    private String getDbString(String sqlStatement, Object[] params)
//    {
//        String returnValue = "";
//        try
//        {
//            PreparedStatement query = getConnection().prepareStatement(sqlStatement);
//            for (int i = 0; i < params.length; i++)
//            {
//                String value = String.valueOf(params[i]);
//                if (isInt(value))
//                {
//                    query.setInt(1 + i, Integer.parseInt(value));
//                } else
//                {
//                    query.setString(1 + i, value);
//                }
//            }
//            ResultSet rs = query.executeQuery();
//            if (rs.next()) returnValue = rs.getString(1);
//            query.close();
//        } catch (SQLException e)
//        {
//            String error = e.getMessage();
//        }
//        return returnValue;
//    }
//
//
//



//    public int getIncome()
//    {
//        return getDbInt("Call GetTotalIncome");
//    }

//    public int getQuantity(int locationId)
//    {
//        return getDbInt("SELECT quantity FROM products WHERE locationId = ?", new Object[]{locationId});
//    }

//    public int getSold(int productTypeId)
//    {
//        return getDbInt("SELECT DISTINCT sold FROM producttype PT inner join products P on P.productTypeId = PT.id WHERE productTypeId = ?", new Object[]{productTypeId});
//    }


//    private boolean executeQuery(String sqlStatement, Object[] params)
//    {
//        boolean isSuccess = false;
//        try
//        {
//            PreparedStatement query = getConnection().prepareStatement(sqlStatement);
//            for (int i = 0; i < params.length; i++)
//            {
//                String s = String.valueOf(params[i]);
//                if (isInt(s))
//                {
//                    query.setInt(1 + i, Integer.parseInt(s));
//                } else
//                {
//                    query.setString(1 + i, s);
//                }
//            }
//            ResultSet rs = query.executeQuery();
//            query.close();
//            isSuccess = true;
//        } catch (SQLException e)
//        {
//           e.printStackTrace();
//        }
//
//        return isSuccess;
//    }





//
//    private int getDbInt(String sqlStatement)
//    {
//        int returnValue = 0;
//        try
//        {
//            returnValue = Integer.parseInt(getDbString(sqlStatement));
//        } catch (Exception e)
//        {
//        }
//        return returnValue;
//    }
//
//    private int getDbInt(String sqlStatement, Object[] params)
//    {
//        int returnValue = 0;
//        try
//        {
//            returnValue = Integer.parseInt(getDbString(sqlStatement, params));
//        } catch (Exception e)
//        {
//            returnValue = -1;
//        }
//        return returnValue;
//    }







}

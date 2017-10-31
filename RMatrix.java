

public class RMatrix
{
    public int columns, rows;
    double[][] matrix;

    RMatrix(int r, int c, double[][] input)
    {
        columns = c;
        rows = r;
        matrix = new double[r][c];
        //matrix=input;

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = input[i][j];
            }
        }
    }

    RMatrix(int fill, int r, int c)
    {
        columns = c;
        rows = r;
        matrix = new double[r][c];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = fill;
            }
        }
    }

    RMatrix(RMatrix old, int delete_r, int delete_c)
    {
        columns = old.columns - 1;
        rows = old.rows - 1;
        matrix = new double[rows][columns];

        for (int i = 0, i2 = 0; i < rows; i++, i2++)
        {
            for (int j = 0, j2 = 0; j < columns; j++, j2++)
            {
                if (i2 == delete_r)
                {
                    i2++;
                }
                if (j2 == delete_c)
                {
                    j2++;
                }
                matrix[i][j] = old.matrix[i2][j2];
            }
        }
    }

    public void display()
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                System.out.print(matrix[i][j]);
                System.out.print("  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public void scalar_mult(double n)
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] *= n;
            }
        }
    }

    public static RMatrix add(RMatrix a, RMatrix b)
    {
        RMatrix temp = new RMatrix(a.rows, a.columns, a.matrix);
        if (a.columns == b.columns && a.rows == b.rows)
        {
            for (int i = 0; i < a.rows; i++)
            {
                for (int j = 0; j < a.columns; j++)
                {
                    temp.matrix[i][j] += b.matrix[i][j];
                }
            }
        } else
        {
            throw new ArithmeticException("Matrix operation is not permitted\n");
        }
        return temp;
    }

    public static RMatrix sub(RMatrix a, RMatrix b)
    {
        RMatrix temp = new RMatrix(a.rows, a.columns, a.matrix);
        if (a.columns == b.columns && a.rows == b.rows)
        {
            for (int i = 0; i < a.rows; i++)
            {
                for (int j = 0; j < a.columns; j++)
                {
                    temp.matrix[i][j] -= b.matrix[i][j];
                }
            }
        } else
        {
            throw new ArithmeticException("Matrix operation is not permitted\n");
        }

        return temp;
    }


    public static RMatrix multiply(RMatrix a, RMatrix b)
    {
        RMatrix temp = new RMatrix(0, a.rows, b.columns);

        if (a.columns == b.rows)
        {
            for (int i = 0; i < a.rows; ++i)
            {
                for (int j = 0; j < b.columns; ++j)
                {
                    for (int k = 0; k < a.columns; ++k)
                    {
                        temp.matrix[i][j] += a.matrix[i][k] * b.matrix[k][j];
                    }
                    //temp.matrix[i][j] += a.matrix[i][j];
                }
            }
            temp.columns = b.columns;
            temp.rows = a.rows;

        } else
        {
            throw new ArithmeticException("Matrix operation is not permitted\n");
        }

        return temp;
    }

    public static RMatrix transpose(RMatrix a)
    {
        RMatrix temp = new RMatrix(0, a.columns, a.rows);

        for (int i = 0; i < temp.rows; i++)
        {
            for (int j = 0; j < temp.columns; j++)
            {
                temp.matrix[i][j] = a.matrix[j][i];
            }
        }

        return temp;
    }

    public double det()
    {
        if (columns != rows)
        {
            throw new ArithmeticException("Determinant not defined");
        } else if (columns == 1)
        {
            return matrix[0][0];
        } else
        {
            double determinant = 0;
            for (int i = 0; i < columns; i++)
            {
                determinant += matrix[0][i] * (new RMatrix(this, 0, i).det()) * java.lang.Math.pow(-1, (i));
            }
            return determinant;
        }
    }

    public static RMatrix cofactor(RMatrix a)
    {
        if (a.columns != a.rows)
        {
            throw new ArithmeticException("Matrix operation is not permitted");
        } else
        {
            RMatrix temp = new RMatrix(0, a.rows, a.columns);
            for (int i = 0; i < a.rows; i++)
            {
                for (int j = 0; j < a.columns; j++)
                {
                    temp.matrix[i][j] = (new RMatrix(a, i, j)).det() * java.lang.Math.pow(-1, (i + j));
                }
            }

            return temp;
        }
    }

    public static RMatrix adjoint(RMatrix a)
    {
        if (a.columns != a.rows)
        {
            throw new ArithmeticException("Matrix operation is not permitted");
        } else
        {
            return transpose(cofactor(a));
        }
    }

    public static RMatrix inverse(RMatrix a)
    {
        double determinant = a.det();
        if (a.columns != a.rows || determinant == 0)
        {
            throw new ArithmeticException("Matrix operation is not permitted");
        } else
        {
            RMatrix temp = adjoint(a);
            temp.scalar_mult(1 / determinant);
            return temp;
        }
    }

    public static double dotproduct(RMatrix a, RMatrix b)
    {
        double temp = 0;
        if (a.columns == b.columns && a.rows == b.rows && a.rows == 1)
        {
            for (int i = 0; i < a.columns; i++)
            {
                temp += a.matrix[0][i] * b.matrix[0][i];
            }
        } else
        {
            throw new ArithmeticException("Matrix operation is not permitted\n");
        }
        return temp;
    }

    public static RMatrix projection(RMatrix a, RMatrix b)
    {
        double scalar = (dotproduct(a, b) / dotproduct(a, a));
        RMatrix temp = new RMatrix(0, a.rows, a.columns);
        if (a.columns == b.columns && a.rows == b.rows && a.rows == 1)
        {
            for (int i = 0; i < a.columns; i++)
            {
                temp.matrix[0][i] = scalar * a.matrix[0][i];
            }
        } else
        {
            throw new ArithmeticException("Matrix operation is not permitted\n");
        }
        return temp;
    }

    public static RMatrix gramschmidt(RMatrix a)
    {
        RMatrix temp = new RMatrix(0, a.rows, a.columns);
        if (a.columns == a.rows)
        {

            RMatrix[] ortho = new RMatrix[a.rows];
            RMatrix[] orig = new RMatrix[a.rows];
            for (int i = 0; i < a.rows; i++)
            {
                ortho[i] = new RMatrix(0, 1, a.columns);
                orig[i] = new RMatrix(0, 1, a.columns);

                for (int j = 0; j < a.columns; j++)
                {
                    ortho[i].matrix[0][j] = a.matrix[i][j];
                    orig[i].matrix[0][j] = a.matrix[i][j];
                }
            }

            for (int i = 0; i < a.rows; i++)
            {
                for (int j = 0; j < i; j++)
                {
                    ortho[i] = RMatrix.sub(ortho[i], projection(ortho[j], orig[i]));
                }

                for (int j = 0; j < a.columns; j++)
                {
                    temp.matrix[i][j] = ortho[i].matrix[0][j];
                }
            }

//            //Test code
//            for (int i = 0; i < a.rows; i++)
//            {
//                for(int j=i+1;j<a.rows;j++)
//                {
//                    System.out.println(dotproduct(ortho[i],ortho[j]));
//                }
//            }

        } else
        {
            throw new ArithmeticException("Matrix operation is not permitted\n");
        }
        return temp;
    }
}

// Coded by Rishi K.S